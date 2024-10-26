from grid import *
from particle import Particle
from utils import *
import setting
import numpy as np

np.random.seed(setting.RANDOM_SEED)
from itertools import product


def create_random(count, grid):
    """
    Returns a list of <count> random Particles in free space.

    Parameters:
        count: int, the number of random particles to create
        grid: a Grid, passed in to motion_update/measurement_update

    Returns:
        List of Particles with random coordinates in the grid's free space.
    """
    # TODO: implement here
    # -------------------
    particles = []
    for _ in range(count):
        x, y = grid.random_free_place()
        h = random.uniform(0, 360)
        particles.append(Particle(x, y, h))
    return particles
    # -------------------


# ------------------------------------------------------------------------
def motion_update(old_particles, odometry_measurement, grid):
    """
    Implements the motion update step in a particle filter. Refer setting.py and utils.py for required functions and noise parameters.

    NOTE: the GUI will crash if you have not implemented this method yet. To get around this, try setting new_particles = old_particles.

    Arguments:
        old_particles: List
            list of Particles representing the belief before motion update p(x_{t-1} | u_{t-1}) in *global coordinate frame*
        odometry_measurement: Tuple
            noisy estimate of how the robot has moved since last step, (dx, dy, dh) in *local robot coordinate frame*

    Returns:
        a list of NEW particles representing belief after motion update \tilde{p}(x_{t} | u_{t})
    """
    new_particles = []

    for particle in old_particles:
        # extract the x/y/heading from the particle
        x_g, y_g, h_g = particle.xyh
        # and the change in x/y/heading from the odometry measurement
        dx_r, dy_r, dh_r = odometry_measurement

        new_particle = None
        # TODO: implement here
        # ----------------------------------
        # align odometry_measurement's robot frame coords with particle's global frame coords (heading already aligned)
        dx_g, dy_g = rotate_point(dx_r, dy_r, h_g)

        noisy_dx = add_gaussian_noise(dx_g, setting.ODOM_TRANS_SIGMA)
        noisy_dy = add_gaussian_noise(dy_g, setting.ODOM_TRANS_SIGMA)
        noisy_dh = add_gaussian_noise(dh_r, setting.ODOM_HEAD_SIGMA)

        # compute estimated new coordinate, using current pose and odometry measurements. Make sure to add noise to
        # simulate the uncertainty in the robot's movement.
        new_x = x_g + noisy_dx
        new_y = y_g + noisy_dy
        new_h = h_g + noisy_dh
        new_h = (new_h + 180) % 360 - 180

        # Create a new particle with this noisy coordinate
        new_particle = Particle(new_x, new_y, new_h)
        # ----------------------------------
        new_particles.append(new_particle)

    return new_particles


# ------------------------------------------------------------------------
def generate_marker_pairs(robot_marker_list, particle_marker_list):
    """ Pair markers in order of closest distance

        Arguments:
        robot_marker_list -- List of markers observed by the robot
        particle_marker_list -- List of markers observed by the particle

        Returns: List[Tuple] of paired robot and particle markers
    """
    marker_pairs = []
    while len(robot_marker_list) > 0 and len(particle_marker_list) > 0:
        # TODO: implement here
        # ----------------------------------
        # find the (particle marker,robot marker) pair with shortest grid distance
        min_distance = float('inf')
        pair_to_add = None
        for r_marker in robot_marker_list:
            for p_marker in particle_marker_list:
                distance = grid_distance(r_marker[0], r_marker[1], p_marker[0], p_marker[1])
                if distance < min_distance:
                    min_distance = distance
                    pair_to_add = (r_marker, p_marker)

        # add this pair to marker_pairs and remove markers from corresponding lists
        if pair_to_add:
            marker_pairs.append(pair_to_add)
            robot_marker_list.remove(pair_to_add[0])
            particle_marker_list.remove(pair_to_add[1])
        # ----------------------------------
    return marker_pairs


def gaussian_pdf(x, mean, sigma):
    return (1.0 / (sigma * math.sqrt(2 * math.pi))) * math.exp(-0.5 * ((x - mean) ** 2) / (sigma ** 2))


# ------------------------------------------------------------------------
def marker_likelihood(robot_marker, particle_marker):
    """ Calculate likelihood of reading this marker using Gaussian PDF. The
        standard deviation of the marker translation and heading distributions
        can be found in settings.py

        Arguments:
        robot_marker -- Tuple (x,y,theta) of robot marker pose
        particle_marker -- Tuple (x,y,theta) of particle marker pose

        Returns: float probability
    """
    # l = 0.0
    # TODO: implement here
    # ----------------------------------
    # find the distance between the particle marker and robot marker
    rx, ry, rtheta = robot_marker
    px, py, ptheta = particle_marker
    distance = grid_distance(rx, ry, px, py)

    # find the difference in heading between the particle marker and robot marker
    heading_diff = diff_heading_deg(rtheta, ptheta)

    # calculate the likelihood of this marker using the gaussian pdf
    distance_likelihood = gaussian_pdf(distance, 0, setting.MARKER_TRANS_SIGMA)
    heading_likelihood = gaussian_pdf(heading_diff, 0, setting.MARKER_HEAD_SIGMA)

    l = distance_likelihood * heading_likelihood
    # ----------------------------------
    return l


# ------------------------------------------------------------------------
def particle_likelihood(robot_marker_list, particle_marker_list):
    """ Calculate likelihood of the particle pose being the robot's pose

        Arguments:
        robot_marker_list -- List of markers observed by the robot
        particle_marker_list -- List of markers observed by the particle

        Returns: float probability
    """
    l = 1.0
    marker_pairs = generate_marker_pairs(robot_marker_list, particle_marker_list)
    # TODO: implement here
    # ----------------------------------
    # update the particle likelihood using the likelihood of each marker pair
    # HINT: consider what the likelihood should be if there are no pairs generated
    for robot_marker, particle_marker in marker_pairs:
        pair_likelihood = marker_likelihood(robot_marker, particle_marker)
        l *= pair_likelihood

    if len(marker_pairs) == 0:
        l = 0.0
    # ----------------------------------
    return l


# ------------------------------------------------------------------------
def measurement_update(particles, measured_marker_list, grid):
    """ Particle filter measurement update

        Arguments:
        particles -- input list of particle represents belief \tilde{p}(x_{t} | u_{t})
                before measurement update (but after motion update)

        measured_marker_list -- robot detected marker list, each marker has format:
                measured_marker_list[i] = (rx, ry, rh)
                rx -- marker's relative X coordinate in robot's frame
                ry -- marker's relative Y coordinate in robot's frame
                rh -- marker's relative heading in robot's frame, in degree

                * Note that the robot can only see markers which is in its camera field of view,
                which is defined by ROBOT_CAMERA_FOV_DEG in setting.py
				* Note that the robot can see mutliple markers at once, and may not see any one

        grid -- grid world map, which contains the marker information,
                see grid.py and CozGrid for definition
                Can be used to evaluate particles

        Returns: the list of particles represents belief p(x_{t} | u_{t})
                after measurement update
    """
    measured_particles = []
    particle_weights = []
    num_rand_particles = 25

    if len(measured_marker_list) > 0:
        for p in particles:
            x, y = p.xy
            if grid.is_in(x, y) and grid.is_free(x, y):
                robot_marker_list = measured_marker_list.copy()
                particle_marker_list = p.read_markers(grid)
                # l = None

                # TODO: implement here
                # ----------------------------------
                # compute the likelihood of the particle pose being the robot's
                # pose when the particle is in a free space
                l = particle_likelihood(robot_marker_list, particle_marker_list)
                # ----------------------------------
            else:
                pass
                # TODO: implement here
                # ----------------------------------
                # compute the likelihood of the particle pose being the robot's pose
                # when the particle is NOT in a free space
                l = 0.0
                # ----------------------------------

            particle_weights.append(l)
    else:
        particle_weights = [1.] * len(particles)

    # TODO: Importance Resampling
    # ----------------------------------
    # if the particle weights are all 0, generate a new list of random particles
    # normalize the particle weights
    total_weight = sum(particle_weights)
    if total_weight > 0:
        normalized_weights = [w / total_weight for w in particle_weights]
    else:
        normalized_weights = [1.0 / len(particles)] * len(particles)

    # create a fixed number of random particles and add to measured particles
    num_resample = len(particles) - num_rand_particles

    # resample remaining particles using the computed particle weights
    if total_weight == 0:
        measured_particles.extend(create_random(len(particles), grid))
    else:
        indices = np.random.choice(range(len(particles)), size=num_resample, p=normalized_weights)
        measured_particles.extend([particles[i] for i in indices])

        measured_particles.extend(create_random(num_rand_particles, grid))

    # ----------------------------------

    return measured_particles

import math
import numpy as np
import cv2
from contour import box_measure

def vision_lidar_distance_calculation(image, lidar_range_array, fov):
    '''
    arguments: 
        image: image from the camera
        lidar_range_array: array of values representing a 2D lidar scan
        fov: field of view of the camera
    
    return values:
        distance: distance of the object from the robot  (m)
        angle: angle (heading) of the marker's centroid with respect to the robot (deg)

    This function calculates the distance between the sign and the robot 
    in centimeters and the angle from the robot to the marker in degrees.
       
    '''
    
    # Step 1: Find centroid coordinates for the sign in the image
    centroid = box_measure(image)

    
    # Step 2: Get the width of the image
    image_width = image.shape[1]
    
    # Step 3: Compute the ratio between fov and image_width
    fov_to_pixels_ratio = fov / image_width
    
    # Step 4: Calculate the pixel difference between image centre and centroid pixel
    image_centre_x = image_width / 2
    pixel_difference = image_centre_x - centroid[0]
    
    # Step 5: Compute the angle using outputs from step 3 and 4
    angle = pixel_difference * fov_to_pixels_ratio
    angle = math.degrees(angle)
    
    # Step 6: Get the distance at the calculated angle using lidar (index = angle value)
    lidar_index = int(angle)
    distance = lidar_range_array[lidar_index]

    #distance = -100
    #angle = 180

    return distance, angle
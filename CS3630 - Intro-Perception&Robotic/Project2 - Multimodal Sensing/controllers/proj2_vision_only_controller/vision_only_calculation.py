import math
from contour import box_measure

def vision_only_depth_calculation(image1, image2, fov, camera_translation):
    '''
    arguments: 
        image1: image from the first camera
        image2: image from the second camera
        fov: field of view of the cameras, both cameras have the same value in our robot
        camera_translation: horizontal displacement between the two cameras
    
    return values:
        depth: depth of the object (perpendicular distance of the object from the 2 cameras)
        angle (heading) of the marker's centroid with respect to the robot

    This function calculates :
    1. The perpendicular distance of the object from the 2 cameras (m)
    2. The angle at the which the traffic sign (marker) is present with respect to the robot (deg)
    
    focal_length = focal length of the camera
    focal_length = image_width / (2 * math.tan(fov / 2))

    '''
    
    # Step 1: Find centroid coordinates for the sign in both the images
    centroid1 = box_measure(image1)
    centroid2 = box_measure(image2)
    
    # Step 2: Calculate focal length of the camera using width of image and fov
    image_width = image1.shape[1]
    focal_length = image_width / (2 * math.tan(fov / 2))
    
    # Step 3: Depth calculation
    disparity = abs(centroid1[0] - centroid2[0])
    depth = (focal_length * camera_translation) / disparity
    
    # Step 4: Compute the ratio between fov and image_width
    fov_ratio = fov / image_width

    # Step 5: Calculate the pixel difference between image centre and centroid1 / centroid2 pixel
    pixel_diff = (image_width / 2) - centroid1[0]

    # Step 6: Compute the angle of the marker from the robot (hint: Apply trigonometry)
    alpha = abs(pixel_diff * fov_ratio)
    if (pixel_diff < 0):
        base = (depth * math.tan(alpha)) - camera_translation / 2
        angle = -math.degrees(math.atan(base / depth))
    else:
        base = (depth * math.tan(alpha)) + camera_translation / 2
        angle = math.degrees(math.atan(base / depth))

    #depth = -100
    #angle = 180

    return depth, angle
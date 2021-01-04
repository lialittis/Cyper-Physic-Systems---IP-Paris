"""
 Simple 2D ICP implementation
 author: David Filliat
"""

import numpy as np
from scipy.spatial import KDTree
import math


# A few helper function

def angle_wrap(a):
    """
    Keep angle between -pi and pi
    """
    return np.fmod(a + np.pi, 2*np.pi ) - np.pi


def mean_angle(angleList):
    """
    Compute the mean of a list of angles
    """

    mcos = np.mean(np.cos(angleList))
    msin = np.mean(np.sin(angleList))

    return math.atan2(msin, mcos)

def points_filter(dat,thres_dist):
    """
    """
    print("Filtering points which are too close to each other...\n")
    count_pass = 0
    count_append = 0
    for i in range(dat.shape[1]):
        #print(np.array([dat[0,i],dat[1,i]]))
        if i==0:
            dat_filt = np.array([dat[0,i],dat[1,i]],dtype=float).reshape(2,1)
            # print(dat_filt)
            current_position = dat_filt
            count_append += 1
        else:
            distance = math.sqrt(math.pow(dat[0,i]-current_position[0,0],2) + math.pow(dat[1,i]-current_position[1,0],2))
            if distance>thres_dist:
                count_append +=1
                current_position = np.array([dat[0,i],dat[1,i]],dtype=float).reshape(2,1)
                dat_filt = np.append(dat_filt,current_position,axis=1)
                # print(dat_filt)
            else:
                count_pass += 1
    #print(dat_filt)
    print("count_append is: ",count_append," and count_pass is: ",count_pass,"\n")
    return dat_filt
            
            
def match_filter(dat_filt,distance,percent,index):
    """
    """
    # print("Match filtering...\n")
    
    num_data = int(len(index)*percent)
    # print("num_data is: ",num_data)
    order = range(len(index))
    zipped = zip(distance,order)
    # print(list(zipped))
    sort_zipped = sorted(zipped,key=lambda x:(x[0],x[1]))

    result = zip(*sort_zipped)
    new_distance, new_order = [list(x) for x in result]
    # print(new_distance)
    # print(new_order)
    for i in range(num_data):
        if i == 0:
            dat_match = np.array([dat_filt[0,new_order[i]],dat_filt[1,new_order[i]]]).reshape(2,1)
            current_position = dat_match
            new_index = [index[new_order[i]]]
        else:
            current_position = np.array([dat_filt[0,new_order[i]],dat_filt[1,new_order[i]]]).reshape(2,1)
            dat_match = np.append(dat_match,current_position,axis=1)
            new_index.append(index[new_order[i]])

    # print(dat_match,new_index)
    return dat_match,new_index



def icp(model, data, maxIter, thres):
    """
    ICP (iterative closest point) algorithm
    Simple ICP implementation for teaching purpose
    - input
    model : scan taken as the reference position
    data : scan to align on the model
    maxIter : maximum number of ICP iterations
    thres : threshold to stop ICP when correction is smaller
    - output
    R : rotation matrix
    t : translation vector
    meandist : mean point distance after convergence
    """

    print('Running ICP, ', end='')

    # Various inits
    olddist = float("inf")  # residual error
    maxRange = 10  # limit on the distance of points used for ICP

    # Create array of x and y coordinates of valid readings for reference scan
    valid = model["ranges"] < maxRange
    ref = np.array([model["x"], model["y"]])
    ref = ref[:, valid]

    # Create array of x and y coordinates of valid readings for processed scan
    valid = data["ranges"] < maxRange
    dat = np.array([data["x"], data["y"]])
    dat = dat[:, valid]

    # ----------------------- TODO ------------------------
    # Filter data points too close to each other
    # Put the result in dat_filt
    
    # dat_filt = dat
    minimal_distance = 0.1
    dat_filt = points_filter(dat,minimal_distance)

    # Initialize transformation to identity
    R = np.eye(2)
    t = np.zeros((2, 1))

    # Main ICP loop
    for iter in range(maxIter):

        # ----- Find nearest Neighbors for each point, using kd-trees for speed
        tree = KDTree(ref.T)
        distance, index = tree.query(dat_filt.T)
        meandist = np.mean(distance)
        # print("meandist : ", meandist)
        # print("distance : ", distance)

        # ----------------------- TODO ------------------------
        # filter points matchings, keeping only the closest ones
        # you have to modify :
        # - 'dat_matched' with the points
        # - 'index' with the corresponding point index in ref
        precent = 0.65
        # dat_matched = dat_filt
        dat_matched,index = match_filter(dat_filt,distance,precent,index)
        # print("index : ",index)

        # ----- Compute transform

        # Compute point mean
        mdat = np.mean(dat_matched, 1)
        mref = np.mean(ref[:, index], 1)

        # Use SVD for transform computation
        C = np.transpose(dat_matched.T-mdat) @ (ref[:, index].T - mref)
        u, s, vh = np.linalg.svd(C)
        Ri = vh.T @ u.T
        Ti = mref - Ri @ mdat

        # Apply transformation to points
        dat_filt = Ri @ dat_filt
        dat_filt = np.transpose(dat_filt.T + Ti)

        # Update global transformation
        R = Ri @ R
        t = Ri @ t + Ti.reshape(2, 1)

        # Stop when no more progress
        if abs(olddist-meandist) < thres:
            break

        # store mean residual error to check progress
        olddist = meandist

    print("finished with mean point corresp. error {:f}".format(meandist))

    return R, t, meandist

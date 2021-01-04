#!/bin/env python3

"""

TP Reactive control for mobile robots

author: Alexandre Chapoutot and David Filliat

"""
import math

import numpy as np
import matplotlib.pyplot as plt

from matplotlib.path import Path
import matplotlib.patches as patches

import vehicle_model as vm


# Parameters
show_animation = True


# Compute control
def bicycle_to_path_control(xTrue, waypoints, waypointId, xGoal):
    """
    Compute control from current pose to follow a path
    Input:
    - xTrue : current [x, y, \theta]' robot pose
    - waypoints : set of points defining the path [ [x1, x2], [y1, y2], ...]
    - wayPointId : next waypoint in the path. Should be updated by the function
    - XGoal : [x, y, \theta] goal pose.  Should be updated by the function
    Output:
    - u : [v, \phi] control: speed and steering wheel angle
    - wayPointId : updated next waypoint in the path.
    - XGoal : updated goal pose
    """
    waypoint = waypoints[waypointId]
    print(waypoint)
    # move_point = xGoal
    
    # lookahead distance
    lookahead = 1

    # fixed step
    delta = 0.5

    # Control Gains
    K_rho = 50
    K_alpha = 6

    u = np.array([0.0, 0.0])

    rho = np.sqrt(pow((xGoal[0] - xTrue[0]),2) + pow((xGoal[1]-xTrue[1]),2))

    # flag = 0
    while rho < lookahead and np.array_equal(xGoal,waypoint)==False :
        distance_xGoal_point = np.sqrt(pow((xGoal[0] - waypoint[0]),2) + pow((xGoal[1]-waypoint[1]),2))
        # print(distance_xGoal_point)
        if distance_xGoal_point>=delta :
            xGoal[0] = xGoal[0] + (delta/distance_xGoal_point)*(waypoint[0]-xGoal[0])
            xGoal[1] = xGoal[1] + (delta/distance_xGoal_point)*(waypoint[1]-xGoal[1])
            # print(waypointId)
        else:
            xGoal = waypoint
            # waypointId += 1
            # flag = 1
            # print(flag)
        rho = np.sqrt(pow((xGoal[0] - xTrue[0]),2) + pow((xGoal[1]-xTrue[1]),2))
    
    alpha = vm.angle_wrap(np.arctan2((xGoal[1]-xTrue[1]),(xGoal[0]-xTrue[0])) - xTrue[2])
    u[0] = K_rho*rho
    u[1] = K_alpha*alpha

    if rho < 0.005:
        waypointId += 1
    return (u, waypointId, xGoal)


# Main function
def main():

    # Path definition
    waypoints = np.array([[0.1, 0, 0],
                          [4, 0, 0],
                          [4, 4, 0],
                          [3.5, 1, 0],
                          [0, 4, 0],
                          [1, 2, -1.57]])

    xTrue = np.array([0.0, 0.0, 0.0])
    waypointId = 0
    xGoal = waypoints[0]

    # Initial speed and angle
    v = 0
    phi = 0

    if show_animation:
        # for stopping simulation with the esc key.
        plt.gcf().canvas.mpl_connect('key_release_event',
                lambda event: [exit(0) if event.key == 'escape' else None])
        plt.grid(True)
        plt.axis("equal")
        # display initial position
        vm.plot_arrow(xTrue[0], xTrue[1], xTrue[2], fc='r')
        # display Path
        plt.plot(waypoints[:, 0:1], waypoints[:, 1:2], 'ko--', linewidth=2,
                 markersize=5)

    k = 0
    last = waypoints[-1]
    while np.amax(np.absolute(vm.dist(xTrue[0:2], last[0:2]))) > 0.05 and k < 25000:
        # Compute Control
        [u, waypointId, xGoal] = bicycle_to_path_control(xTrue, waypoints,
                                                         waypointId, xGoal)

        # Simulation of the vehicle motion
        [xTrue, u, v, phi] = vm.simulate_bicycle(xTrue, u, v, phi)

        if show_animation:
            if k % 150 == 1:
                vm.plot_arrow(xTrue[0], xTrue[1], xTrue[2], fc='g')
                plt.plot(xTrue[0], xTrue[1], ".g")
                plt.pause(0.001)
        k = k + 1

    print ("Steps to goal : ", k)
    if show_animation:
        vm.plot_arrow(xTrue[0], xTrue[1], xTrue[2], fc='g')
        plt.plot(xTrue[0], xTrue[1], ".g")
        plt.pause(0.001)
        plt.savefig('bicycle_to_path.png')
        print('Finished. Press Q in window to exit.')
        plt.show()


if __name__ == '__main__':
    print(__file__ + " start!!")
    main()
    print(__file__ + " Done!!")

package ca.mcgill.ecse211;

import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Navigation is a helper class which allows does the calculations for our robot
 */
// THIS IS A STATIC CLASS, DO NOT PUT INSTANCES

public class Navigation {
    
    
    private static double currentTheta; // the current angle
    
    /**
     * Converts input distance to the total rotation of each wheel needed to cover
     * that distance.
     *
     * @param distance: distance needed to travel based on wheel radius
     * @return the wheel rotations necessary to cover the distance
     */
    public static int convertDistance(double distance) {
        return (int) ((180.0 * distance) / (PI * WHEEL_RAD));
    }
    
    /**
     * Converts input angle to the total rotation of each wheel needed to rotate the
     * robot by that angle.
     *
     * @param angle: angle to rotate
     * @return the wheel rotations necessary to rotate the robot by the angle
     */
    public static int convertAngle(double angle) {
        return convertDistance(PI * TRACK * angle / 360.0);
    }
    
    /**
     * Check if the robot can avoid right or left depending on the current X,Y and Theta
     *
     * @param curX: current X
     * @param curY: current Y
     * @return: dodge right or left
     */

    public static boolean canAvoidRight() {
        double[] currLocation = odometer.getXYT();

        double curX = currLocation[0];
        double curY = currLocation[1];
        currentTheta = currLocation[2];

        currentTheta = odometer.getXYT()[2];
        // there are 4 cases
        if (currentTheta > 315 || currentTheta < 45) { // case 1: going up
            return (curX < TILE_SIZE * (island.ll.x + (island.ur.x - island.ll.x)/2));
        } else if (currentTheta > 225 && currentTheta < 315) { // case 2: going left 
            return (curY < TILE_SIZE * (island.ll.y + (island.ur.y - island.ll.y)/2));
        } else if (currentTheta > 135 && currentTheta < 225) { // case 3: going  down 
            return (curX > TILE_SIZE * (island.ll.x + (island.ur.x - island.ll.x)/2));
        } else if (currentTheta > 45 && currentTheta < 135) { // case 4: going right 
            return (curY > TILE_SIZE * (island.ll.y + (island.ur.y - island.ll.y)/2));
        }
        return true; // default true
    }
    
    /**
     * Turn 90 degrees right and move 1 tile over
     */
    public static void avoidRight() {
        leftMotor.setSpeed(ROTATE_SPEED);
        rightMotor.setSpeed(ROTATE_SPEED);
        leftMotor.rotate(convertAngle(90), true);
        rightMotor.rotate(-convertAngle(90), false);

        leftMotor.rotate((int) TILE_SIZE, true);
        rightMotor.rotate((int)TILE_SIZE, false);
    }

    /**
     * Turn 90 degrees left and move 1 tile over
     */
    public static void avoidLeft() {
        leftMotor.setSpeed(ROTATE_SPEED);
        rightMotor.setSpeed(ROTATE_SPEED);
        leftMotor.rotate(-convertAngle(90), true);
        rightMotor.rotate(convertAngle(90), false);

        leftMotor.rotate((int)TILE_SIZE, true);
        rightMotor.rotate((int)TILE_SIZE, false);
    }
    
    /**
     * Travel towards the destination specified in the wayPoint.
     *
     * @param x: the x coordinate of the final destination
     * @param y: the y coordinate of the final destination
     */
    public static void travelTo(double x, double y) {
        
        //Get initial coordinates
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        double currentTheta = 0;
        
        //Calculate angle needed to turn
        //3 cases:
        //Case 1: X == currentX && Y != currentY, adjust Y
        if (currentX == x) {
            if (currentY > y) {
                currentTheta = Math.PI;
            } else if (currentY < y) {
                currentTheta = 0;
            }
            //Case 2: X != currentX && Y == currentY, adjust X
        } else if (currentY == y) {
            if (currentX > x) {
                currentTheta = -Math.PI / 2;
            } else if (currentX < x) {
                currentTheta = Math.PI / 2;
            }
        }
        //Case 3: X != currentX && Y != currentY, adjust both
        else {
            currentTheta = Math.atan((currentX - x) / (currentY - y));
            if (currentY > y) {
                currentTheta += Math.PI;
            }
        }
        
        turnTo(Math.toDegrees(currentTheta)); // Turn the robot towards the destination
        
        leftMotor.setSpeed(FORWARD_SPEED);
        rightMotor.setSpeed(FORWARD_SPEED+5);
        
        double distToTravel = euclideanDistance(currentX, currentY, x, y);
        //    System.out.println(distToTravel);
        rightMotor.rotate(convertDistance(distToTravel), true);
        leftMotor.rotate(convertDistance(distToTravel), false);
//        Sound.beep();

}

    
    /**
     * This method will return the euclidean distance of the robot with respect to the current destination
     *
     * @param curX  :current X coordinate
     * @param curY  : current Y coordinate
     * @param targX :target X coordinate
     * @param targY : target Y coordinate
     * @return double error : euclidean error
     */
    public static double euclideanDistance(double curX, double curY, double targX, double targY) {
        return Math.sqrt(Math.pow((curX - targX), 2) + Math.pow((curY - targY), 2));
    }
    
    /**
     * Turns robot head towards a X,Y location
     *
     * @param x: Target x to turn to
     * @param y: Target Y to turn to
     */
    public static void turnToXY(double x, double y) {
        //Get initial coordinates
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        double currentTheta = 0;
        
        //Calculate angle needed to turn
        //3 cases:
        //Case 1: X == currentX && Y != currentY, adjust Y
        if (currentX == x) {
            if (currentY > y) {
                currentTheta = Math.PI;
            } else if (currentY < y) {
                currentTheta = 0;
            }
            //Case 2: X != currentX && Y == currentY, adjust X
        } else if (currentY == y) {
            if (currentX > x) {
                currentTheta = -Math.PI / 2;
            } else if (currentX < x) {
                currentTheta = Math.PI / 2;
            }
        }
        //Case 3: X != currentX && Y != currentY, adjust both
        else {
            currentTheta = Math.atan((currentX - x) / (currentY - y));
            if (currentY > y) {
                currentTheta += Math.PI;
            }
        }
        
        turnTo(Math.toDegrees(currentTheta)); // Turn the robot towards the destination
    }
    /**
     * Rotate the vehicle towards the desired angle
     *
     * @param theta: angle wanted to rotate to
     */
    public static void turnTo(double theta) {
        double dTheta = getAngle(theta);
        // Sets the speed
        leftMotor.setSpeed(ROTATE_SPEED);
        rightMotor.setSpeed(ROTATE_SPEED);
        // Rotates
        rightMotor.rotate(-convertAngle(dTheta), true);
        leftMotor.rotate(convertAngle(dTheta), false);
    }
    
    /**
     * minimizes the turn to [-180.180]
     *
     * @param theta: the desired angle
     * @return the angle of rotation to face the desired angle with the minimum
     */
    public static double getAngle(double theta) {
        currentTheta = odometer.getXYT()[2];
        //Computes the angle between 0 and 360
        double dTheta = (theta - currentTheta + 360) % 360;
        
        //Converts to minimum angle
        if (Math.abs(dTheta - 360) < dTheta) {
            dTheta -= 360;
        }
        
        return dTheta;
    }
    
}

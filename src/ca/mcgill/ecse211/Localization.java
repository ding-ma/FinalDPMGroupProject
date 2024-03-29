package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Localization methods in order to centralize the robot at a specific point
 */
public class Localization {
    static double[] angleAtLines = new double[4];
    
    /**
     * returns the current location of the ultrasonic sensor
     *
     * @param angle difference between angle on one line
     * @return: coordinate location (x or y)
     */
    public static double currentLocation(double angle) {
        return -Math.abs(SENSOR_LOCATION * Math.cos(Math.toRadians(angle / 2)));
    }
    
    /**
     * Performs falling edge localization
     */
    public static void fallingEdge() {
        leftMotor.setSpeed(225);
        rightMotor.setSpeed(225);
    
        double angle1;
        double angle2;
        double turnAngle;
        while (SensorsPoller.getCurrentDistance() < 75) {
            leftMotor.backward();
            rightMotor.forward();
        }
    
        //Rotate to face away from the wall
        while (SensorsPoller.getCurrentDistance() < distFallingEdge + tolFallingEdge) {
            leftMotor.backward();
            rightMotor.forward();
        }
    
        //Rotate towards the wall
        while (SensorsPoller.getCurrentDistance() > distFallingEdge) {
            leftMotor.backward();
            rightMotor.forward();
        }
    
        leftMotor.stop(true);
        rightMotor.stop(false);
        sleep(10);
        //Record first angle
        angle1 = odometer.getXYT()[2];
    
        while (SensorsPoller.getCurrentDistance() < 75) {
            leftMotor.forward();
            rightMotor.backward();
        }
    
        //Rotate to face away from the wall
        while (SensorsPoller.getCurrentDistance() < distFallingEdge + tolFallingEdge) {
            leftMotor.forward();
            rightMotor.backward();
        }
        
        //Rotate to face the other wall
        while (SensorsPoller.getCurrentDistance() > distFallingEdge) {
            leftMotor.forward();
            rightMotor.backward();
        }
        
        leftMotor.stop(true);
        rightMotor.stop(false);
        //Record second angle
        sleep(10);
        angle2 = odometer.getXYT()[2];
        
        double dTheta = 0;
        //Compute the angle:
        //Case 1: The first angle is smaller than the second
        if (angle1 < angle2) {
            dTheta = 45 - (angle1 + angle2) / 2;
    
        }
        //Case 2: The first angle is greater than the second
        else if (angle1 > angle2) {
            dTheta = 225 - (angle1 + angle2) / 2;
        }
        turnAngle = dTheta + odometer.getXYT()[2];
    
        //Turn to 0 degrees
        leftMotor.rotate(-Navigation.convertAngle(turnAngle) + 20, true);
        rightMotor.rotate(Navigation.convertAngle(turnAngle) - 20, false);
    
        odometer.setTheta(0);
    }
    
    /**
     * travel until line is hit, for localization
     *
     * @param turnAngle angle which we want to turn the robot
     */
    public static void travelUntilLineHit(int turnAngle) {
        // Face origin
        odometer.setTheta(SensorsPoller.getCurrentAngle());
        Navigation.turnTo(turnAngle);
        leftMotor.setSpeed(220);
        rightMotor.setSpeed(220);
        while (true) {
        
        
            leftMotor.forward();
            rightMotor.forward();
            // Move backwards to put the rotation point on the line
            if (SensorsPoller.getCurrentLightIntensity() < 0.3) { // Compare to previous intensity
                leftMotor.stop(true);
                rightMotor.stop(false);
                sleep(10);
                leftMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), true);
                rightMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), false);

//                Sound.beep();
                break;
            }
        }
    
    
    }
    
    /**
     * Performs light localization at a point. Travels and sets the odometer to that point.
     *
     * @param xCoord x-coordinate to localize
     * @param yCoord y-coordinate to localize
     */
    public static void centralizeAtPoint(double xCoord, double yCoord) {
        int currLineDetected = 0;// Count how many lines we've detected this far.
        double currentX;
        double currentY;
        double angleX;
        double angleY;
        leftMotor.setSpeed(250);
        rightMotor.setSpeed(250);
        boolean onlyHitOnce = false;
        while (currLineDetected < 4) {
            // Rotate in place to find the next line.
            leftMotor.backward();
            rightMotor.forward();
            if (SensorsPoller.getIsLineHit() == true && !onlyHitOnce) { //Compare intensities
                angleAtLines[currLineDetected] = odometer.getXYT()[2];
                currLineDetected++;
                onlyHitOnce = true;
            }
            if (SensorsPoller.getIsLineHit() == false) {
                onlyHitOnce = false;
            }
        }
        // Stops the motors
        leftMotor.stop(true);
        rightMotor.stop(false);
        
        // Find out our angle using the difference of the lines.
        angleY = angleAtLines[2] - angleAtLines[0]; // Lines 0 and 2 are the vertical lines
        angleX = angleAtLines[3] - angleAtLines[1]; // Lines 1 and 3 are the horizontal lines
        
        // Get dx and dy
        currentX = currentLocation(angleY);
        currentY = currentLocation(angleX);
        
        odometer.setXYT(currentX + xCoord, currentY + yCoord, odometer.getXYT()[2]); // updates odo with current location compared to
        // origin
        if (!(Math.abs(angleX) < 3 && Math.abs(angleY) < 3)) {
            Navigation.travelTo(xCoord, yCoord); // make it travel to origin
        }
        
        Navigation.turnTo(WifiParser.getLocalizeStartingPoint()[2]);
        
        System.out.println("current angle is " + SensorsPoller.getCurrentAngle());
        odometer.setXYT(xCoord, yCoord, SensorsPoller.getCurrentAngle());
        
        
    }
    
    /**
     * Cleaner way of sleeping threads
     *
     * @param timeMs time wished to sleep
     */
    private static void sleep(int timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (Exception ignored) {
        }
        
    }
    
}
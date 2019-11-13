package ca.mcgill.ecse211;

import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

public class Localization {
    
    /**
     * returns the current location of the ultrasonic sensor
     *
     * @param angle: difference between angle on one line
     * @return: coordinate location (x or y)
     */
    public static double currentLocation(double angle) {
        return -Math.abs(SENSOR_LOCATION * Math.cos(Math.toRadians(angle / 2)));
    }
    
    public static void fallingEdge() {
        
        double angle1;
        double angle2;
        double turnAngle;
        //fixes bug when us sensor is pointing at wall
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
        
        
        //Record first ange
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
        
        //Record second angle
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
        leftMotor.rotate(-Navigation.convertAngle(turnAngle), true);
        rightMotor.rotate(Navigation.convertAngle(turnAngle), false);
        odometer.setXYT(0.0, 0.0, 0.0);
        
    }
    
    public static void travelUntilLineHit(int turnAngle) {
        // Face origin
        Navigation.turnTo(turnAngle);
        leftMotor.setSpeed(ROTATE_SPEED);
        rightMotor.setSpeed(ROTATE_SPEED);
        while (true) {
            System.out.println(SensorsPoller.getCurrentLightIntensity());
            
            leftMotor.forward();
            rightMotor.forward();
            if (SensorsPoller.getCurrentLightIntensity() < 0.3) { // Compare to previous intensity
                leftMotor.stop();
                rightMotor.stop();
                Sound.beep();
                break;
            }
        }
        // Move backwards to put the rotation point on the line
        leftMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), true);
        rightMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), false);
    }
    
    public static void centralizeAtPoint(double xCoord, double yCoord){
        double[] angleAtLines = new double[4];
        double curIntensity = SensorsPoller.getCurrentDistance();
        double previousIntensity = SensorsPoller.getPreviousLightIntensity();
            boolean onlyHitOnce = false;
            int currLineDetected = 0;// Count how many lines we've detected this far.
            double currentX;
            double currentY;
            double angleX;
            double angleY;
            previousIntensity = curIntensity;
            while (currLineDetected < 4) {
                // Rotate in place to find the next line.
                leftMotor.backward();
                rightMotor.forward();
                if (SensorsPoller.getIsLineHit() && !onlyHitOnce) { // Compare to previous intensity
                    onlyHitOnce = true;
                    angleAtLines[currLineDetected] = odometer.getXYT()[2];
                    currLineDetected++;
                    Sound.beep();
                }
                if (!SensorsPoller.getIsLineHit())
                    onlyHitOnce = false;
                sleep(50);
            }
            // Stops the motors
            leftMotor.stop(true);
            rightMotor.stop();
        
            // Find out our angle using the difference of the lines.
            angleY = angleAtLines[2] - angleAtLines[0]; // Lines 0 and 2 are the vertical lines
            angleX = angleAtLines[3] - angleAtLines[1]; // Lines 1 and 3 are the horizontal lines
        
            // Get dx and dy
            currentX = currentLocation(angleY);
            currentY = currentLocation(angleX);
        
            sleep(1000);
        
            // Go to origin
            odometer.setXYT(currentX, currentY, odometer.getXYT()[2]); // updates odo with current location compared to
            // origin
            Navigation.travelTo(xCoord, yCoord); // make it travel to origin
            odometer.setXYT(xCoord, yCoord, odometer.getXYT()[2]);
    }
    
    /**
     * cleaner way of sleeping threads
     * @param timeMs: time wished to sleep
     */
    private static void sleep(int timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (Exception ignored) {
        }
        
    }
    
}
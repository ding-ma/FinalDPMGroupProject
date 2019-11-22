package ca.mcgill.ecse211;

import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

public class Localization {
    static double[] angleAtLines = new double[4];
    
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
        leftMotor.setSpeed(ROTATE_SPEED);
        rightMotor.setSpeed(ROTATE_SPEED);
        
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
        //      Navigation.turnTo(-3);
        odometer.setXYT(0.0, 0.0, 0.0);
        
    }
    
    public static void travelUntilLineHit(int turnAngle) {
        // Face origin
        Navigation.turnTo(turnAngle);
        leftMotor.setSpeed(MOTOR_LOW);
        rightMotor.setSpeed(MOTOR_LOW);
        while (true) {
    
    
            leftMotor.forward();
            rightMotor.forward();
            // Move backwards to put the rotation point on the line
            if (SensorsPoller.getCurrentLightIntensity() < 0.3) { // Compare to previous intensity
                leftMotor.stop(true);
                rightMotor.stop(false);
                sleep(100);
                leftMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), true);
                rightMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), false);
        
                Sound.beep();
                break;
            }
        }
     

    }
    
    public static void centralizeAtPoint(double xCoord, double yCoord){
        int currLineDetected = 0;// Count how many lines we've detected this far.
        double currentX;
        double currentY;
        double angleX;
        double angleY;
        leftMotor.setSpeed(MOTOR_LOW);
        rightMotor.setSpeed(MOTOR_LOW);
        while (currLineDetected < 4) {
            // Rotate in place to find the next line.
            leftMotor.backward();
            rightMotor.forward();
            if (SensorsPoller.getCurrentLightIntensity() < 0.3) { //Compare intensities
                angleAtLines[currLineDetected] = odometer.getXYT()[2];
                currLineDetected++;
                Sound.beep();
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
        if(!(angleX < 3 && angleY <3)) {
          Navigation.travelTo(xCoord, yCoord); // make it travel to origin
        }
      
    
        // Turn to face 0 degrees
        double currAngle = odometer.getXYT()[2];
//        if (currAngle <= 360 && currAngle >= 2.0) {
//            Navigation.turnTo(-20); // robot always under turns to right
//        }
        
        odometer.setXYT(xCoord, yCoord, odometer.getXYT()[2]);
        Navigation.turnTo(0);

//        //todo, fix ressources for team number with wifi server
//        if (Resources.greenTeam == 15) {
//            odometer.setXYT(8 * TILE_SIZE, 1 * TILE_SIZE, 270);
//        }
//    
//        if (Resources.greenTeam != 15) {
//            odometer.setXYT(1 * TILE_SIZE, 14 * TILE_SIZE, 90);
//        }
      
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
package ca.mcgill.ecse211;

import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Tunnel Navigation methods in order to get the robot at the entrance of the tunnel and the correction inside the tunnel.
 */
public class TunnelNavigation {
    
    /**
     * Travels to the entrance of the tunnel
     */
    public static void entranceOfTunnel() {
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        
        // ACTUAL FUNCTION, REST IS TESTING
        // Moves to centre of 1 block before entrance of tunnel and turns toward tunnel
        
        // if (greenTeam == 15) {
        // move horizontally first
        // Navigation.travelTo((tng.ll.x + 1)*TILE_SIZE, currentY);
        // Navigation.travelTo((tng.ll.x + 1)*TILE_SIZE, (tng.ll.y-1)*TILE_SIZE);
        // Localization.centralizeAtPoint((tng.ll.x + 1)*TILE_SIZE, (tng.ll.y-1)*TILE_SIZE);
        // Navigation.travelTo((tng.ll.x + 0.5)*TILE_SIZE, (tng.ll.y-1)*TILE_SIZE);
        // Navigation.turnTo(0);
        // }
        // else {
        // //move vertically first
        // Navigation.travelTo(currentX, (tnr.ll.y + 1)*TILE_SIZE)
        // Navigation.travelTo((tnr.ll.x-1)*TILE_SIZE, (tnr.ll.y + 1)*TILE_SIZE); //
        // Localization.centralizeAtPoint((tnr.ll.x - 1)*TILE_SIZE, (tnr.ll.y + 1)*TILE_SIZE);
        // Navigation.travelTo((tnr.ll.x - 1)*TILE_SIZE, (tnr.ll.y + 0.5)*TILE_SIZE);
        // Navigation.turnTo(90);
        // }
        
        // Testing
        Navigation.travelTo((6) * TILE_SIZE, (4) * TILE_SIZE); // should be dependent on the tile and tunnel location
        Localization.travelUntilLineHit(45);// this depends on the team we are on. 45 for testing purposes.
        Localization.centralizeAtPoint((6) * TILE_SIZE, (4) * TILE_SIZE);
    }
    
    // todo, need to add doging algorithm
    
    /**
     * Travelling through tunnel with the help of the sweaping sensor
     */
    public static void goThroughTunnel() {
        /*
         * sweap sensor, and get the angle of the sensor. if something is found, dodge accordinly
         */
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        
        // if (greenTeam == 15) {
        // move horizontally first
        // Navigation.travelTo((tng.ll.x+0.5)*TILE_SIZE, currentY);
        // Navigation.travelTo((tng.ll.x+0.5)*TILE_SIZE, (tng.ur.y+0.5)*TILE_SIZE);
        // }
        // else {
        // //move vertically first
        // Navigation.travelTo(currentX, (tnr.ll.y+0.5)*TILE_SIZE);
        // Navigation.travelTo((tnr.ur.x+0.5)*TILE_SIZE, (tnr.ll.y+0.5)*TILE_SIZE);
        // }
        
        //Testing
//        Navigation.turnTo(90);
//        leftMotor.rotate(Navigation.convertDistance(0.5 * Resources.TILE_SIZE), true);
//        rightMotor.rotate(Navigation.convertDistance(0.5 * Resources.TILE_SIZE), false);
//        
//        Navigation.turnTo(0);
        //this should get the robot positioned in the centerwe
//        leftMotor.rotate(Navigation.convertDistance(0.5 * Resources.TILE_SIZE), true);
//        rightMotor.rotate(Navigation.convertDistance(0.5 * Resources.TILE_SIZE), false);


//     Navigation.turnTo(0);
        
        // check if tunnelURx > tunnelLLx (we can calculate the distance to move forward by the cordinates of the tunnel.
        // we also need to go fast through the tunnel to decrease drift over distance.
        //
        leftMotor.stop(true);
        rightMotor.stop(false);
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
        
        // leftMotor.rotate(Navigation.convertDistance(1.2*Resources.TILE_SIZE), true); //this should get the robot
        // positioned in the centerwe
        // rightMotor.rotate(Navigation.convertDistance(1.2*Resources.TILE_SIZE), false);
        //
        
        
        ultraSonicMotor.rotate(-60);
        ultraSonicMotor.rotate(120);
        ultraSonicMotor.rotate(-120);
        
        
        double[] distTheta = new double[2];
//    while (true) {
//
//
//      // Option 2 for tunnel nav
//      /*
//       * leftMotor.forward(); rightMotor.forward(); System.out.println(SensorsPoller.getCurrentDistance());
//       * 
//       * if(SensorsPoller.getCurrentDistance() < 33) { System.out.println("Here=" + SensorsPoller.getCurrentDistance());
//       * leftMotor.setSpeed(200); rightMotor.setSpeed(206); leftMotor.backward(); rightMotor.forward(); }else {
//       * leftMotor.setSpeed(rightMotor.getSpeed()); leftMotor.forward(); rightMotor.forward(); }
//       */
//
//      // Option 1 sweeping sensor implementation
//      leftMotor.setSpeed(50);
//      rightMotor.setSpeed(50);
//      leftMotor.forward();
//      rightMotor.forward();
//
//      ultraSonicMotor.rotate(120); // taken at right
//
//      distTheta[0] = SensorsPoller.getCurrentDistance();
//      System.out.println("right:" + distTheta[0]);
//
//      ultraSonicMotor.rotate(-120); // taken at the left
//
//
//      distTheta[1] = SensorsPoller.getCurrentDistance();
//      System.out.println("left:" + distTheta[1]);
//
//
//      // if distance read on right is > distance read on left
//      // when US reads left and right distance = 32, robot is close to left tunnel wall
//      if ((distTheta[0] > distTheta[1]) || ((distTheta[1] < 33) && (distTheta[0] < 33))) {
//
//        leftMotor.setSpeed(120);
//        rightMotor.setSpeed(50);
//        leftMotor.forward();
//        rightMotor.backward();
//        try {
//          Thread.sleep(250);
//        } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//      } else if ((distTheta[0] < distTheta[1]) || ((distTheta[0] < 33) && (distTheta[1] > 32))) {
//        rightMotor.setSpeed(110);
//        leftMotor.setSpeed(50);
//        leftMotor.backward();
//        rightMotor.forward();
//        try {
//          Thread.sleep(250);
//        } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//      } else {
//        leftMotor.setSpeed(50);
//        rightMotor.setSpeed(50);
//        leftMotor.forward();
//        rightMotor.forward();
//        try {
//          Thread.sleep(250);
//        } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//        }
//      }
//      // if(odometer.getXYT()[1] > (4 + 1) * Resources.TILE_SIZE ) { //get to a good position away from the tunnel
//      // leftMotor.stop(true);
//      // rightMotor.stop(false);
//      // Navigation.travelTo(3*Resources.TILE_SIZE,(5) * Resources.TILE_SIZE ); //change to coordinates tunnel
//      // break;
//      // }
//    }
       int counter = 0; 
        //Third alternative yet to be tested
        while (!SensorsPoller.getIsLineHit() && counter != 2) {
            
            leftMotor.rotate(Navigation.convertDistance(0.25 * Resources.TILE_SIZE), true); //always drive forward half a tile
            rightMotor.rotate(Navigation.convertDistance(0.25 * Resources.TILE_SIZE), false);
            
            leftMotor.stop(true);
            rightMotor.stop();
            
            ultraSonicMotor.rotate(120); // distance taken at right
            
            distTheta[0] = SensorsPoller.getCurrentDistance();
            System.out.println("right:" + distTheta[0]);
            
            ultraSonicMotor.rotate(-120); // distance taken at the left
            
            
            distTheta[1] = SensorsPoller.getCurrentDistance();
            System.out.println("left:" + distTheta[1]);
            
            if (SensorsPoller.getIsLineHit() && counter != 2) { //base case to exit the tunnel sweeping mode
//         you can reset the speed of motors back to normal here since we are out of the tunnel
              counter++;
            }else if(SensorsPoller.getIsLineHit() && counter == 2){
              break;
            }
            
            if(distTheta[0] > 99 && distTheta[1] < 50) {    
              leftMotor.stop(true);
              rightMotor.rotate(Navigation.convertAngle(15), true);
            }
            
            if ((distTheta[0] > distTheta[1]) || ((distTheta[1] < 33) && (distTheta[0] < 33))) { //close to left wall
                
                leftMotor.rotate(Navigation.convertAngle(15), true); // check this value
                rightMotor.rotate(-Navigation.convertAngle(15), false);
                
                
            } else if ((distTheta[0] < distTheta[1]) || ((distTheta[0] < 33) && (distTheta[1] > 32))) {//close to right wall
                leftMotor.rotate(-Navigation.convertAngle(15), false); // check this value
                rightMotor.rotate(Navigation.convertAngle(15), true);
                
            } else {
                continue; // can be refactored but basically go forward. Speed can be reset here as well.
                
            }
            
        }
        
    }
    
    /**
     * Travel to the shooting Point in order to fire the ping-pong ball
     */
    public static void shootingPoint() {
        Point ourBin;
        if (greenTeam == 15) {
            ourBin = greenBin;
        } else {
            ourBin = redBin;
        }
        
        Navigation.turnToXY(ourBin.x, ourBin.y);
        Sound.beep();
        
        double[] currLocation = odometer.getXYT();
        
        if (Navigation.euclideanDistance(currLocation[0], currLocation[1], ourBin.x, ourBin.y) < SHOOTING_DISTANCE) {
            Navigation.turnTo(currLocation[2] + 180);
        }
        
        while ((Math.abs(Navigation.euclideanDistance(currLocation[0], currLocation[1], ourBin.x, ourBin.y) - SHOOTING_DISTANCE) < SHOOTING_ERROR_ALLOWED)) {
            rightMotor.forward();
            leftMotor.forward();
            
            if (SensorsPoller.getCurrentDistance() < 15) {
                if (Navigation.canAvoidRight()) {
                    Navigation.avoidRight();
                } else {
                    Navigation.avoidLeft();
                }
            }
            currLocation = odometer.getXYT();
            
            Navigation.turnToXY(ourBin.x, ourBin.y);
            if (Navigation.euclideanDistance(currLocation[0], currLocation[1], ourBin.x, ourBin.y) < SHOOTING_DISTANCE) {
                Navigation.turnTo(currLocation[2] + 180);
            }
        }
        
        firePingPongBall();
    }
    
    /**
     * Triggers the touch sensor for the top brick in order to fire the ping-pong ball
     */
    public static void firePingPongBall() {
        // motor rotate so it touches the sensor
        triggerMotor.rotate(50);
        triggerMotor.rotate(-50);
        
        // makes the robot move super slowly during launch, so we have a better chance of hitting the target
        leftMotor.setSpeed(50);
        rightMotor.setSpeed(50);
        leftMotor.rotate(Navigation.convertDistance(10), true);
        rightMotor.rotate(Navigation.convertDistance(10), false);
    }
}

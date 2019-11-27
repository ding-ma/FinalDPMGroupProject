package ca.mcgill.ecse211;

import lejos.hardware.Sound;
import static ca.mcgill.ecse211.Resources.*;

/**
 * Tunnel Navigation methods in order to get the robot at the entrance of the tunnel and the correction inside the
 * tunnel.
 */
public class TunnelNavigation {

  static int[] localizeParams = WifiParser.getLocalizeStartingPoint();

  /**
   * Travels to the entrance of the tunnel
   */
  public static void entranceOfTunnel(double[] tunnellocations) {
    double currentX = odometer.getXYT()[0];
    double currentY = odometer.getXYT()[1];


    // Moves to centre of 1 block before entrance of tunnel and turns toward tunnel
    Navigation.travelTo(tunnellocations[0] * TILE_SIZE, tunnellocations[1] * TILE_SIZE);
    Localization.centralizeAtPoint(tunnellocations[0] * TILE_SIZE, tunnellocations[1] * TILE_SIZE);
    Navigation.turnTo(tunnellocations[4]);

  }


  /**
   * Travelling through tunnel with the help of the sweaping sensor
   */
  public static void goThroughTunnel(double[] tunnellocations) {
    /*
     * sweep sensor, and get the angle of the sensor. if something is found, dodge accordingly
     */
    double currentX = odometer.getXYT()[0];
    double currentY = odometer.getXYT()[1];

    leftMotor.setSpeed(100); // move slowly to make the turns
    rightMotor.setSpeed(100);
    int angleA = 0;
    int angleB = 0;
    
    if (tunnellocations[4] == 0 && tunnellocations[0] > WifiParser.ourTunnel.ll.x) { // applies for corner 0,1 vertical
                                                                                     // tunnel
      // turn 270, move half a tile, turn to 0 and move .75 a tile
      angleA = 270;
      angleB = 0;
    } else {
      // turn 90, move half a tile, turn to 0, move .75 a tile
    	if (tunnellocations[4] == 0) {
      angleA = 90;
      angleB = 0;
    	}
    }
    
    if (tunnellocations[4] == 90 && tunnellocations[1] > WifiParser.ourTunnel.ll.y) { // applies for corner 0,3
                                                                                     // horizontal tunnel
      // turn to 180, move half a tile, turn to 90, move .75 a tile
      angleA = 180;
      angleB = 90;

    } else {
    	if (tunnellocations[4] == 90) {
      // turn to 0, move half a tile, turn to 90, move .75 a tile
      angleA = 0;
      angleB = 90;
    	}
    }

    if (tunnellocations[4] == 180 && tunnellocations[0] < WifiParser.ourTunnel.ur.x) { // applies for corner 2,3
                                                                                       // vertical tunnel
      // turn to 90, move half a tile, turn 180, move .75 a tile
    	angleA = 90;
      angleB = 180;
      
    } else {
    	if (tunnellocations[4] == 180) {

      // turn to 270, move half a tile, turn 180, move .75 a tile
      angleA = 270;
      angleB = 180;}
    }

    if (tunnellocations[4] == 270 && tunnellocations[1] < WifiParser.ourTunnel.ur.y) { // applies for corner 1,2
                                                                                       // horizontal tunnel
      // turn to 0, move half a tile, turn 270, move .75 a tile
      angleA = 0;
      angleB = 270;
    } else {
      // turn to 180, move half a tile, turn 270, move .75 a tile
    	if (tunnellocations[4] == 270) {

    	angleA = 180;
      angleB = 270;
    	}
    }
    
    System.out.println("A"+angleA);
    System.out.println("B"+angleB);

    Navigation.turnTo(angleA); // angle to turn based on your orientation
    leftMotor.rotate(Navigation.convertDistance(0.5 * Resources.TILE_SIZE), true);
    rightMotor.rotate(Navigation.convertDistance(0.5 * Resources.TILE_SIZE), false);
    Navigation.turnTo(angleB);
    leftMotor.rotate(Navigation.convertDistance(0.75 * Resources.TILE_SIZE), true); // this should get our head inside
                                                                                    // the tunnel
    rightMotor.rotate(Navigation.convertDistance(0.75 * Resources.TILE_SIZE), false);

    leftMotor.stop(true);
    rightMotor.stop(false);
    leftMotor.setSpeed(200); // we might need to go fast through the tunnel to decrease drift over distance.
    rightMotor.setSpeed(200);

    ultraSonicMotor.rotate(-60);
    ultraSonicMotor.rotate(120);
    ultraSonicMotor.rotate(-120);


    double[] distTheta = new double[2];


    while (true) { // we MUST make sure our head is in the tunnel before getting here.
      leftMotor.rotate(Navigation.convertDistance(0.25 * Resources.TILE_SIZE), true); // always drive forward half a
                                                                                      // tile
      rightMotor.rotate(Navigation.convertDistance(0.25 * Resources.TILE_SIZE), false);

      leftMotor.stop(true);
      rightMotor.stop();

      ultraSonicMotor.rotate(120); // distance taken at right

      distTheta[0] = SensorsPoller.getCurrentDistance();
      System.out.println("right:" + distTheta[0]);

      ultraSonicMotor.rotate(-120); // distance taken at the left

      distTheta[1] = SensorsPoller.getCurrentDistance();
      System.out.println("left:" + distTheta[1]);

      if (distTheta[0] > 99 && distTheta[1] > 99) { // base case
        leftMotor.rotate(Navigation.convertDistance(0.7 * Resources.TILE_SIZE), true); // accounts for light sensor
                                                                                       // reading exit line
        rightMotor.rotate(Navigation.convertDistance(0.7 * Resources.TILE_SIZE), false);
        break;
      }

      else if ((distTheta[0] > distTheta[1]) || ((distTheta[1] < 33) && (distTheta[0] < 33))) { // close to left wall

        leftMotor.rotate(Navigation.convertAngle(10), true); // check this value
        rightMotor.rotate(-Navigation.convertAngle(10), false);


      } else if ((distTheta[0] < distTheta[1]) || ((distTheta[0] < 33) && (distTheta[1] > 32))) {// close to right wall
        leftMotor.rotate(-Navigation.convertAngle(10), false); // check this value
        rightMotor.rotate(Navigation.convertAngle(10), true);

      } else {
        continue; // can be refactored but basically go forward. Speed can be reset here as well.
      }

    }

    while (!SensorsPoller.getIsLineHit()) {
      leftMotor.forward();
      rightMotor.forward();
    }

    leftMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), true);
    rightMotor.rotate(Navigation.convertDistance(-SENSOR_LOCATION), false);

    Navigation.travelTo(tunnellocations[2] * TILE_SIZE, tunnellocations[3] * TILE_SIZE);

    Localization.centralizeAtPoint(tunnellocations[2] * TILE_SIZE, tunnellocations[3] * TILE_SIZE);
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
//    Sound.beep();

    double[] currLocation = odometer.getXYT();

    if (Navigation.euclideanDistance(currLocation[0], currLocation[1], ourBin.x, ourBin.y) < SHOOTING_DISTANCE) {
      Navigation.turnTo(currLocation[2] + 180);
    }

    while ((Math.abs(Navigation.euclideanDistance(currLocation[0], currLocation[1], ourBin.x, ourBin.y)
        - SHOOTING_DISTANCE) < SHOOTING_ERROR_ALLOWED)) {
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
    for (int i = 0; i < 3; i++) { //need to beep 3 times when we reach our launch point
      Sound.beep();
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

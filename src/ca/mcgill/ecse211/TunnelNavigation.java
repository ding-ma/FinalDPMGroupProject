package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;

public class TunnelNavigation {


  public static void entranceOfTunnel() {
    double currentX = odometer.getXYT()[0];
    double currentY = odometer.getXYT()[1];

    // if (greenTeam == 15) {
    // Navigation.travelTo((4-1)*30.48, currentY);
    // Navigation.travelTo((3) * 30.48, (3 - 1) * 30.48);
    // move horizontally first
    // Navigation.travelTo((tng.ll.x-1)*30.48, currentY);
    // Navigation.travelTo((tng.ll.x-1)*30.48, (tng.ll.y-1)*30.48);
    // }
    // else {
    // //move vertically first
    // Navigation.travelTo(currentX, (tnr.ll.y)*30.48)
    // Navigation.travelTo((tnr.ll.x-1)*30.48, (tnr.ll.y)*30.48)
    // }

    // Test coordinates below
    Navigation.travelTo((6) * 30.48, (4 - 1) * 30.48); // should be dependent on the tile and tunnel location
    Localization.centralizeAtPoint((6) * 30.48, (4 - 1) * 30.48);

  }

  public static void exitOfTunnel(double xTopRight, double yTopRight) {

  }

  // todo, need to add doging algorithm
  public static void goThroughTunnel() {
    /*
     * sweap sensor, and get the angle of the sensor. if something is found, dodge accordinly
     */
    double currentX = odometer.getXYT()[0];
    double currentY = odometer.getXYT()[1];

    // if (greenTeam == 15) {
    // move horizontally first
    // entrance of tunnel
    // Navigation.travelTo((3 + 0.5) * 30.48, currentY);
    // through tunnel
    // Navigation.travelTo((3 + 0.5) * 30.48, (5 + 0.5) * 30.48);
    // Navigation.travelTo((tng.ll.x+0.5)*30.48, currentY);
    // Navigation.travelTo((tng.ll.x+0.5)*30.48, (tng.ur.y+0.5)*30.48);
    // }
    // else {
    // //move vertically first
    // Navigation.travelTo(currentX, (tnr.ll.y+0.5)*30.48)
    // Navigation.travelTo((tnr.ur.x+0.5)*30.48, (tnr.ll.y+0.5)*30.48)
    // }


     Navigation.turnTo(90); 
     leftMotor.rotate(Navigation.convertDistance(0.5*Resources.TILE_SIZE), true); 
     //this should get the robot positioned in the centerwe
     rightMotor.rotate(Navigation.convertDistance(0.5*Resources.TILE_SIZE), false);
     
     
//     Navigation.turnTo(0);

    // check if tunnelURx > tunnelLLx (we can calculate the distance to move forward by the cordinates of the tunnel.
    // we also need to go fast through the tunnel to decrease drift over distance.
    //
    leftMotor.stop(true);
    rightMotor.stop(false);
    leftMotor.setSpeed(100);
    rightMotor.setSpeed(100);

    // leftMotor.rotate(Navigation.convertDistance(1.2*Resources.TILE_SIZE), true); //this should get the robot
    // positioned in the centerwe
    // rightMotor.rotate(Navigation.convertDistance(1.2*Resources.TILE_SIZE), false);
    //


    ultraSonicMotor.rotate(-60);
    ultraSonicMotor.rotate(120);
    ultraSonicMotor.rotate(-120);


    double[] distTheta = new double[2];
    while (true) {


      // Option 2 for tunnel nav
      /*
       * leftMotor.forward(); rightMotor.forward(); System.out.println(SensorsPoller.getCurrentDistance());
       * 
       * if(SensorsPoller.getCurrentDistance() < 33) { System.out.println("Here=" + SensorsPoller.getCurrentDistance());
       * leftMotor.setSpeed(200); rightMotor.setSpeed(206); leftMotor.backward(); rightMotor.forward(); }else {
       * leftMotor.setSpeed(rightMotor.getSpeed()); leftMotor.forward(); rightMotor.forward(); }
       */

      // Option 1 sweeping sensor implementation
      leftMotor.setSpeed(50);
      rightMotor.setSpeed(50);
      leftMotor.forward();
      rightMotor.forward();

      ultraSonicMotor.rotate(120); // taken at right

      distTheta[0] = SensorsPoller.getCurrentDistance();
      System.out.println("right:" + distTheta[0]);

      ultraSonicMotor.rotate(-120); // taken at the left


      distTheta[1] = SensorsPoller.getCurrentDistance();
      System.out.println("left:" + distTheta[1]);


      // if distance read on right is > distance read on left
      // when US reads left and right distance = 32, robot is close to left tunnel wall
      if ((distTheta[0] > distTheta[1]) || ((distTheta[1] < 33) && (distTheta[0] < 33))) {

        leftMotor.setSpeed(120);
        rightMotor.setSpeed(50);
        leftMotor.forward();
        rightMotor.backward();
        try {
          Thread.sleep(250);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else if ((distTheta[0] < distTheta[1]) || ((distTheta[0] < 33) && (distTheta[1] > 32))) {
        rightMotor.setSpeed(110);
        leftMotor.setSpeed(50);
        leftMotor.backward();
        rightMotor.forward();
        try {
          Thread.sleep(250);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      } else {
        leftMotor.setSpeed(50);
        rightMotor.setSpeed(50);
        leftMotor.forward();
        rightMotor.forward();
        try {
          Thread.sleep(250);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      // if(odometer.getXYT()[1] > (4 + 1) * Resources.TILE_SIZE ) { //get to a good position away from the tunnel
      // leftMotor.stop(true);
      // rightMotor.stop(false);
      // Navigation.travelTo(3*Resources.TILE_SIZE,(5) * Resources.TILE_SIZE ); //change to coordinates tunnel
      // break;
      // }
    }

  }

  public static void shootingPoint() {
    Navigation.travelTo(2, 7);
    // Rotate to angle
    Navigation.turnTo(45);
    // Navigation.travelTo(bin.x, bin.y);
    // //Rotate to angle
    // Navigation.turnTo(tnr.ll.x);
  }

  public static void firePingPongBall() {
    // motor rotate so it touches the sensor
    triggerMotor.rotate(50);
    triggerMotor.rotate(-50);
  }
}

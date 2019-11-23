package ca.mcgill.ecse211;

import lejos.hardware.Button;
import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

public class Main implements Runnable{
    public static void main(String[] args) {
        int buttonChoice;
        do {
            //Clear the display
            LCD.clear();
            //Prompt the user for travel and shoot, and just shoot
            LCD.drawString("< Left | Right >", 0, 0);
            LCD.drawString(" Travel| Stand  ", 0, 1);
            LCD.drawString(" to    | and    ", 0, 2);
            LCD.drawString(" and   | Shoot  ", 0, 3);
            LCD.drawString(" Shoot |        ", 0, 4);
    
            //  buttonChoice = Button.waitForAnyPress();//Wait for the user to decide
           buttonChoice = Button.ID_LEFT; //quick hack to work around this, starts faster
        } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
        
        
        if (buttonChoice == Button.ID_LEFT) { //navigate then shoot
            new Thread(odometer).start(); // start odo thread
            new Thread(new SensorsPoller()).start();
            new Thread(new Display()).start(); //start display thread
            new Thread(new Main()).start();
        }
        if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
            System.exit(0);
        }
    }
    
    @Override
    public void run() {
        //US localization
    	Localization.fallingEdge();

        // Rotates to point, moves there, light localizes
        if (greenTeam == 15) {
            Localization.travelUntilLineHit(315);
            Localization.centralizeAtPoint(TILE_SIZE * 7), TILE_SIZE * 1);
            odometer.setXYT(7 * TILE_SIZE, 1 * TILE_SIZE, 270);
        } else {
            Localization.travelUntilLineHit(135);
            Localization.centralizeAtPoint(TILE_SIZE * 1), TILE_SIZE * 7);
            odometer.setXYT(7 * TILE_SIZE, 1 * TILE_SIZE, 90);
        }

        // Moves to entrance of tunnel
        TunnelNavigation.entranceOfTunnel();

        TunnelNavigation.goThroughTunnel();

        // If we want to shoot
        // TunnelNavigation.shootingPoint();

        // If we don't want to shoot
        Navigation.turnTo(odometer.getXYT()[2] + 180);
        TunnelNavigation.goThroughTunnel();
//    	
    	
    	
    	
    	
    	
    	
//      odometer.setXYT(30.48, 30.48,0);
//      Navigation.travelTo(1* Resources.TILE_SIZE, 13*Resources.TILE_SIZE);
//      leftMotor.stop(true);
//      rightMotor.stop();
//      leftMotor.flt();
//      rightMotor.flt();
//      System.out.println("Gyro Angle = " + SensorsPoller.getCurrentAngle());
//      System.out.println("Odometer= " + odometer.getXYT()[2]);
//     System.out.println(Arrays.toString(odometer.getXYT())); 
//      try {
//    Thread.sleep(1000);
//} catch (InterruptedException e) {
//    // TODO Auto-generated catch block
//    e.printStackTrace();
//}
//      Navigation.travelTo(1* Resources.TILE_SIZE, 7*Resources.TILE_SIZE);
//      leftMotor.stop(true);
//      rightMotor.stop();
//      leftMotor.flt();
//      rightMotor.flt();
//      System.out.println("Gyro Angle = " + SensorsPoller.getCurrentAngle());
//      System.out.println("Odometer= " + odometer.getXYT()[2]);
//     System.out.println(Arrays.toString(odometer.getXYT())); 
//      try {
//    Thread.sleep(1000);
//} catch (InterruptedException e) {
//    // TODO Auto-generated catch block
//    e.printStackTrace();
//}
//      Navigation.travelTo(1* Resources.TILE_SIZE, 10*Resources.TILE_SIZE);
//      leftMotor.stop(true);
//      rightMotor.stop();
//      leftMotor.flt();
//      rightMotor.flt();
//      System.out.println("Gyro Angle = " + SensorsPoller.getCurrentAngle());
//      System.out.println("Odometer= " + odometer.getXYT()[2]);
//     System.out.println(Arrays.toString(odometer.getXYT())); 
//      try {
//    Thread.sleep(1000);
//} catch (InterruptedException e) {
//    // TODO Auto-generated catch block
//    e.printStackTrace();
//}
//      Navigation.travelTo(1* Resources.TILE_SIZE, 13*Resources.TILE_SIZE);
//      leftMotor.stop(true);
//      rightMotor.stop();
//      leftMotor.flt();
//      rightMotor.flt();
//      System.out.println("Gyro Angle = " + SensorsPoller.getCurrentAngle());
//      System.out.println("Odometer= " + odometer.getXYT()[2]);
//     System.out.println(Arrays.toString(odometer.getXYT())); 
//      try {
//    Thread.sleep(1000);
//} catch (InterruptedException e) {
//    // TODO Auto-generated catch block
//    e.printStackTrace();
//}
//      Navigation.travelTo(1* Resources.TILE_SIZE, 14*Resources.TILE_SIZE);
//      leftMotor.stop(true);
//      rightMotor.stop();
//      leftMotor.flt();
//      rightMotor.flt();
//      System.out.println("Gyro Angle = " + SensorsPoller.getCurrentAngle());
//      System.out.println("Odometer= " + odometer.getXYT()[2]);
//     System.out.println(Arrays.toString(odometer.getXYT())); 
//      try {
//    Thread.sleep(1000);
//} catch (InterruptedException e) {
//    // TODO Auto-generated catch block
//    e.printStackTrace();
//}
//      System.out.println("Gyro Angle = " + SensorsPoller.getCurrentAngle());
//      System.out.println("Odometer= " + odometer.getXYT()[2]);
//     System.out.println(Arrays.toString(odometer.getXYT())); 
        //      Localization.fallingEdge();
       /***
        * TURN TO  
        */
//      for(int i=1; i<10000000;i++) {
//        Navigation.turnTo(90*i);
//      }
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Localization.travelUntilLineHit(45);
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Localization.centralizeAtPoint(1 * Resources.TILE_SIZE,1 * Resources.TILE_SIZE);
//      try {
//      Thread.sleep(500);
//      } catch (InterruptedException e) {
//          // TODO Auto-generated catch block
//          e.printStackTrace();
//      }
//        odometer.setXYT(1 * Resources.TILE_SIZE, 1 * Resources.TILE_SIZE, 0);
//
////
//        TunnelNavigation.entranceOfTunnel();
////////        Sound.beep();
////        Navigation.turnTo(0);   //by this time robot is at appropriate distance to tunnel ready to localize again.
//
////        Localization.travelUntilLineHit(45);
//        Localization.centralizeAtPoint(7*Resources.TILE_SIZE, 3*Resources.TILE_SIZE); //this should be changed to coordinates before the tunnel
////        try {
////        Thread.sleep(500);
////        } catch (InterruptedException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        
////        Navigation.turnTo(-35); //it seemed to under turn after localization so this fixes it.
//        odometer.setTheta(0);   //should be facing zero by this point.
////     
 //     TunnelNavigation.goThroughTunnel();
//      Localization.centralizeAtPoint(3*Resources.TILE_SIZE, (4+1)*Resources.TILE_SIZE); //it has exited the tunnel and is at the nearest safe zone
//      
//        
        
//        Sound.beep();
//        odometer.setXYT(4*Resources.TILE_SIZE, 2*Resources.TILE_SIZE, 0);
//        
//        TunnelNavigation.goThroughTunnel();
//        TunnelNavigation.shootingPoint();
//        TunnelNavigation.firePingPongBall();
//        Navigation.travelTo(3 * TILE_SIZE, 3 * TILE_SIZE); // this should be the lower left of the tunnel (-1,-1)
//        Localization.centralizeAtPoint(3 * TILE_SIZE, 3 * TILE_SIZE);
        
//        TunnelNavigation.entranceOfTunnel(5,3,true);
//        //maybe localize before?
//        TunnelNavigation.insideTunnel(true);
//        TunnelNavigation.shootingPoint(15,9);
//        TunnelNavigation.firePingPongBall();
//        TunnelNavigation.exitOfTunnel(5,5);
//        //maybe localize before?
//        TunnelNavigation.insideTunnel(false);
//        //once u got our of the tunnel, u can now go back to starting point
//        Navigation.travelTo(0, 0);
        
    }
}


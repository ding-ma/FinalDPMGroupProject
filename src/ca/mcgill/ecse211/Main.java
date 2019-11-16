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
            //buttonChoice = Button.waitForAnyPress();//Wait for the user to decide
            buttonChoice = Button.ID_LEFT; //quick hack to work around this, starts faster
        } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
        
        
        if (buttonChoice == Button.ID_LEFT) { //navigate then shoot
            new Thread(odometer).start(); // start odo thread
            new Thread(new SensorsPoller()).start();
            new Thread(new Display()).start(); //start display thread
            new Thread(new OdoCorrection()).start();
            //     new Thread(new Main()).start();
            Navigation.travelTo(1 * TILE_SIZE, 4 * TILE_SIZE);
        
        }
        if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
            System.exit(0);
        }
    }
    
    @Override
    public void run() {
        leftMotor.setSpeed(FORWARD_SPEED);
        rightMotor.setSpeed(FORWARD_SPEED);
    
        while (true) {
            leftMotor.forward();
            rightMotor.forward();
            if (SensorsPoller.getIsLineHitLeft()) {
                Sound.beep();
            }
        }
//        Localization.fallingEdge(); //gyro reset after this
//        Localization.travelUntilLineHit(45);
//        Localization.centralizeAtPoint(1 * TILE_SIZE,1 * TILE_SIZE);
//       odometer.setXYT(1 * TILE_SIZE, 1 * TILE_SIZE, 0);
//
//       Navigation.travelTo(4*TILE_SIZE, 2*TILE_SIZE);       
//       Localization.travelUntilLineHit(SensorsPoller.getCurrentAngle());
//       Localization.centralizeAtPoint(4*TILE_SIZE, 2*TILE_SIZE);
    
    
        // TunnelNavigation.entranceOfTunnel();
        //    System.out.println(SensorsPoller.getCurrentAngle());
        // SensorsPoller.resetGyro(SensorsPoller.getCurrentAngle());
        //      System.out.println(SensorsPoller.getCurrentAngle());
//        Sound.beep();
//        Localization.travelUntilLineHit(45);
//        Localization.centralizeAtPoint(3*TILE_SIZE, 2*TILE_SIZE);
//        Sound.beep();
//        odometer.setXYT(3*TILE_SIZE, 2*TILE_SIZE, 0);
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
        //once u got our of the tunnel, u can now go back to starting point
//        Navigation.travelTo(0, 0);
        
    }
}


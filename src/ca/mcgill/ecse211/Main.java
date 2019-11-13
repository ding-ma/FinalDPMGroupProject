package ca.mcgill.ecse211;

import lejos.hardware.Button;

import static ca.mcgill.ecse211.Resources.LCD;
import static ca.mcgill.ecse211.Resources.odometer;

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
        Localization.fallingEdge();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Localization.travelUntilLineHit(45);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Localization.centralizeAtPoint(0, 0);
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


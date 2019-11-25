package ca.mcgill.ecse211;

import lejos.hardware.Button;
import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Main class that drives all the code.
 * Spawns the threads and the sequence of executions
 */
public class Main implements Runnable {
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
    
    /**
     * cleaner way of sleeping threads
     *
     * @param timeMs: time wished to sleep
     */
    private static void sleep(int timeMs) {
        try {
            Thread.sleep(timeMs);
        } catch (Exception ignored) {
        }
        
    }

    @Override
    public void run() {
    
        //US localization
        Localization.fallingEdge();
        sleep(100);
    
        if (greenTeam == 15)
            SensorsPoller.resetGyro(270);
        else
            SensorsPoller.resetGyro(90);
    
        sleep(50);
    
        Localization.travelUntilLineHit(45);
    
        // Rotates to point, moves there, light localizes
        if (greenTeam == 15) {
            Localization.centralizeAtPoint(TILE_SIZE * 8, TILE_SIZE * 1);
            //  odometer.setTheta(270);
        } else {
            Localization.centralizeAtPoint(TILE_SIZE * 1, TILE_SIZE * 8);
            //   odometer.setTheta(90);
        }
    
        sleep(50);
        // Moves to entrance of tunnel
        TunnelNavigation.entranceOfTunnel();
        Sound.beepSequenceUp();
        // Goes through tunnel
        TunnelNavigation.goThroughTunnel();

//         If we want to shoot
        // TunnelNavigation.shootingPoint();
        
        // If we don't want to shoot
        if (greenTeam == 15) {
            Localization.travelUntilLineHit(45);
            Localization.centralizeAtPoint(tng.ur.x, tng.ur.y + 1);
            Navigation.travelTo(tng.ur.x - 0.5, tng.ur.y + 1);
            Navigation.turnTo(SensorsPoller.getCurrentAngle()+180);
        } else {
            Localization.travelUntilLineHit(45);
            Localization.centralizeAtPoint(tnr.ur.x + 1, tng.ur.y);
            Navigation.travelTo(tng.ur.x + 1, tng.ur.y - 0.5);
            Navigation.turnTo(270);
        }
    
        //going back home
        TunnelNavigation.goThroughTunnel();
        TunnelNavigation.entranceOfTunnel();
        sleep(100);
        Navigation.travelTo(0, 0);
        
    }
}


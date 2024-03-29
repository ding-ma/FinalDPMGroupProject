package ca.mcgill.ecse211;

import lejos.hardware.Button;
import lejos.hardware.Sound;

import static ca.mcgill.ecse211.Resources.*;

/**
 * Main class that drives all the code. Spawns the threads and the sequence of executions
 */
public class Main implements Runnable {
    public static void main(String[] args) {
        int buttonChoice;
        do {
    
            // Clear the display
            LCD.clear();
            // Prompt the user for travel and shoot, and just shoot
            LCD.drawString("Waiting...", 0, 0);
            
            // buttonChoice = Button.waitForAnyPress();//Wait for the user to decide
            buttonChoice = Button.ID_LEFT; // quick hack to work around this, starts faster
        } while (buttonChoice != Button.ID_LEFT && buttonChoice != Button.ID_RIGHT);
        
        
        if (buttonChoice == Button.ID_LEFT) { // navigate then shoot
            new Thread(odometer).start(); // start odo thread
            new Thread(new SensorsPoller()).start();
            new Thread(new Display()).start(); // start display thread
            new Thread(new Main()).start();
        }
        if (Button.waitForAnyPress() == Button.ID_ESCAPE) {
            System.exit(0);
        }
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
    
    @Override
    public void run() {
        int[] startingPoint = WifiParser.getLocalizeStartingPoint();
        double[] tunnelPoints = WifiParser.tunnelLocalizationPoints();
        
        Localization.fallingEdge(); // US localization
        SensorsPoller.resetGyro(startingPoint[2]); // reset gyro to localization point angle
        Localization.travelUntilLineHit(startingPoint[2] + 45);
        Localization.centralizeAtPoint(startingPoint[0] * TILE_SIZE, startingPoint[1] * TILE_SIZE);

        
        for (int i = 0; i < 3; i++) { // 3 sequence of beeps after localizing
            Sound.beep();
        }
        
        // Moves to entrance of tunnel
        TunnelNavigation.entranceOfTunnel(tunnelPoints);
    
        // Goes through tunnel and localizes at nearest point
        TunnelNavigation.goThroughTunnel(tunnelPoints);
        
        // If we want to shoot
        TunnelNavigation.shootingPoint();
        
        // If we don't want to shoot
        
        
        //.............. going back home.........................//
        
        // swap entry and exit values and change approach angle value
        
        switch ((int) tunnelPoints[4]) {
            
            case 0:
                swapEntryExit(tunnelPoints);
                tunnelPoints[4] = 180;
                break;
            case 90:
                swapEntryExit(tunnelPoints);
                tunnelPoints[4] = 270;
                break;
            case 180:
                swapEntryExit(tunnelPoints);
                tunnelPoints[4] = 0;
                break;
            case 270:
                swapEntryExit(tunnelPoints);
                tunnelPoints[4] = 90;
                break;
            
            default:
                tunnelPoints[4] = 0;
            
        }
        
        // Moves to entrance of tunnel
        TunnelNavigation.entranceOfTunnel(tunnelPoints);
        Localization.centralizeAtPoint(tunnelPoints[0] * TILE_SIZE, tunnelPoints[1] * TILE_SIZE); //these are swapped
        TunnelNavigation.goThroughTunnel(tunnelPoints); // Goes through tunnel and localizes at nearest point
        sleep(100);
        Navigation.travelTo(startingPoint[0], startingPoint[1]);
        
        for (int i = 0; i < 5; i++) { // 5 sequence of beeps to indicate return to starting area
            Sound.beep();
        }
        
        
    }
    
    /**
     * This method swaps the entry and exit tunnel coordinates in the array
     *
     * @param tunnelPoints: entries of tunnel coordinates
     */
    private void swapEntryExit(double[] tunnelPoints) {
        // swap x coordinates
        double tmpx = tunnelPoints[0];
        tunnelPoints[0] = tunnelPoints[2];
        tunnelPoints[2] = tmpx;
        
        // swap y coordinates
        double tmpy = tunnelPoints[1];
        tunnelPoints[1] = tunnelPoints[3];
        tunnelPoints[3] = tmpy;
    }
}


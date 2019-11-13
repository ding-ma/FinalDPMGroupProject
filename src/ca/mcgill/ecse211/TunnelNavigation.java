package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;

public class TunnelNavigation {
    
    /**
     * Travel to the tunnel using a right angle turn
     *
     * @param tunnelY: the x coordinate of the final destination
     * @param tunnelX: the y coordinate of the final destination
     * @param isGreenTeam: Team that the robot is on, green or red.
     */
    public static void entranceOfTunnel() {
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        
        // if (greenTeam == 15) {
            //move horizontally first
            Navigation.travelTo((tng.ll.x-1)*30.48, currentY);
            Navigation.travelTo((tng.ll.x-1)*30.48, (tng.ll.y-1)*30.48)
        // }
        // else {
        //     //move vertically first
        //     Navigation.travelTo(currentX, (tnr.ll.y)*30.48)
        //     Navigation.travelTo((tnr.ll.x-1)*30.48, (tnr.ll.y)*30.48)
        // }
    }
    
    public static void exitOfTunnel(double xTopRight, double yTopRight){
        
    }
    
    //todo, need to add doging algorithm
    public static void goThroughTunnel(){
        /*
        sweap sensor, and get the angle of the sensor.
        if something is found, dodge accordinly
         */
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        
        // if (greenTeam == 15) {
            //move horizontally first
            Navigation.travelTo((tng.ll.x+0.5)*30.48, currentY);
            Navigation.travelTo((tng.ll.x+0.5)*30.48, (tng.ur.y+0.5)*30.48)
        // }
        // else {
        //     //move vertically first          
        //     Navigation.travelTo(currentX, (tnr.ll.y+0.5)*30.48)
        //     Navigation.travelTo((tnr.ur.x+0.5)*30.48, (tnr.ll.y+0.5)*30.48)
        // }
    }
    
    public static void shootingPoint(){
        Navigation.travelTo(bin.x, bin.y);
        //Rotate to angle
        Navigation.turnTo(tnr.ll.x)
    }
    public static void firePingPongBall(){
        //motor rotate so it touches the sensor
    }
}

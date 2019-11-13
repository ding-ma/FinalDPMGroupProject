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
    public static void entranceOfTunnel(double tunnelX, double tunnelY, boolean isGreenTeam) {
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        
        if (isGreenTeam) {
            //move horizontally first
            Navigation.travelTo(tunnelX, currentY);
            Navigation.travelTo(tunnelX, tunnelY);
        }
        else {
            //move vertically first
            Navigation.travelTo(currentX, tunnelY);
            Navigation.travelTo(tunnelX, tunnelY);
        }
    }
    
    public static void exitOfTunnel(double xTopRight, double yTopRight){
    
    }
    
    //todo, need to add doging algorithm
    public static void insideTunnel(boolean isShortTunnel){
        /*
        sweap sensor, and get the angle of the sensor.
        if something is found, dodge accordinly
         */
        if (isShortTunnel) {
            rightMotor.rotate(Navigation.convertDistance(30.48), true);
            leftMotor.rotate(Navigation.convertDistance(30.48), false);
        }
        else {
            rightMotor.rotate(Navigation.convertDistance(60.96), true);
            leftMotor.rotate(Navigation.convertDistance(60.96), false);
        }
    }
    
    public static void shootingPoint(double binX, double binY){
    
    }
    public static void firePingPongBall(){
        //motor rotate so it touches the sensor
    }
}

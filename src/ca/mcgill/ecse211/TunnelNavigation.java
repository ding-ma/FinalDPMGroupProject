package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.SENSOR_LOCATION;
import static ca.mcgill.ecse211.Resources.leftMotor;
import static ca.mcgill.ecse211.Resources.odometer;
import static ca.mcgill.ecse211.Resources.rightMotor;
import static ca.mcgill.ecse211.Resources.triggerMotor;
import lejos.hardware.Sound;

public class TunnelNavigation {
    

    public static void entranceOfTunnel() {
        double currentX = odometer.getXYT()[0];
        double currentY = odometer.getXYT()[1];
        
        // if (greenTeam == 15) {
        //  Navigation.travelTo((4-1)*30.48, currentY);
//        Navigation.travelTo((3) * 30.48, (3 - 1) * 30.48);
            //move horizontally first
//            Navigation.travelTo((tng.ll.x-1)*30.48, currentY);
//            Navigation.travelTo((tng.ll.x-1)*30.48, (tng.ll.y-1)*30.48);
        // }
        // else {
        //     //move vertically first
        //     Navigation.travelTo(currentX, (tnr.ll.y)*30.48)
        //     Navigation.travelTo((tnr.ll.x-1)*30.48, (tnr.ll.y)*30.48)
        // }
        
//        Test coordinates below
        Navigation.travelTo((4) * 30.48, (3 - 1) * 30.48);
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
        //entrance of tunnel
//        Navigation.travelTo((3 + 0.5) * 30.48, currentY);
        //through tunnel
//        Navigation.travelTo((3 + 0.5) * 30.48, (5 + 0.5) * 30.48);
//            Navigation.travelTo((tng.ll.x+0.5)*30.48, currentY);
//            Navigation.travelTo((tng.ll.x+0.5)*30.48, (tng.ur.y+0.5)*30.48);
        // }
        // else {
        //     //move vertically first          
        //     Navigation.travelTo(currentX, (tnr.ll.y+0.5)*30.48)
        //     Navigation.travelTo((tnr.ur.x+0.5)*30.48, (tnr.ll.y+0.5)*30.48)
        // }
        
       
        Navigation.turnTo(90);
        leftMotor.rotate(Navigation.convertDistance(0.4*Resources.TILE_SIZE), true);
        rightMotor.rotate(Navigation.convertDistance(0.4*Resources.TILE_SIZE), false);
        Navigation.turnTo(0);
        
        //check if tunnelURx > tunnelLLx
        leftMotor.forward();
        rightMotor.forward();
    }
    
    public static void shootingPoint(){
        Navigation.travelTo(2, 7);
        //Rotate to angle
        Navigation.turnTo(45);
//        Navigation.travelTo(bin.x, bin.y);
//        //Rotate to angle
//        Navigation.turnTo(tnr.ll.x);
    }
    public static void firePingPongBall(){
        //motor rotate so it touches the sensor
        triggerMotor.rotate(50);
        triggerMotor.rotate(-50);
    }
}

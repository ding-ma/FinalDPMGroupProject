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
        Navigation.travelTo((7) * 30.48, (4 - 1) * 30.48); //should be dependent on the tile and tunnel location
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
        leftMotor.rotate(Navigation.convertDistance(0.5*Resources.TILE_SIZE), true); //this should get the robot positioned in the centerwe
        rightMotor.rotate(Navigation.convertDistance(0.5*Resources.TILE_SIZE), false);
        Navigation.turnTo(0);
        
        //check if tunnelURx > tunnelLLx (we can calculate the distance to move forward by the cordinates of the tunnel.
        //we also need to go fast through the tunnel to decrease drift over distance.
        leftMotor.stop(true);
        rightMotor.stop(false);
        leftMotor.setSpeed(200);
        rightMotor.setSpeed(200);
        
        while(true) {
          leftMotor.forward();
          rightMotor.forward();
          if(odometer.getXYT()[1] > (4 + 1) * Resources.TILE_SIZE ) { //get to a good position away from the tunnel
            leftMotor.stop(true);
            rightMotor.stop(false);
            Navigation.travelTo(3*Resources.TILE_SIZE,(5) * Resources.TILE_SIZE ); //change to coordinates tunnel
            break;
          }
        }
        
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

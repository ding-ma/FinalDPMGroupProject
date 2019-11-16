package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.leftMotor;
import static ca.mcgill.ecse211.Resources.rightMotor;

public class OdoCorrection implements Runnable {
    boolean leftSensorHit;
    boolean rightSensorHit;
    double correctionSpeed = 50;
    
    @Override
    public void run() {
        
        while (true) {
            leftSensorHit = SensorsPoller.getIsLineHitLeft();
            rightSensorHit = SensorsPoller.getIsLineHitRight();
            
            if (leftSensorHit && !rightSensorHit) { //robot shifting right
                leftMotor.stop();
                rightMotor.setSpeed(50);
                while (!rightSensorHit) {
                    rightMotor.forward();
                }
            }
            
            if (rightSensorHit && !leftSensorHit) { //shifting left
                rightMotor.stop();
                leftMotor.setSpeed(50);
                while (!leftSensorHit) {
                    leftMotor.forward();
                }
            }
        }
    }
}


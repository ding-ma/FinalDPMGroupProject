package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;
public class OdometerCorrection implements Runnable{
    private double currentSystemTime;
    private double previousSystemTime;
    @Override
    public void run() {
        previousSystemTime = System.currentTimeMillis();
        while (true){
            currentSystemTime = System.currentTimeMillis();
            if (currentSystemTime-previousSystemTime <120000) { //2 minutes
                double offset = SensorsPoller.getCurrentAngle();
                GYRO_SENSOR.getRateMode();
                GYRO_SENSOR.getAngleMode();
                previousSystemTime  = System.currentTimeMillis();
                // when returning, need to return offet + current angle, this should be done in sensorpooler class
            }
            
        }
    }
}

package ca.mcgill.ecse211;

/**
 * Poller class with getter and setters to fetch the entire robot sensor
 * Sensors that are fetched:
 * -Gyroscope
 * -Ultrasonic Sensor
 * -Light Sensor
 */

import static ca.mcgill.ecse211.Resources.*;
public class SensorsPoller implements Runnable{
    
    private float[] gyroData = new float[GYRO_SENSOR.sampleSize()];
    private float[] usData = new float[US_SENSOR.sampleSize()];
    private float[] lightData = new float[COLOR_SENSOR.sampleSize()];
    private float[] previousLight = new float[COLOR_SENSOR.sampleSize()];
    
    
    private static double currentAngle =0;
    private static int currentDistance;
    private static double currentLightIntensity;
    private static double previousLightIntensity;
    private static double lightIntensity;
    private static double angleOffSet;
    
    private static boolean isLineHit =false;
    
    /**
     * Resets the gyroscope to 0
     *
     * @param offset: Adding offset in order to compensate the reset
     */
    public static void resetGyro(double offset) {
        GYRO_SENSOR.reset();
        angleOffSet = offset;
    }
    
    /**
     * Get current Gyroscope angle
     *
     * @return: Angle of the robot
     */
    public static double getCurrentAngle() {
        return (currentAngle + angleOffSet) % 359;
    }
    
    /**
     * Get current US sensor distance
     *
     * @return: US sensor distance
     */
    public static int getCurrentDistance() {
        return currentDistance;
    }
    
    /**
     * Get current light intensity
     *
     * @return: Current light intensity
     */
    public static double getCurrentLightIntensity() {
        return currentLightIntensity;
    }
    
    /**
     * Current light Intensity
     *
     * @param lightIntensity: Brightness reading from light sensor
     */
    private void setCurrentLightIntensity(double lightIntensity) {
        currentLightIntensity = lightIntensity;
    }
    
    /**
     * Get previous light intensity
     *
     * @return: Previous light intensity
     */
    public static double getPreviousLightIntensity() {
        return previousLightIntensity;
    }
    
    /**
     * Previous light intensity
     *
     * @param prevLightIntensity: Brightness reading from light sensor
     */
    private void setPreviousLightIntensity(double prevLightIntensity) {
        previousLightIntensity = prevLightIntensity;
    }
    
    /**
     * Check if the line is hit with the differential method
     *
     * @return: True if line is hit, false otherwise
     */
    public static boolean getIsLineHit() {
        return isLineHit;
    }
    
    @Override
    public void run() {
        COLOR_SENSOR.getMode("Red").fetchSample(lightData, 0);
        setPreviousLightIntensity(lightData[0]);
        
        while (true) {
            GYRO_SENSOR.getAngleMode().fetchSample(gyroData, 0);
            US_SENSOR.getDistanceMode().fetchSample(usData, 0);
            
            setGyroData(gyroData[0]);
            setUsData((int) (usData[0] * 100));
            
            COLOR_SENSOR.getMode("Red").fetchSample(previousLight, 0);
            
            setCurrentLightIntensity(previousLight[0]);
            
            setIsLineHit(getCurrentLightIntensity(), getPreviousLightIntensity());
//    System.out.println(getCurrentAngle());
            try {
                Thread.sleep(75);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    /**
     * Set Gyroscope Angle
     * @param angle: Gyroscope Angle reading
     */
    private void setGyroData(double angle){
        double temp = angle% 359;
        if (angle < 0)
            temp = 360 + angle % 359; //todo, check if adding 359 vs 360. it was at 360
        currentAngle = temp;
    }
    
    /**
     * Set Ultrasonic distance
     * @param distance: US distance reading
     */
    private void setUsData(int distance){
        if(distance > 100)
            distance = 100;
        currentDistance = distance;
    }
    
    /**
     * Using differential, check if the line was a black line
     *
     * @param currentLightIntensity:  Current light Intensity
     * @param previousLightIntensity: Previous light intensity
     */
    private void setIsLineHit(double currentLightIntensity, double previousLightIntensity){
        double diffValue = currentLightIntensity - previousLightIntensity;
        if(diffValue<0)
            isLineHit = false;
        if(diffValue > 0.1 && !isLineHit){
            isLineHit = true;
        }
        setPreviousLightIntensity(currentLightIntensity);
        
    }
    
}
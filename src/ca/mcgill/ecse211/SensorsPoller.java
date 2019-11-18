package ca.mcgill.ecse211;

public class SensorsPoller implements Runnable{
    
    private float[] gyroData = new float[Resources.GYRO_SENSOR.sampleSize()];
    private float[] usData = new float[Resources.US_SENSOR.sampleSize()];
    private float[] lightData = new float[Resources.COLOR_SENSOR.sampleSize()];
    private float[] previousLight = new float[Resources.COLOR_SENSOR.sampleSize()];
    
    
    private static double currentAngle;
    private static int currentDistance;
    private static double currentLightIntensity;
    private static double previousLightIntensity;
    private static double lightIntensity;
    private static double angleOffSet;
    
    private static boolean isLineHit =false;
    @Override
    public void run() {
        Resources.COLOR_SENSOR.getMode("Red").fetchSample(lightData, 0);
        setPreviousLightIntensity(lightData[0]);
        
        while (true){
            Resources.GYRO_SENSOR.getAngleMode().fetchSample(gyroData, 0);
            Resources.US_SENSOR.getDistanceMode().fetchSample(usData, 0);
            
            setGyroData(gyroData[0]);
            setUsData((int)(usData[0]*100));
            
            Resources.COLOR_SENSOR.getMode("Red").fetchSample(previousLight, 0);
            
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
    
    private void setGyroData(double angle){
        double temp = angle% 359;
        if (angle < 0)
            temp = 360 + angle % 359; //todo, check if adding 359 vs 360. it was at 360
        currentAngle = temp;
    }
    
    private void setUsData(int distance){
        currentDistance = distance;
    }
    
    private void setCurrentLightIntensity(double lightIntensity){
        currentLightIntensity = lightIntensity;
    }
    private void setPreviousLightIntensity(double prevLightIntensity){
        previousLightIntensity = prevLightIntensity;
    }
    
    public static double getCurrentAngle() {
        return currentAngle;
    }
    
    public static int getCurrentDistance(){
        return currentDistance;
    }
    
    public static double getCurrentLightIntensity() {
        return currentLightIntensity;
    }
    
    public static double getPreviousLightIntensity() {
        return previousLightIntensity;
    }
    
    public void setIsLineHit(double currentLightIntensity, double previousLightIntensity){
        double diffValue = currentLightIntensity - previousLightIntensity;
        if(diffValue<0)
            isLineHit = false;
        if(diffValue > 0.1 && !isLineHit){
            isLineHit = true;
        }
        setPreviousLightIntensity(currentLightIntensity);
        
    }
    public static boolean getIsLineHit(){
        return isLineHit;
    }
    
}
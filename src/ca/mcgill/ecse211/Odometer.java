package ca.mcgill.ecse211;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static ca.mcgill.ecse211.Resources.*;

//static import to avoid duplicating variables and make the code easier to read

/**
 * The odometer class keeps track of the robot's (x, y, theta) position.
 *
 * @author Rodrigo Silva
 * @author Dirk Dubois
 * @author Derek Yu
 * @author Karim El-Baba
 * @author Michael Smith
 * @author Younes Boubekeur
 */

public class Odometer implements Runnable {
    
    /**
     * The odometer update period in ms.
     */
    private static final long ODOMETER_PERIOD = 25;
    /**
     * Fair lock for concurrent writing
     */
    private static Lock lock = new ReentrantLock(true);
    private static Odometer odo; // Returned as singleton
    // Motor-related variables
    private static int leftMotorTachoCount = 0;
    
    // Thread control tools
    private static int rightMotorTachoCount = 0;
    /**
     * The x-axis position in cm.
     */
    private volatile double x;
    /**
     * The y-axis position in cm.
     */
    private volatile double y; // y-axis position
    /**
     * The orientation in degrees.
     */
    private volatile double theta; // Head angle
    /**
     * Indicates if a thread is trying to reset any position parameters
     */
    private volatile boolean isResetting = false;
    /**
     * Lets other threads know that a reset operation is over.
     */
    private Condition doneResetting = lock.newCondition();
    private int previousLeftMotorTachoCount = 0;
    private int previousRightMotorTachoCount = 0;
    
    /**
     * This is the default constructor of this class. It initiates all motors and
     * variables once.It cannot be accessed externally.
     */
    private Odometer() {
        setXYT(TILE_SIZE, TILE_SIZE, 0);
    }
    
    /**
     * Returns the Odometer Object. Use this method to obtain an instance of
     * Odometer.
     *
     * @return the Odometer Object
     */
    public synchronized static Odometer getOdometer() {
        if (odo == null) {
            odo = new Odometer();
        }
        
        return odo;
    }
    
    /**
     * This method is where the logic for the odometer will run.
     */
    @Override
    public void run() {
        long updateStart, updateEnd; //
        
        double distanceL; //distance travelled by left wheel
        double distanceR; //distance travelled by right wheel
        double deltaD;//average distance travelled
        double deltaT;//angle displacement
        double dX;//x displacement during the loop
        double dY;//y displacement during the loop
        double dtheta;//angle displacement in radiants
        
        while (true) {
            updateStart = System.currentTimeMillis();
            leftMotorTachoCount = leftMotor.getTachoCount();
            rightMotorTachoCount = rightMotor.getTachoCount();
            
            // gets the distance travelled between two loops
            distanceL = Math.PI * WHEEL_RAD * (leftMotorTachoCount - previousLeftMotorTachoCount) / 180;
            distanceR = Math.PI * WHEEL_RAD * (rightMotorTachoCount - previousRightMotorTachoCount) / 180;
            
            // store the previous in memory for the next loop
            previousLeftMotorTachoCount = leftMotorTachoCount;
            previousRightMotorTachoCount = rightMotorTachoCount;
            
            // use formulas provided in class
            deltaD = 0.5 * (distanceL + distanceR);
            deltaT = (distanceL - distanceR) / TRACK;
            theta += deltaT;
            dX = deltaD * Math.sin(Math.toRadians(theta));
            dY = deltaD * Math.cos(Math.toRadians(theta));
            dtheta = Math.toDegrees(deltaT);
            
            // update the screen
            odo.update(dX, dY, dtheta);
            
            // this ensures that the odometer only runs once every period
            updateEnd = System.currentTimeMillis();
            if (updateEnd - updateStart < ODOMETER_PERIOD) {
                try {
                    Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
                } catch (InterruptedException e) {
                    // there is nothing to be done
                }
            }
        }
    }
    
    // IT IS NOT NECESSARY TO MODIFY ANYTHING BELOW THIS LINE
    
    /**
     * Returns the Odometer data.
     * <p>
     * Writes the current position and orientation of the robot onto the odoData
     * array. {@code odoData[0] =
     * x, odoData[1] = y; odoData[2] = theta;}
     *
     * @return the odometer data.
     */
    public double[] getXYT() {
        double[] position = new double[3];
        lock.lock();
        try {
            while (isResetting) { // If a reset operation is being executed, wait until it is over.
                doneResetting.await(); // Using await() is lighter on the CPU than simple busy wait.
            }
            
            position[0] = x;
            position[1] = y;
            position[2] = theta;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        
        return position;
    }
    
    /**
     * Adds dx, dy and dtheta to the current values of x, y and theta, respectively.
     * Useful for odometry.
     *
     * @param dx      : current x
     * @param dy:     current y
     * @param dtheta: current theta
     */
    public void update(double dx, double dy, double dtheta) {
        lock.lock();
        isResetting = true;
        try {
            x += dx;
            y += dy;
            theta = (theta + (360 + dtheta) % 360) % 360; // keeps the updates within 360 degrees
            isResetting = false;
            doneResetting.signalAll(); // Let the other threads know we are done resetting
        } finally {
            lock.unlock();
        }
        
    }
    
    /**
     * Overrides the values of x, y and theta. Use for odometry correction.
     *
     * @param x     the value of x
     * @param y     the value of y
     * @param theta the value of theta in degrees
     */
    public void setXYT(double x, double y, double theta) {
        lock.lock();
        isResetting = true;
        try {
            this.x = x;
            this.y = y;
            this.theta = theta;
            isResetting = false;
            doneResetting.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Overwrites x. Use for odometry correction.
     *
     * @param x the value of x
     */
    public void setX(double x) {
        lock.lock();
        isResetting = true;
        try {
            this.x = x;
            isResetting = false;
            doneResetting.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Overwrites y. Use for odometry correction.
     *
     * @param y the value of y
     */
    public void setY(double y) {
        lock.lock();
        isResetting = true;
        try {
            this.y = y;
            isResetting = false;
            doneResetting.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Overwrites theta. Use for odometry correction.
     *
     * @param theta the value of theta
     */
    public void setTheta(double theta) {
        lock.lock();
        isResetting = true;
        try {
            this.theta = theta;
            isResetting = false;
            doneResetting.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
}

package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class Accelermotor4237 extends Sensor4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    Accelerometer accelerometer = new BuiltInAccelerometer();
        
    public void BuiltInAccelerometer()
    {
        
    }

    @Override
    public void readPeriodicInputs()
    {
        double xAccel = accelerometer.getX();
        double yAccel = accelerometer.getY();
    }

    @Override
    public void writePeriodicOutputs()
    {

    }
}

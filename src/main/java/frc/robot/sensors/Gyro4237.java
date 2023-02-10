package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.sensors.WPI_PigeonIMU;

import edu.wpi.first.math.geometry.Rotation2d;

public class Gyro4237 extends Sensor4237
{

    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();
    WPI_PigeonIMU gyro = new WPI_PigeonIMU(0);
    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class PeriodicIO
    {
        private double angle;
        private Rotation2d rotation2d;
    }

    private boolean resetGyro = false;

    // private final WPI_Pigeon2 gyro = new WPI_Pigeon2(Constants.Sensor.PIGEON, Constants.Motor.CAN_BUS);
    private final PeriodicIO periodicIO = new PeriodicIO();

    public Gyro4237()
    {
        //reset();
        gyro.setYaw(180.0);
        periodicIO.angle = gyro.getYaw();
        // periodicIO.rotation2d = gyro.getRotation2d();
    }

    public void reset()
    {
        resetGyro = true;
        // gyro.reset();
    }

    public double getAngle()
    {
        return periodicIO.angle;
        //return gyro.getRoll(); // x-axis
        //return gyro.getPitch(); // y-axis
        //return gyro.getYaw(); // z-axis
    }

    public Rotation2d getRotation2d()
    {
        return periodicIO.rotation2d;
        // return gyro.getRotation2d();
    }

    @Override
    public synchronized void readPeriodicInputs()
    {
        periodicIO.angle = gyro.getYaw(); // z-axis
        // periodicIO.angle = gyro.getRoll(); // x-axis
        // periodicIO.angle = gyro.getPitch(); // y-axis

        // periodicIO.rotation2d = gyro.getRotation2d();
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        if(resetGyro)
        {
            gyro.reset();
            resetGyro = false;
        }

        System.out.println(toString());
    }

    @Override
    public String toString()
    {
        return String.format("Gyro %f \n", periodicIO.angle);
    }
}

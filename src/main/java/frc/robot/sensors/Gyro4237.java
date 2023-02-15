package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import javax.swing.plaf.synth.SynthTextAreaUI;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.ctre.phoenix.sensors.Pigeon2.AxisDirection;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;

public class Gyro4237 extends Sensor4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum ResetState
    {
        kStart, kTry, kDone;
    }


    private class PeriodicIO
    {
        // Inputs
        private double angle;
        private Rotation2d rotation2d;

        // Outputs
    }

    private final WPI_Pigeon2 gyro = new WPI_Pigeon2(Constants.Gyro.PIGEON_ID, Constants.Gyro.PIGEON_CAN_BUS);
    private ResetState resetState = ResetState.kDone;
    private Timer timer = new Timer();

    // private final WPI_Pigeon2 gyro = new WPI_Pigeon2(Constants.Sensor.PIGEON, Constants.Motor.CAN_BUS);
    private final PeriodicIO periodicIO = new PeriodicIO();

    public Gyro4237()
    {
        //reset();
        // gyro.setYaw(180.0);
        initPigeon();
        periodicIO.angle = gyro.getYaw();
        periodicIO.rotation2d = gyro.getRotation2d();

    }

    public void initPigeon()
    {
        gyro.configFactoryDefault();
        gyro.configMountPose(Constants.Gyro.FORWARD_AXIS, Constants.Gyro.UP_AXIS); //forward axis and up axis
        // gyro.setYaw(180.0);  // 2022 robot started with front facing away from the driver station, 2023 will not
        gyro.reset();
        Timer.delay(0.5);
    }

    public void reset()
    {
        resetState = ResetState.kStart;
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
        if (resetState == ResetState.kDone)
        {
            // periodicIO.angle = gyro.getYaw(); // z-axis
            // periodicIO.angle = gyro.getRoll(); // x-axis
            periodicIO.angle = gyro.getPitch(); // y-axis

            periodicIO.rotation2d = gyro.getRotation2d();
        }
    }

    @Override
    public synchronized void writePeriodicOutputs()
    {
        if(resetState == ResetState.kStart)
        {
            gyro.reset();
            timer.reset();
            timer.start();
            // gyro.setYaw(180.0);
            resetState = ResetState.kTry;
        }
        else if (resetState == ResetState.kTry && timer.hasElapsed(Constants.Gyro.RESET_GYRO_DELAY))
                resetState = ResetState.kDone;

        // System.out.println(periodicIO.angle + "   " + periodicIO.rotation2d.getDegrees());
    }

    @Override
    public String toString()
    {
        return String.format("Gyro %f \n", periodicIO.angle);
    }

    
}

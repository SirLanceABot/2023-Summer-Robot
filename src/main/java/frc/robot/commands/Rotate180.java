package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.Gyro4237;
import frc.robot.subsystems.Drivetrain;

public class Rotate180 extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private boolean fieldRelative;

    private double initialYaw;
    private double currentYaw;
    private double targetYaw;
    private double rotatePower;
    private double rotateError;
    private final double ROTATE_TOLERANCE = 1.0;
    private final double ROTATE_KP = 0.1;

    /**
     * Allows driver to quickly rotate the robot 180 degrees while still driving around the field
     * 
     * @param drivetrain
     * @param gyro
     * @param xSpeed
     * @param ySpeed
     * @param fieldRelative
     */
    public Rotate180(Drivetrain drivetrain, Gyro4237 gyro, DoubleSupplier xSpeed, DoubleSupplier ySpeed, boolean fieldRelative)
    {
        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.fieldRelative = fieldRelative;

        if(this.drivetrain != null)
        {
            addRequirements(drivetrain);
        }
    }

    @Override 
    public void initialize()
    {
        initialYaw = gyro.getYaw();
        targetYaw = initialYaw + 180.0;
    }

    @Override
    public void execute()
    {
        currentYaw = gyro.getYaw();
        rotateError = MathUtil.inputModulus(targetYaw - currentYaw, -180.0, 180.0);

        rotatePower = rotateError * ROTATE_KP;

        if(drivetrain != null)
        {
            drivetrain.drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), rotatePower, fieldRelative);
        }
    }

    @Override
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {
        if(drivetrain != null)
        {
            return false;
        }
        else if(Math.abs(rotateError) < ROTATE_TOLERANCE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
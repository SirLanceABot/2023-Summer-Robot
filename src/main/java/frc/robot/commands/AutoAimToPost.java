package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Vision;


public class AutoAimToPost extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private final Vision vision;

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    NetworkTableEntry tx = table.getEntry("tx");

    private final double POST_ALIGNMENT_TOLERANCE = 0.50;  //Limelight angle measurement in degrees
    private final double POST_ALIGNMENT_ROTATE_TOLERANCE = 0.1;
    private final double POST_ALIGNMENT_STRAFE_KP = 0.050;
    private final double POST_ALIGNMENT_ROTATE_KP = 0.050;
    private final double POST_ALIGNMENT_MIN_SPEED = 0.2;
    private final Timer timer = new Timer();

    private double strafeError;
    private double rotateError;
    private double strafePower;
    private double rotatePower;
    private boolean doneRotating = false;

    /**
     * Creates a new AutoAimToPost
     * 
     *
     * @param drivetrain Drivetrain subsystem.
     * @param vision Vision sensor.
     */
    public AutoAimToPost(Drivetrain drivetrain, Gyro4237 gyro, Vision vision) 
    {
        // System.out.println(fullClassName + ": Constructor Started");
        
        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.vision = vision;
        
        if(this.drivetrain != null)
        {
            addRequirements(drivetrain);
        }
            
        
        // System.out.println(fullClassName + ": Constructor Finished");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);  //turns limelight on
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);  //turns limelight on
        timer.reset();
        timer.start();

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // double xDistance = vision.getx();
        strafeError = vision.getX();
        rotateError = 180.0 - gyro.getYaw();

        strafePower = -(POST_ALIGNMENT_STRAFE_KP * strafeError);
        rotatePower = POST_ALIGNMENT_ROTATE_KP * rotateError;

        if(Math.abs(strafePower) < POST_ALIGNMENT_MIN_SPEED)
        {
            strafePower = Math.copySign(POST_ALIGNMENT_MIN_SPEED, strafePower);
        }

        if(drivetrain != null)
        {
            if(!doneRotating)
            {
                drivetrain.drive(0.0, strafePower, rotatePower, false);
            }
            else
            {
                drivetrain.drive(0.0, strafePower, 0.0, false);
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        // double xDistance = vision.getX();

        if(drivetrain != null)
        {
            drivetrain.drive(0.0, 0.0, 0.0, false);
        }

        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);  //turns limelight off
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);  //turns limelight off

        // System.out.println("End X: " + xDistance);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        strafeError = vision.getX();
        rotateError = 180 - gyro.getYaw();

        if(Math.abs(rotateError) < POST_ALIGNMENT_ROTATE_TOLERANCE)
        {
            doneRotating = true;
        }

        if(Math.abs(strafeError) < POST_ALIGNMENT_TOLERANCE && timer.hasElapsed(1.0) && doneRotating)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "AutoAimToPost(drivetrian, vision)";
    }
    
}

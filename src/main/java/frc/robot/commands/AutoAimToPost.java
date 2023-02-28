package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.Drivetrain;
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
    private final Vision vision;

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    NetworkTableEntry tx = table.getEntry("tx");

    private final double POST_ALIGNMENT_TOLERANCE = 0.5;  //Limelight angle measurement in degrees
    private final double POST_ALIGNMENT_DRIVE_KP = 0.020;

    private double error;
    private double drivePower;

    /**
     * Creates a new AutoAimToPost
     * 
     *
     * @param drivetrain Drivetrain subsystem.
     * @param vision Vision sensor.
     */
    public AutoAimToPost(Drivetrain drivetrain, Vision vision) 
    {
        System.out.println(fullClassName + ": Constructor Started");
        
        this.drivetrain = drivetrain;
        this.vision = vision;
        
        if(this.drivetrain != null)
            addRequirements(drivetrain);
        
        System.out.println(fullClassName + ": Constructor Finished");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // double xDistance = vision.getx();
        error = vision.getX();

        drivePower = -(POST_ALIGNMENT_DRIVE_KP * error);

        if(drivetrain != null)
        {
            drivetrain.drive(0.0, drivePower, 0.0, true);
        }
        // System.out.println("  X: " + xDistance);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        // double xDistance = vision.getX();

        if(drivetrain != null)
        {
            drivetrain.drive(0.0, 0.0, 0.0, true);
        }
        // System.out.println("End X: " + xDistance);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        error = vision.getX();

        if(Math.abs(error) < POST_ALIGNMENT_TOLERANCE)
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

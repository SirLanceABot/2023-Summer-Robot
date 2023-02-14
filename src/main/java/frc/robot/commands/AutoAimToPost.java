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

    private final double alignmentTolerance = 0.5;  //Limelight angle measurement in degrees


    /**
     * Creates a new AutoAimToPost
     *
     * @param subsystem The subsystem used by this command.
     */
    public AutoAimToPost(Drivetrain drivetrain, Vision vision) 
    {
        System.out.println("Constructor");
        this.drivetrain = drivetrain;
        this.vision = vision;
        
        if(this.drivetrain != null)
            addRequirements(drivetrain);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        double xDistanceNT = tx.getDouble(0.0);
        double xDistance = vision.getX();

        if(drivetrain != null)
            drivetrain.drive(0.0, -Math.signum(xDistance)*0.50, 0.0, true);

        // System.out.println("  X: " + xDistance);
        // System.out.println("XNT: " + xDistanceNT);
        // System.out.println(System.currentTimeMillis());
        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        double xDistanceNT = tx.getDouble(0.0);
        double xDistance = vision.getX();

        if(drivetrain != null)
            drivetrain.drive(0.0, 0.0, 0.0, true);

        System.out.println("Stop");
        
        // System.out.println("End X: " + xDistance);
        // System.out.println("End XNT: " + xDistanceNT);
        // System.out.println(System.currentTimeMillis());
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        double xDistanceNT = tx.getDouble(0.0);
        double xDistance = vision.getX();

        if(Math.abs(xDistance) < alignmentTolerance)
        {
            // System.out.println("Finished X: " + xDistance);
            // System.out.println("Finished XNT: " + xDistanceNT);
            // System.out.println(System.currentTimeMillis());
            return true;
        }
        else
        {
            return false;
        }
        
    }
}

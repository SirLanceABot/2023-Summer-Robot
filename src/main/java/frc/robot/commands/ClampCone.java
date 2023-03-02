package frc.robot.commands;

// import frc.robot.subsystems.ExampleSubsystem;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;

/** 
 * An example command that uses an example subsystem. 
 */
public class ClampCone extends CommandBase 
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
    private final Shoulder shoulder;
    private final Arm arm;
    private final Grabber grabber;


    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public ClampCone(Shoulder shoulder, Arm arm, Grabber grabber) 
    {
        this.shoulder = shoulder;
        this.arm = arm;
        this.grabber = grabber;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(shoulder != null && arm != null && grabber != null)
            {
                // addRequirements(this.shoulder);
                // addRequirements(this.arm);
                // addRequirements(this.grabber);
            }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(shoulder != null && arm != null && grabber != null)
        {
            arm.moveToArmReadyToClamp();
            shoulder.moveToSuctionCone();
            grabber.grabGamePiece();
            shoulder.moveToShoulderReadyToClamp();
            arm.moveToClampCone();
            shoulder.moveToClampCone();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return false;
    }
    
    @Override
    public boolean runsWhenDisabled()
    {
        return false;
    }

    @Override
    public String toString()
    {
        String str = this.getClass().getSimpleName();
        return String.format("Command: %s( )", str);
    }
}


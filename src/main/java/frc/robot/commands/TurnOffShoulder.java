package frc.robot.commands;
import frc.robot.subsystems.Shoulder;
import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class TurnOffShoulder extends CommandBase 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Shoulder shoulder;
    private boolean isFinished;


    /**
     * Creates a new TurnOffShoulder.
     *
     * @param shoulder The subsystem used by this command.
     */
    public TurnOffShoulder(Shoulder shoulder) 
    {
        this.shoulder = shoulder;
        
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(this.shoulder);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        isFinished = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        shoulder.off();
        isFinished = true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return isFinished;
    }
}

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import frc.robot.subsystems.Gatherer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * accesses Gatherer
 */
public class ReverseGathererIntake extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private final static String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private boolean isFinished;

    private Gatherer gatherer;

    /*
     * Constructor
     */
    public ReverseGathererIntake(Gatherer gatherer)
    {
        this.gatherer = gatherer;

        if(gatherer != null)
        {
            addRequirements(this.gatherer);
        }
        
    }

    /**
     * Called when the command is initially scheduled.
     */
    @Override
    public void initialize()
    {
        isFinished = false;
    }

    /**
     * Called every time the scheduler runs while the command is scheduled.
     */
    @Override
    public void execute()
    {
        if(gatherer != null)
        {
            gatherer.freeGamePiece();
        }
        
        isFinished = true;
    }

    /**
     * Called once the command ends or is interrupted.
     */
    @Override
    public void end(boolean interrupted)
    {}

    /**
     *  Returns true when the command should end.
     */
    @Override
    public boolean isFinished() 
    {
        return isFinished;
    }
}
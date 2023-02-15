package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import frc.robot.subsystems.Gatherer;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Accesses Gatherer
 */
public class TurnOffGathererIntake extends CommandBase
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
    private boolean isFinished;
    private Gatherer gatherer;

    /**
     * Constructor
     */
    public TurnOffGathererIntake(Gatherer gatherer)
    {
        this.gatherer = gatherer;

        addRequirements(this.gatherer);
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
        gatherer.turnOff();
        isFinished = true;
    }

    /**
     *  Called once the command ends or is interrupted.
     */
    @Override
    public void end(boolean interrupted)
    {}

    /**
     * Returns true when the command should end.
     */
    @Override
    public boolean isFinished() 
    {
        return isFinished;
    }
}
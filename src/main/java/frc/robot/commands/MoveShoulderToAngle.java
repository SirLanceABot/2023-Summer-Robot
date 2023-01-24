package frc.robot.commands;

import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Shoulder.LevelAngle;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MoveShoulderToAngle extends CommandBase
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private final Shoulder shoulder;
    private boolean isFinished;
    private LevelAngle desiredAngle;

    
    // private final double LEVEL_0_ANGLE = 5;         // Gatherer Position
    // private final double LEVEL_1_ANGLE = 30;        // Low Scoring Position
    // private final double LEVEL_2_ANGLE = 60;        // Middle Scoring Position
    // private final double LEVEL_3_ANGLE = 100;       // High Scoring Position
    
    /**
     * Creates a new RaiseShoulder.
     *
     * @param shoudler The subsystem used by this command.
     */
    public MoveShoulderToAngle(Shoulder shoulder, LevelAngle desiredLevel) 
    {
        this.shoulder = shoulder;
        this.desiredAngle = desiredLevel;
        
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
        //TODO: use angles or encoder ticks?
        if(shoulder.getAngle() < desiredAngle.min)
        {
            shoulder.moveUp();
        }
        else if(shoulder.getAngle() > desiredAngle.max)
        {
            shoulder.moveDown();
        }
        else
        { 
            isFinished = true;
        }
    }


    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return isFinished;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        shoulder.hold();    // once shoulder is done moving, hold position
    }

}
package frc.robot.commands;

import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Shoulder.ShoulderPosition;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MoveShoulderToScoringPosition extends CommandBase
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
    private ShoulderPosition desiredPosition;

    
    // private final double LEVEL_0_ANGLE = 5;         // Gatherer Position
    // private final double LEVEL_1_ANGLE = 30;        // Low Scoring Position
    // private final double LEVEL_2_ANGLE = 60;        // Middle Scoring Position
    // private final double LEVEL_3_ANGLE = 100;       // High Scoring Position
    
    /**
     * Creates a new MoveShoulderToScoringPosition.
     * Moves the shoulder to one of the predetermined positions
     *
     * @param shoudler Shoulder subsystem.
     * @param desiredPosition Position that the shoulder needs to go to (ScoringPosition)
     */
    public MoveShoulderToScoringPosition(Shoulder shoulder, ShoulderPosition desiredPosition) 
    {
        System.out.println("Command Contructor Started");
        this.shoulder = shoulder;
        this.desiredPosition = desiredPosition;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(shoulder != null)
        {
            addRequirements(this.shoulder);
        }
        System.out.println("Command Contructor Ended");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        System.out.println("Initialize");

        isFinished = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(shoulder != null)
        {
            System.out.println("Execute");

            switch(desiredPosition)
            {
                case kHigh:
                    System.out.println("High");
                    shoulder.moveToHigh();
                    break;
                
                case kMiddle:
                    shoulder.moveToMiddle();
                    break;
                
                case kLow:
                    shoulder.moveToLow();
                    break;
                
                case kGather:
                    shoulder.moveToGather();
                    break;
                
                case kOverride:
                    break;
            }
        }

        // if(shoulder !=  null)
        // {
        //     if(shoulder.getPosition() < desiredPosition.min)
        //     {
        //         shoulder.moveUp();
        //     }
        //     else if(shoulder.getPosition() > desiredPosition.max)
        //     {
        //         shoulder.moveDown();
        //     }
        //     else
        //     { 
        //         isFinished = true;
        //     }
        // }
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
        if(shoulder != null)
        {
            shoulder.hold();    // once shoulder is done moving, hold position
        }
        
    }

    @Override
    public String toString()
    {
        return "MoveShoulderToScoringPosition(shoulder, " + desiredPosition + ")";
    }
}
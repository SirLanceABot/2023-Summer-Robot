package frc.robot.commands;

import frc.robot.subsystems.Shoulder;
import frc.robot.Constants.TargetPosition;

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

    private TargetPosition desiredPosition;

    
    /**
     * 
     * Creates a new MoveShoulderToScoringPosition.
     * Moves the shoulder to one of the predetermined positions
     *
     * @param shoudler Shoulder subsystem.
     * @param desiredPosition Position that the shoulder needs to go to (ScoringPosition)
     */
    public MoveShoulderToScoringPosition(Shoulder shoulder, TargetPosition desiredPosition) 
    {
        this.shoulder = shoulder;
        this.desiredPosition = desiredPosition;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(shoulder != null)
        {
            addRequirements(this.shoulder);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(shoulder != null)
        {
            switch(desiredPosition)
            {
                case kHighCone:
                    shoulder.moveToHighCone();
                    break;

                case kHighConeLock:
                    shoulder.moveToHighConeLock();
                    break;

                case kHighCube:
                    shoulder.moveToHighCube();
                    break;
                
                case kMiddleCone:
                    shoulder.moveToMiddleCone();
                    break;
                
                case kMiddleCube:
                    shoulder.moveToMiddleCube();
                    break;
                
                case kLowCone:
                    shoulder.moveToLow();
                    break;
                
                case kGather:
                    shoulder.moveToGather();
                    break;

                case kClamp:
                    shoulder.moveToClampCone();
                    break;

                case kReadyToPickUp:
                    shoulder.moveToReadyToPickUp();
                    break;
                
                case kShoulderReadyToClamp:
                    shoulder.moveToShoulderReadyToClamp();
                    break;

                case kSuctionCone:
                    shoulder.moveToSuctionCone();
                    break;

                case kStartingPosition:
                    shoulder.moveToStartingPosition();
                    break;
                
                case kSubstation:
                    shoulder.moveToSubstation();
                    break;

                case kLimelight:
                    shoulder.moveToLimelight();
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
        if(shoulder != null && desiredPosition != TargetPosition.kOverride)
        {
            return shoulder.atSetPoint();
        
        }
        return true;
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        // if(shoulder != null)
        // {
        //     shoulder.off();    // once shoulder is done moving, hold position
        // }
        
    }

    @Override
    public String toString()
    {
        return "MoveShoulderToScoringPosition(" + desiredPosition + ")";
    }
}
package frc.robot.commands;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Arm.ArmPosition;
import frc.robot.subsystems.Shoulder.ShoulderPosition;
import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** 
 * Move arm and shoulder to a scoring position. Uses arm and shoulder subsytems. 
 */
public class ScoreGamePiece extends CommandBase 
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
    private final Arm arm;
    private final Shoulder shoulder;
    private ArmPosition targetArmPosition;
    private ShoulderPosition targetShoulderPosition;
    private Boolean isArmFirst;
    private Boolean isFinished = false;
    private int movementStep = 0;


    /**
     * Creates a new MoveArmAndShoulder.
     *
     * @param arm The arm subystem.
     * @param shoulder The shoulder subsystem.
     * @param targetArmPosition Target position for the arm (Type: ArmPosition)
     * @param targetShoulderPosition Target position for the shoulder (Type: ShoulderPosition)
     */
    public ScoreGamePiece(Arm arm, Shoulder shoulder, ArmPosition targetArmPosition, ShoulderPosition targetShoulderPosition) 
    {
        this.arm = arm;
        this.shoulder = shoulder;
        this.targetArmPosition = targetArmPosition;
        this.targetShoulderPosition = targetShoulderPosition;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(arm != null && shoulder != null)
        {
            addRequirements(this.arm);
            addRequirements(this.shoulder);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        isFinished = false;
        movementStep = 0;

        if(shoulder != null && arm != null)
        {
            if(shoulder.getPosition() > targetShoulderPosition.value)
            {
                isArmFirst = true;
            }
            else
            {
                isArmFirst = false;
            }

            movementStep = 1;
        }

        
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        switch(movementStep)
        {
            case 0:
                isFinished = true;
                break;

            case 1:
                if(isArmFirst)  // if arm needs to move first
                {
                    moveArmToScoringPosition(arm, targetArmPosition);
            
                    if(arm.atSetPoint())    // if arm is done moving, go to the next step
                    {
                        movementStep++; 
                    }
                }
                else  // if shoulder needs to move first
                {
                    moveShoulderToScoringPosition(shoulder, targetShoulderPosition);

                    if(shoulder.atSetPoint())    // if shoulder is done moving, go to the next step
                    {
                        movementStep++;
                    }
                }
                break;
            
            case 2:
                if(isArmFirst)  // if arm needs to move first
                {
                    moveShoulderToScoringPosition(shoulder, targetShoulderPosition);

                    if(shoulder.atSetPoint())    // if shoulder is done moving, go to the next step
                    {
                        movementStep++; 
                    }
                }
                else    // if shoulder needs to move first
                {
                    moveArmToScoringPosition(arm, targetArmPosition);

                    if(arm.atSetPoint())    // if arm is done moving, go to the next step
                    {
                        movementStep++; 
                    }
                }
                break;
            
            case 3:
                isFinished = true;
                break;
        }

    }

    // Moves the arm to the correct position
    public void moveArmToScoringPosition(Arm arm, ArmPosition targetPosition)
    {
        if(arm != null)
        {
            switch(targetPosition)
            {
                case kHigh:
                    arm.moveToHigh();
                    break;
                
                case kMiddle:
                    arm.moveToMiddle();
                    break;
                
                case kLow:
                    arm.moveToLow();
                    break;
                
                case kGather:
                    arm.moveToGather();
                    break;
                
                case kOverride:
                    break;
            }
        }
    }

    // Moves shoulder to the correct position
    public void moveShoulderToScoringPosition(Shoulder shoulder, ShoulderPosition targetPosition)
    {
        if(shoulder != null)
        {
            switch(targetPosition)
            {
                case kHigh:
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
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        return isFinished;
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
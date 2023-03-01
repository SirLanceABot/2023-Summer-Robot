package frc.robot.commands;

import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Wrist;
// import frc.robot.subsystems.Arm.TargetPosition;
// import frc.robot.subsystems.Shoulder.TargetPosition;
import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;

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
    private final Shoulder shoulder;
    private final Arm arm;
    private final Grabber grabber;
    private final Wrist wrist;
    // private TargetPosition targetArmPosition;
    // private TargetPosition targetShoulderPosition;
    private TargetPosition targetPosition;
    private Boolean isArmFirst;
    private Boolean isFinished = false;
    private int movementStep = 0;
    private final Timer timer = new Timer();


    /**
     * Creates a new MoveArmAndShoulder.
     *
     * @param shoulder The shoulder subsystem.
     * @param arm The arm subystem.
     * @param grabber The grabber subsystem.
     * @param wrist The wrist subsystem.
     * @param targetArmPosition Target position for the arm (Type: ArmPosition)
     * @param targetShoulderPosition Target position for the shoulder (Type: ShoulderPosition)
     */
    public ScoreGamePiece(Shoulder shoulder, Arm arm, Grabber grabber, Wrist wrist, TargetPosition targetPosition) 
    {
        this.shoulder = shoulder;
        this.arm = arm;
        this.grabber = grabber;
        this.wrist = wrist;
        this.targetPosition = targetPosition;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(shoulder != null && arm != null &&  grabber != null && wrist != null)
        {
            addRequirements(this.shoulder);
            addRequirements(this.arm);
            addRequirements(this.grabber);
            addRequirements(this.wrist);
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
            if(shoulder.getPosition() > targetPosition.shoulder)
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
                    moveArmToScoringPosition(arm, targetPosition);
            
                    if(arm.atSetPoint())    // if arm is done moving, go to the next step
                    {
                        movementStep++; 
                    }
                }
                else  // if shoulder needs to move first
                {
                    moveShoulderToScoringPosition(shoulder, targetPosition);

                    if(shoulder.atSetPoint())    // if shoulder is done moving, go to the next step
                    {
                        movementStep++;
                    }
                }
                break;
            
            case 2:
                if(isArmFirst)
                {
                    if(wrist != null)
                    {
                        wrist.wristDown();
                    }
                    
                }
                else
                {
                    if(wrist != null)
                    {
                        wrist.wristUp();
                    }
                }

                timer.reset();
                timer.start();
                movementStep++;
                break;
            
            case 3:
                if(timer.hasElapsed(0.2))
                {
                    movementStep++;
                }
                break;
            
            case 4:
                if(isArmFirst)  // if arm needs to move first
                {
                    moveShoulderToScoringPosition(shoulder, targetPosition);

                    if(shoulder.atSetPoint())    // if shoulder is done moving, go to the next step
                    {
                        movementStep++; 
                    }
                }
                else    // if shoulder needs to move first
                {
                    moveArmToScoringPosition(arm, targetPosition);

                    if(arm.atSetPoint())    // if arm is done moving, go to the next step
                    {
                        movementStep++; 
                    }
                }
                break;
            
            case 5:
                if(grabber != null)
                {
                    grabber.releaseGamePiece();
                }

                movementStep++;
                
            case 6:
                System.out.println("DONE");
                isFinished = true;
                break;
        }

    }

    // Moves shoulder to the correct position
    public void moveShoulderToScoringPosition(Shoulder shoulder, TargetPosition targetPosition)
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

    // Moves the arm to the correct position
    public void moveArmToScoringPosition(Arm arm, TargetPosition targetPosition)
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
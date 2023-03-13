package frc.robot.commands;

import frc.robot.Constants.SuctionState;
import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristPosition;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;


/** 
 * Move arm and shoulder to a scoring position. Uses arm and shoulder subsytems. 
 */
public class ExtendScorer extends SequentialCommandGroup
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
    private TargetPosition targetPosition;


    /**
     * Creates a new ExtendScorer.
     *
     * @param shoulder The shoulder subsystem.
     * @param arm The arm subystem.
     * @param wrist The wrist subsystem.
     * @param grabber The grabber subsystem.
     * @param targetPosition Target position (Type: TargetPosition)
     */
    public ExtendScorer(Shoulder shoulder, Arm arm, Wrist wrist, Grabber grabber, TargetPosition targetPosition) 
    {
        this.shoulder = shoulder;
        this.arm = arm;
        this.grabber = grabber;
        this.wrist = wrist;
        this.targetPosition = targetPosition;
        
        // Use addRequirements() here to declare subsystem dependencies.
        // if(shoulder != null && arm != null && wrist != null)
        if(shoulder != null && arm != null &&  grabber != null && wrist != null)
        {
            addRequirements(this.shoulder);
            addRequirements(this.arm);
            addRequirements(this.grabber);
            addRequirements(this.wrist);

            build();
        }
    }

    private void build()
    {
        if(targetPosition == TargetPosition.kHighCone || targetPosition == TargetPosition.kMiddleCone || targetPosition == TargetPosition.kLowCone)
        {
            addCommands( new ParallelCommandGroup( 
            new MoveShoulderToScoringPosition(shoulder, targetPosition),
            new SequentialCommandGroup( 
                new WaitUntilCommand(() -> shoulder.getPosition() > TargetPosition.kLowCone.shoulder).withTimeout(1.0),
                new MoveWrist(wrist, WristPosition.kUp))));
            addCommands( new MoveArmToScoringPosition(arm, targetPosition));
            // addCommands( new MoveWrist(wrist, WristPosition.kUp) );
            // addCommands( new MoveArmToScoringPosition(arm, targetPosition) );
            // addCommands( new ReleaseGamePiece(grabber) );
        }

        if(targetPosition == TargetPosition.kHighCube || targetPosition == TargetPosition.kMiddleCube || targetPosition == TargetPosition.kLowCube)
        {
            addCommands( new MoveShoulderToScoringPosition(shoulder, targetPosition));
            addCommands( new MoveArmToScoringPosition(arm, targetPosition));
        }

        if(targetPosition == TargetPosition.kSubstation)
        {
            addCommands( new ParallelCommandGroup( 
            // new GrabGamePiece(grabber),
            new SuctionControl(grabber, SuctionState.kOn),
            new MoveShoulderToScoringPosition(shoulder, TargetPosition.kSubstation),
            new SequentialCommandGroup( 
                new WaitUntilCommand(() -> shoulder.getPosition() > TargetPosition.kLowCone.shoulder).withTimeout(1.0)),
                new MoveArmToScoringPosition(arm, TargetPosition.kSubstation)));
        }
    }

    @Override
    public String toString()
    {
        return "ExtendScorer(" + targetPosition + ")";
    }
}
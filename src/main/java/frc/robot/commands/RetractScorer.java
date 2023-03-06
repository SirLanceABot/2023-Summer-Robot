package frc.robot.commands;

import frc.robot.Constants.TargetPosition;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristPosition;

// import frc.robot.subsystems.Arm.TargetPosition;
// import frc.robot.subsystems.Shoulder.TargetPosition;
import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


/** 
 * Move arm and shoulder to a scoring position. Uses arm and shoulder subsytems. 
 */
public class RetractScorer extends SequentialCommandGroup
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
    // private final Grabber grabber;
    private final Wrist wrist;
    private TargetPosition targetPosition;


    /**
     * Creates a new ExtendScorer.
     *
     * @param shoulder The shoulder subsystem.
     * @param arm The arm subystem.
     * @param wrist The wrist subsystem.
     * @param targetPosition Target position (Type: TargetPosition)
     */
    public RetractScorer(Shoulder shoulder, Arm arm, Wrist wrist, TargetPosition targetPosition) 
    {
        this.shoulder = shoulder;
        this.arm = arm;
        // this.grabber = grabber;
        this.wrist = wrist;
        this.targetPosition = targetPosition;
        
        // Use addRequirements() here to declare subsystem dependencies.
        // if(shoulder != null && arm != null &&  grabber != null && wrist != null)
        if(shoulder != null && arm != null && wrist != null)
        {
            addRequirements(this.shoulder);
            addRequirements(this.arm);
            // addRequirements(this.grabber);
            addRequirements(this.wrist);

            build();
        }
    }

    private void build()
    {
        addCommands( new MoveArmToScoringPosition(arm, targetPosition) );
        addCommands( new MoveWrist(wrist, WristPosition.kDown) );
        addCommands( new MoveShoulderToScoringPosition(shoulder, targetPosition) );
        // addCommands( new ReleaseGamePiece(grabber) );
    }

    @Override
    public String toString()
    {
        return "ExtendScorer(" + targetPosition + ")";
    }
}
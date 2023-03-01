package frc.robot.commands;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Shoulder;

// import frc.robot.subsystems.Arm.TargetPosition;
// import frc.robot.subsystems.Shoulder.TargetPosition;
import frc.robot.Constants.TargetPosition;
import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** 
 * Move arm and shoulder to a scoring position. Uses arm and shoulder subsytems. 
 */
public class ReturnToGather extends CommandBase 
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

    private TargetPosition targetArmPosition;
    private TargetPosition targetShoulderPosition;
    private Boolean isArmFirst;
    private Boolean isFinished = false;
    private int movementStep = 0;


    /**
     * Creates a new MoveArmAndShoulder.
     *
     * @param arm The arm subystem.
     * @param shoulder The shoulder subsystem.
     * @param wrist The wrist subsystem.
     */
    public ReturnToGather(Arm arm, Shoulder shoulder) 
    {
        this.arm = arm;
        this.shoulder = shoulder;

        
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
        movementStep = 1;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(arm != null && shoulder != null)
        {
            arm.moveToGather();
            
            shoulder.moveToGather();
        }

        // switch(movementStep)
        // {
        //     case 0:
        //         isFinished = true;
        //         break;

        //     case 1:
        //         arm.moveToGather();
        //         movementStep++;
        //         break;
            
        //     case 2:
        //         wrist.wristDown();
        //         movementStep++;
        //         break;
            
        //     case 3:
        //         shoulder.moveToGather();
        //         break;
            
        //     case 4:
        //         isFinished = true;
        //         break;
        // }
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
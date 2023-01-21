package frc.robot.commands;

import frc.robot.subsystems.Shoulder;

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
    private double shoulderAngle;
    private int desiredLevel;
    private double desiredAngle;
    
    //TODO: determine correct angles for levels
    private final double LEVEL_0_ANGLE = 5;         // Gatherer Position
    private final double LEVEL_1_ANGLE = 30;        // Low Scoring Position
    private final double LEVEL_2_ANGLE = 60;        // Middle Scoring Position
    private final double LEVEL_3_ANGLE = 100;       // High Scoring Position
     
    /**
     * Creates a new RaiseShoulder.
     *
     * @param shoudler The subsystem used by this command.
     */
    public MoveShoulderToAngle(Shoulder shoulder) 
    {
        this.shoulder = shoulder;
        
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
        shoulderAngle = shoulder.getShoulderAngle();
        
        //TODO: how do we get desiredLevel?

        // set desired angle based on constants
        switch(desiredLevel)
        {
            case 0:
                desiredAngle = LEVEL_0_ANGLE;
                break;
            case 1:
                desiredAngle = LEVEL_1_ANGLE;
                break;
            case 2:
                desiredAngle = LEVEL_2_ANGLE;
                break;
            case 3:
                desiredAngle = LEVEL_3_ANGLE;
                break;
        }

        // move shoulder to angle
        while(shoulderAngle < desiredAngle)
        {
            shoulder.moveUp();
        }
        while(shoulderAngle > desiredAngle)
        {
            shoulder.moveDown();
        }

        //TODO: do we want to check if it is at the correct angle?


        shoulder.hold();    // once shoulder is at correct angle, hold position
        
       
        isFinished = true;  
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


}

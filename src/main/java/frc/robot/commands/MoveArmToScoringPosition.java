// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmPosition;
import java.lang.invoke.MethodHandles;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * Command that moves the arm to where the user tells it to go
 */
public class MoveArmToScoringPosition extends CommandBase 
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
    private ArmPosition desiredPosition;

    /**
     * @deprecated
     * DO NOT USE THIS COMMAND 
     * INSTEAD USE SCOREGAMEPIECE
     * Creates a new MoveArmToScoringPosition.
     * Moves the arm to one of the predetermined positions
     *
     * @param arm Shoulder subsystem.
     * @param desiredPosition Position that the arm needs to go to (ScoringPosition)
     */
    public MoveArmToScoringPosition(Arm arm, ArmPosition desiredPosition) 
    {
        this.arm = arm;
        this.desiredPosition = desiredPosition;

        // Declares subsystem dependencies
        if(arm != null)
        {
            addRequirements(this.arm);
        }
        
    }

    /**
     * Called when the command is initially scheduled.
     * Sets isFinished to false, indicating that the command still needs to run
     */ 
    @Override
    public void initialize()
    {
          
    }
    
    /**
     * Called every time the scheduler runs while the command is scheduled.
     * Checks if the arm is in the desired position, and moves it accordingly.
     */
    @Override
    public void execute()
    {
        if(arm != null)
        {
            switch(desiredPosition)
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

    /**
     * Returns true when the command should end.
     */ 
    @Override
    public boolean isFinished()
    {
        if(arm != null && desiredPosition != ArmPosition.kOverride)
        {
            return arm.atSetPoint();
        }
        return true;
    } 

    /**
     * Called once the command ends or is interrupted.
     * Stops the motor, preventing the arm from moving.
     */
    @Override
    public void end(boolean interrupted)
    {
        // if(arm != null)
        // {
        //     arm.off();
        // }
        
    }

    @Override
    public String toString()
    {
        return "MoveArm(arm, " + desiredPosition + ")";
    }
}
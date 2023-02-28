// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Wrist;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** 
 * An example command that uses an example subsystem. 
 */
public class MoveWristDown extends CommandBase 
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
    private final Wrist wrist;
    public boolean isFinished;


    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public MoveWristDown(Wrist wrist) 
    {
        System.out.println(fullClassName + ": Constructor Started");
        
        this.wrist = wrist;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if (this.wrist != null)
            addRequirements(this.wrist);

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        System.out.println(this);

        isFinished = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        if(wrist != null)
        {
            wrist.wristDown();
        }
        
        
        // if(grabber.isGrabberClosed())
        // {
        //     isFinished = true;
        // }
        // else
        // {
        //     isFinished = false;
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
        return false;
    }

    public String toString()
    {
        return "GrabGamePiece()";
    }
}


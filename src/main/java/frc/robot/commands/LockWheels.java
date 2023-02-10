package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class LockWheels extends CommandBase
{
    
    private final Drivetrain drivetrain;
    
    public LockWheels(Drivetrain drivetrain)
    {
        this.drivetrain = drivetrain;

        if(this.drivetrain != null)
            addRequirements(drivetrain);
    }

    @Override 
    public void initialize()
    {
        if(drivetrain != null)
            drivetrain.lockWheels();
    }

    @Override
    public void execute()
    {}

    @Override
    public void end(boolean interrupted)
    {}

    @Override
    public boolean isFinished()
    {
        if(drivetrain != null)
            return false;
        else
            return true;
    }

}
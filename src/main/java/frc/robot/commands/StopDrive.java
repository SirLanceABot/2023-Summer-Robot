package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class StopDrive extends CommandBase
{
    private Drivetrain drivetrain;
    public StopDrive(Drivetrain drivetrain)
    {
        this.drivetrain = drivetrain;
        if (drivetrain != null)
        {
            addRequirements(drivetrain);
        }
    }

    @Override
    public void initialize()
    {
        if(drivetrain != null)
            drivetrain.stopMotor();
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
        return true;
    }
}

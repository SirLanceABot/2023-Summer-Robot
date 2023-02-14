package frc.robot.commands;

import java.util.function.DoubleSupplier;

import javax.lang.model.util.ElementScanner14;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class LockWheels extends CommandBase
{
    
    private final Drivetrain drivetrain;
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier turn;

    
    public LockWheels(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn)
    {
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.turn = turn;

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
        if(Math.abs(xSpeed.getAsDouble()) > 0.01 || Math.abs(ySpeed.getAsDouble()) > 0.01 || Math.abs(turn.getAsDouble()) > 0.01)
            return true;
        else
            return false;
    }

}
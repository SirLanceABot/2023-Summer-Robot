package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class SwerveDrive extends CommandBase
{
    private final Drivetrain drivetrain;
    private Supplier<Double> xSpeed;
    private Supplier<Double> ySpeed;
    private Supplier<Double> turn;
    private boolean fieldRelative;
    
    public SwerveDrive(Drivetrain drivetrain, Supplier<Double> xSpeed, Supplier<Double> ySpeed, Supplier<Double> turn, boolean fieldRelative)
    {
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.turn = turn;
        this.fieldRelative = fieldRelative;

        if(this.drivetrain != null)
            addRequirements(drivetrain);
    }

    @Override 
    public void initialize()
    {}

    @Override
    public void execute()
    {
        if(drivetrain != null)
            drivetrain.drive(xSpeed.get(), ySpeed.get(), turn.get(), fieldRelative);
    }

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
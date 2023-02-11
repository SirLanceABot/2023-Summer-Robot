package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class SwerveDrive extends CommandBase
{
    private final Drivetrain drivetrain;
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier turn;
    private boolean fieldRelative;
    
    public SwerveDrive(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn, boolean fieldRelative)
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
            // change robot speed
            drivetrain.drive(2.0 * xSpeed.getAsDouble(), 2.0 * ySpeed.getAsDouble(), 2.0 * turn.getAsDouble(), fieldRelative);
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
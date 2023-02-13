package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class LockWheels extends CommandBase
{
    
    private final Drivetrain drivetrain;
    private DoubleSupplier leftYAxis;
    private DoubleSupplier leftXAxis;
    private DoubleSupplier rightXAxis;

    
    public LockWheels(Drivetrain drivetrain, DoubleSupplier leftYAxis, DoubleSupplier leftXAxis, DoubleSupplier rightXAxis)
    {
        this.drivetrain = drivetrain;
        this.leftYAxis = leftYAxis;
        this.leftXAxis = leftXAxis;
        this.rightXAxis = rightXAxis;

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
        return false; 
    }

}
package frc.robot.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveDistanceAuto extends CommandBase
{
    private Drivetrain drivetrain;
    private double xSpeed;
    private double ySpeed;
    private double distanceToDriveMeters;
    private boolean isFinished;
    private Translation2d startingPosition;


    public DriveDistanceAuto(Drivetrain drivetrain, double xSpeed, double ySpeed, double distanceToDriveMeters)
    {
        this.drivetrain = drivetrain;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.distanceToDriveMeters = distanceToDriveMeters;

        if(this.drivetrain != null)
            addRequirements(drivetrain);
        
    }   

    @Override
    public void initialize()
    {
        isFinished = false;
        if(drivetrain != null)
        {
            startingPosition = drivetrain.getCurrentTranslation();
            //drivetrain.cofigOpenLoopRamp(0.75);
        }

    }

    @Override
    public void execute()
    {
        if(drivetrain != null)
        {
            double distanceDrivenMeters = drivetrain.getDistanceDrivenMeters(startingPosition);
            if(Math.abs(distanceDrivenMeters) < Math.abs(distanceToDriveMeters))
            {
                drivetrain.drive(xSpeed, ySpeed, 0.0, false);
            }
            else
            {
                drivetrain.stopMotor();
                isFinished = true;
            }
        }
    }

    @Override 
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
            drivetrain.stopMotor();
            //drivetrain.configOpenLoopRamp(1.0);
    }

    @Override
    public boolean isFinished()
    {
        if(drivetrain != null)
            return isFinished;
        else
            return true;
    }
}



package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveDistanceAuto extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private Drivetrain drivetrain;
    private double xSpeed;
    private double ySpeed;
    private double distanceToDriveMeters;
    // private boolean isFinished;
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
        // isFinished = false;
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
            drivetrain.drive(xSpeed, ySpeed, 0.0, false);
            // double distanceDrivenMeters = drivetrain.getDistanceDrivenMeters(startingPosition);
            // if(Math.abs(distanceDrivenMeters) < Math.abs(distanceToDriveMeters))
            // {
            //     drivetrain.drive(xSpeed, ySpeed, 0.0, false);
            // }
            // else
            // {
            //     drivetrain.stopMotor();
            // }
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
        {
            double distanceDrivenMeters = drivetrain.getDistanceDrivenMeters(startingPosition);
            if(Math.abs(distanceDrivenMeters) < Math.abs(distanceToDriveMeters))
            {
                return false;
            } 
        }
        return true;
    }
}



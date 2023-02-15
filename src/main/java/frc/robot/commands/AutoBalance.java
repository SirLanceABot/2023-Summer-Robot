package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.sensors.Gyro4237;
import frc.robot.subsystems.Drivetrain;


public class AutoBalance extends CommandBase
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
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;

    private double currentPitch;
    private double error;
    private double drivePower;

    private final double BEAM_BALANCED_GOAL_DEGREES = 0.0;
    private final double BEAM_BALANCED_DRIVE_KP = 0.015;
    private final double BEAM_BALANCED_TOLERANCE = 1.0;


    /**
     * Creates a new ExampleCommand.
    *
    * @param subsystem The subsystem used by this command.
    */
    public AutoBalance(Drivetrain drivetrain, Gyro4237 gyro) 
    {
        this.drivetrain = drivetrain;
        this.gyro = gyro;
        
        // Use addRequirements() here to declare subsystem dependencies.
        if(drivetrain != null)
        {
            addRequirements(this.drivetrain);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // currentPitch = gyro.getPitch();

        error = BEAM_BALANCED_GOAL_DEGREES - currentPitch;
        drivePower =  -Math.min(BEAM_BALANCED_DRIVE_KP * error, 1);

        if(Math.abs(drivePower) > 0.5)
        {
            drivePower = Math.copySign(0.4, drivePower);
        }

        if(drivetrain != null)
        {
            drivetrain.drive(0.0, -Math.signum(currentPitch)*0.50, 0.0, true);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.drive(0.0, 0.0, 0.0, true);
            drivetrain.lockWheels();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        if(Math.abs(currentPitch) <= BEAM_BALANCED_TOLERANCE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}

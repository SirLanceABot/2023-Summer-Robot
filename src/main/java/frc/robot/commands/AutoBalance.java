package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Commands;
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

    public enum BalanceState
    {
        // level means gyro is at zero, but not done balancing
        // engaged  means gyro has been at zero for 1 second, and is done balancing
        kNotLevel, kLevel, kBeenLevel;
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private final Timer finishBalanceTimer = new Timer();
    private BalanceState balanceState = BalanceState.kNotLevel;

    private double currentPitch;
    private double currentRoll;
    private double currentYaw;
    private double error;
    private double drivePower;
    

    private final double CS_BALANCE_GOAL_DEGREES = 0.0;
    private final double CS_BALANCE_DRIVE_KP = 0.015;
    private final double CS_BALANCE_TOLERANCE = 3.0;
    private final double CS_BALANCE_MIN_TIME_LEVEL = 1.0;
    private final double CS_BALANCE_MAX_SPEED = 0.5;


    /**
     * Creates a new ExampleCommand.
    *
    * @param subsystem The subsystem used by this command.
    */
    public AutoBalance(Drivetrain drivetrain, Gyro4237 gyro) 
    {
        System.out.println("Constructor");
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
        currentPitch = gyro.getPitch();
        currentYaw = (int)Math.abs(gyro.getYaw()) % 360;
        currentRoll = gyro.getRoll();

        SmartDashboard.putNumber("Current Pitch", currentPitch);
        SmartDashboard.putNumber("Current Yaw", currentYaw);


        // error = CS_BALANCE_GOAL_DEGREES - currentPitch;
        error = currentPitch;
        // error = currentRoll;

        // drivePower =  Math.min(CS_BALANCE_DRIVE_KP * error, 1);
 
        drivePower =  -(CS_BALANCE_DRIVE_KP * error);

        // if(Math.abs(drivePower) > CS_BALANCE_MAX_SPEED)
        if(Math.abs(drivePower) > CS_BALANCE_MAX_SPEED || Math.abs(error) > 14.0)
        {
            drivePower = Math.copySign(CS_BALANCE_MAX_SPEED, drivePower);
        }

        if((currentYaw > 135 && currentYaw < 225))
            drivePower = -drivePower;

        if(drivetrain != null)
        {
            drivetrain.drive(drivePower, 0.0, 0.0, true);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.drive(0.0, 0.0, 0.0, true);
            new LockWheels(drivetrain, () -> 0.0, () -> 0.0, () -> 0.0).schedule();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        if(Math.abs(error) > CS_BALANCE_TOLERANCE)
        {
            balanceState = BalanceState.kNotLevel;
            return false;
        }
        else if(Math.abs(error) <= CS_BALANCE_TOLERANCE && balanceState == BalanceState.kNotLevel)
        {
            finishBalanceTimer.reset();
            finishBalanceTimer.start();
            balanceState = BalanceState.kLevel;
            return false;
        }
        else if(balanceState == BalanceState.kLevel)
        {
            balanceState = BalanceState.kBeenLevel;
            return false;
        }
        else if(balanceState == BalanceState.kBeenLevel && finishBalanceTimer.get() < CS_BALANCE_MIN_TIME_LEVEL)
        {   
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public String toString()
    {
        return "AutoBalance(drivetrain, gyro)";
    }

}

package frc.robot.commands;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.sensors.WPI_Pigeon2;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private double error;
    private double drivePower;

    private final double CS_BALANCE_GOAL_DEGREES = 0.0;
    private final double CS_BALANCE_DRIVE_KP = 0.015;
    private final double CS_BALANCE_TOLERANCE = 3.0;
    private final double CS_BALANCE_MIN_TIME_LEVEL = 1.0;


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
        SmartDashboard.putNumber("Current Pitch", currentPitch);

        error = CS_BALANCE_GOAL_DEGREES - currentPitch;
        drivePower =  -Math.min(CS_BALANCE_DRIVE_KP * error, 1);

        if(Math.abs(drivePower) > 0.5)
        {
            drivePower = Math.copySign(0.5, drivePower);
        }

        if(drivetrain != null)
        {
            drivetrain.drive(-Math.signum(currentPitch)*0.50, 0.0, 0.0, true);
            
        }
        // System.out.println("Drive Power: " + drivePower);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        System.out.println("End");
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
        if(Math.abs(error) > CS_BALANCE_TOLERANCE)
        {
            balanceState = BalanceState.kNotLevel;
            System.out.println("kNotLevel");
            return false;
        }
        else if(Math.abs(error) <= CS_BALANCE_TOLERANCE && balanceState == BalanceState.kNotLevel)
        {
            finishBalanceTimer.reset();
            finishBalanceTimer.start();
            System.out.println("kLevel");
            System.out.println("Timer Reset");
            balanceState = BalanceState.kLevel;
            return false;
        }
        else if(balanceState == BalanceState.kLevel)
        {
            balanceState = BalanceState.kBeenLevel;
            System.out.println("kBeenLevel");
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

}

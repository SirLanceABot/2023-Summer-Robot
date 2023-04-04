package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Ultrasonic4237;
import frc.robot.commands.StopDrive;


public class DriveToSubstationSensor extends CommandBase
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private enum AlignmentState
    {
        kNotAligned, kAligned, kBeenAligned;
    }

    // *** CLASS AND INSTANCE VARIABLES ***
    private final Drivetrain drivetrain;
    private final Ultrasonic4237 ultrasonic;

    // private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    // NetworkTableEntry ta = table.getEntry("ta");
    // private final Timer alignmentTimer = new Timer();

    // private AlignmentState alignmentState = AlignmentState.kNotAligned;
    /**
     * Creates a new AlignToSubstation
     *
     * @param drivetrain Drivetrain subsystem.
     * @param gyro Gyro sensor.
     * @param ultrasonic Ultrasonic sensor.
     */
    public DriveToSubstationSensor(Drivetrain drivetrain, Ultrasonic4237 ultrasonic) 
    {
        // System.out.println(fullClassName + ": Constructor Started");
        // alignmentState = AlignmentState.kNotAligned;

        this.drivetrain = drivetrain;
        // this.gyro = gyro;
        this.ultrasonic = ultrasonic;
        
        if(this.drivetrain != null)
        {
            addRequirements(drivetrain);
        }
        // if(this.ultrasonic != null)
        // {
        //     addRequirements(ultrasonic);
        // }
            
        
        // System.out.println(fullClassName + ": Constructor Finished");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
         // alignmentTimer.reset();
        // alignmentTimer.start();
        // alignmentState = AlignmentState.kNotAligned;
        // foundTarget = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        
        if( drivetrain != null && ultrasonic != null)
        {
            if(ultrasonic.getDistance() > 2.5)
            {
                drivetrain.drive(1.0, 0.0, 0.0, false);
            }

            else if(ultrasonic.getDistance() <= 2.5 && ultrasonic.getDistance() >= 1.75)
            {
                System.out.println("Under 4");
                drivetrain.drive(0.1, 0.0, 0.0, false);
            }

            else if(ultrasonic.getDistance() < 1.75)
            {
                System.out.println("Under 2.5");
                drivetrain.drive(0.0, 0.0, 0.0, false);
                // drivetrain.stopMotor();
            }
        }
        // if(Math.abs(drivePower) < POST_ALIGNMENT_MIN_SPEED)
        // {
        //     drivePower = Math.copySign(POST_ALIGNMENT_MIN_SPEED, drivePower);
        // }

        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.drive(0.0, 0.0, 0.0, false);
        }   
        System.out.println("End");
    }

    // Returns true when the command should end.

    @Override
    public String toString()
    {
        return "AlignToSubstationUltrasonic(drivetrian, gyro, ultrasonic)";
    }

    
}


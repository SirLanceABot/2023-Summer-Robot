package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.robot.sensors.Ultrasonic4237;


public class NewDriveToSubstation extends CommandBase
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
    private final Ultrasonic4237 ultrasonic;
    private final Drivetrain drivetrain;
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier turn;
    private boolean fieldRelative;
    private double scaleFactor;


    private double distance;
    private boolean crawlLock = false;
    private double cutOffDistance;

    /**
     * Creates a new NewDriveToSubstation.
     */
    public NewDriveToSubstation(Drivetrain drivetrain, Ultrasonic4237 ultrasonic, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier turn, boolean fieldRelative, double scaleFactor) 
    {
        this.drivetrain = drivetrain;
        this.ultrasonic = ultrasonic;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.turn = turn;
        this.fieldRelative = fieldRelative;
        this.scaleFactor = scaleFactor;

        crawlLock = false;

        if(this.drivetrain != null)
        {
            addRequirements(drivetrain);
        }
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        crawlLock = false;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        cutOffDistance = 
        9.5 * (Math.abs(xSpeed.getAsDouble() / 4.0) - 0.5) + 6.0;
        SmartDashboard.putNumber("Cutoff Distance", cutOffDistance);

        if(ultrasonic != null && drivetrain != null)
        {
            distance = ultrasonic.getDistance();

            if(distance > cutOffDistance  && !crawlLock)
            {
                drivetrain.drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), turn.getAsDouble(), fieldRelative);
            }
            else
            {
                crawlLock = true;
                drivetrain.drive(xSpeed.getAsDouble() * scaleFactor, ySpeed.getAsDouble() * scaleFactor, turn.getAsDouble() * scaleFactor, fieldRelative);
            }
        }

        
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        if(drivetrain != null || ultrasonic != null)
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
        return "NewDriveToSubstation(drivetrian, ultrasonic, xSpeed, ySpeed, turn, fieldRelative, scaleFactor)";
    }
}

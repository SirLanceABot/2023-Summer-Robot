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
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Drivetrain;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Vision;


public class DriveToSubstation extends CommandBase
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
    private final Gyro4237 gyro;
    private final Vision vision;

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    NetworkTableEntry ta = table.getEntry("ta");

    private double SUBSTATION_ALIGNMENT_ROTATE_TOLERANCE = 1.0;
    private double POST_ALIGNMENT_ROTATE_KP = 0.1;
    private double DRIVE_KP = 0.03;
    private double POST_ALIGNMENT_MIN_SPEED = 0.04;
    // private final Timer alignmentTimer = new Timer();
    private final Timer limelightTimer = new Timer();

    // private AlignmentState alignmentState = AlignmentState.kNotAligned;
    private double drivePower;
    private double distanceToDrive;
    private double rotatePower;
    private double rotateError;
    private boolean doneDriving = false;
    private boolean doneRotating = false;
    private boolean foundTarget = false;

    /**
     * Creates a new AlignToSubstation
     *
     * @param drivetrain Drivetrain subsystem.
     * @param gyro Gyro sensor.
     * @param vision Vision sensor.
     */
    public DriveToSubstation(Drivetrain drivetrain, Gyro4237 gyro, Vision vision) 
    {
        // System.out.println(fullClassName + ": Constructor Started");
        // alignmentState = AlignmentState.kNotAligned;
        doneDriving = false;
        doneRotating = false;

        SmartDashboard.putNumber("Drive KP", DRIVE_KP);

        this.drivetrain = drivetrain;
        this.gyro = gyro;
        this.vision = vision;
        
        if(this.drivetrain != null)
        {
            addRequirements(drivetrain);
        }
            
        
        // System.out.println(fullClassName + ": Constructor Finished");
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        System.out.println("Initialized");
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);  //april tag pipeline
        // alignmentTimer.reset();
        // alignmentTimer.start();
        limelightTimer.reset();
        limelightTimer.start();
        // alignmentState = AlignmentState.kNotAligned;
        doneDriving = false;
        doneRotating = false;
        // foundTarget = false;
        vision.setIsAligned(false);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        distanceToDrive = vision.getAprilTagDistance();
        rotateError = MathUtil.inputModulus(180.0 - gyro.getYaw(), -180.0, 180.0);
        foundTarget = vision.foundTarget();

        DRIVE_KP = SmartDashboard.getNumber("Drive KP", DRIVE_KP);


        drivePower = -(DRIVE_KP * distanceToDrive);
        rotatePower = POST_ALIGNMENT_ROTATE_KP * rotateError;

        SmartDashboard.putNumber("Drive Power", drivePower);
        SmartDashboard.putNumber("Rotate Power", rotatePower);
        SmartDashboard.putNumber("Distance to Drive", distanceToDrive);
        SmartDashboard.putNumber("Rotate Error", rotateError);
        SmartDashboard.putBoolean("Found Target", foundTarget);

        // if(Math.abs(drivePower) < POST_ALIGNMENT_MIN_SPEED)
        // {
        //     drivePower = Math.copySign(POST_ALIGNMENT_MIN_SPEED, drivePower);
        // }

        if(drivetrain != null)
        {
            if(!doneRotating && foundTarget)
            {
                // drivetrain.drive(0.0, strafePower, rotatePower, false);
                drivetrain.drive(0.0, 0.0, rotatePower, false);
            }
            else if(doneRotating && foundTarget)
            {
                drivetrain.drive(drivePower, 0.0, 0.0, false);
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        if(drivetrain != null)
        {
            drivetrain.resetSlewRateLimiter();
            drivetrain.drive(0.0, 0.0, 0.0, false);
        }   
        System.out.println("End");
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        if(!foundTarget && limelightTimer.hasElapsed(0.2))
        {
            return true;
        }

        if(Math.abs(rotateError) < SUBSTATION_ALIGNMENT_ROTATE_TOLERANCE)
        {
            doneRotating = true;

            if(distanceToDrive > 3.3)
            {
                doneDriving = false;
            }
            else
            {
                doneDriving = true;
            }
        }
        else
        {
            doneRotating = false;
        }

        

        if(doneDriving && doneRotating)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "AlignToSubstation(drivetrian, gyro, vision)";
    }

    public BooleanSupplier foundTarget()
    {
        return ()-> foundTarget;
    }
    
}

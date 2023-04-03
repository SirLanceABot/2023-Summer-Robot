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
import frc.robot.sensors.Vision;


public class AlignToPost extends CommandBase
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

    NetworkTableEntry tx = table.getEntry("tx");

    private double POST_ALIGNMENT_TOLERANCE = 0.50;  //Limelight angle measurement in degrees
    private double POST_ALIGNMENT_ROTATE_TOLERANCE = 1.0;
    private double POST_ALIGNMENT_STRAFE_KP = 0.06;
    private double POST_ALIGNMENT_ROTATE_KP = 0.1;
    private double POST_ALIGNMENT_MIN_SPEED = 0.04;
    private final Timer alignmentTimer = new Timer();
    private final Timer limelightTimer = new Timer();

    private AlignmentState alignmentState = AlignmentState.kNotAligned;
    private double strafeError;
    private double rotateError;
    private double strafePower;
    private double rotatePower;
    private boolean doneStrafing = false;
    private boolean doneRotating = false;
    private boolean foundTarget = false;

    /**
     * Creates a new AutoAimToPost
     *
     * @param drivetrain Drivetrain subsystem.
     * @param gyro Gyro sensor.
     * @param vision Vision sensor.
     */
    public AlignToPost(Drivetrain drivetrain, Gyro4237 gyro, Vision vision) 
    {
        // System.out.println(fullClassName + ": Constructor Started");
        alignmentState = AlignmentState.kNotAligned;
        doneStrafing = false;
        doneRotating = false;

        // SmartDashboard.putNumber("Post Alignemnt Tolerance", POST_ALIGNMENT_TOLERANCE);
        // SmartDashboard.putNumber("Post Alignemnt Rotate Tolerance", POST_ALIGNMENT_ROTATE_TOLERANCE);
        // SmartDashboard.putNumber("Post Alignemnt Strafe KP", POST_ALIGNMENT_STRAFE_KP);
        // SmartDashboard.putNumber("Post Alignemnt Rotate KP", POST_ALIGNMENT_ROTATE_KP);
        // SmartDashboard.putNumber("Post Alignemnt Min Speed", POST_ALIGNMENT_MIN_SPEED);

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
        // System.out.println("Initialized");
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1);  //switches to reflective tape pipeline
        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(3);  //turns limelight on
        alignmentTimer.reset();
        alignmentTimer.start();
        limelightTimer.reset();
        limelightTimer.start();
        alignmentState = AlignmentState.kNotAligned;
        doneStrafing = false;
        doneRotating = false;
        // foundTarget = false;
        vision.setIsAligned(false);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // double xDistance = vision.getx();
        strafeError = vision.getX();
        rotateError = MathUtil.inputModulus(180.0 - gyro.getYaw(), -180.0, 180.0);
        foundTarget = vision.foundTarget();

        // POST_ALIGNMENT_TOLERANCE = SmartDashboard.getNumber("Post Alignemnt Tolerance", POST_ALIGNMENT_TOLERANCE);
        // POST_ALIGNMENT_ROTATE_TOLERANCE = SmartDashboard.getNumber("Post Alignemnt Rotate Tolerance", POST_ALIGNMENT_ROTATE_TOLERANCE);
        // POST_ALIGNMENT_STRAFE_KP = SmartDashboard.getNumber("Post Alignemnt Strafe KP", POST_ALIGNMENT_STRAFE_KP);
        // POST_ALIGNMENT_ROTATE_KP = SmartDashboard.getNumber("Post Alignemnt Rotate KP", POST_ALIGNMENT_ROTATE_KP);
        // POST_ALIGNMENT_MIN_SPEED = SmartDashboard.getNumber("Post Alignemnt Min Speed", POST_ALIGNMENT_MIN_SPEED);

        // System.out.println("Yaw: " + gyro.getYaw() + " Rotate Error: " + rotateError);

        strafePower = -(POST_ALIGNMENT_STRAFE_KP * strafeError);
        rotatePower = POST_ALIGNMENT_ROTATE_KP * rotateError;

        // System.out.println("foundTarget" + foundTarget + "alignmentState: " + alignmentState + " strafeError: " + strafeError);

        if(Math.abs(strafePower) < POST_ALIGNMENT_MIN_SPEED)
        {
            strafePower = Math.copySign(POST_ALIGNMENT_MIN_SPEED, strafePower);
        }

        if(drivetrain != null)
        {
            if(!doneRotating && foundTarget)
            {
                // drivetrain.drive(0.0, strafePower, rotatePower, false);
                drivetrain.drive(0.0, 0.0, rotatePower, false);
            }
            else if(doneRotating && foundTarget)
            {
                drivetrain.drive(0.0, strafePower, 0.0, false);
            }
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        // double xDistance = vision.getX();

        if(drivetrain != null)
        {
            drivetrain.resetSlewRateLimiter();

            drivetrain.drive(0.0, 0.0, 0.0, false);
            // drivetrain.stopMotor();
            // System.out.println("AlignToPost Ended");
        }

        // if(candle != null)
        // {
        //     if(foundTarget)
        //     {
        //         candle.signalGreen();
        //     }
        //     else
        //     {
        //         candle.signalWhite();
        //     }
        // }
        

        // System.out.println("End");

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(0);  //turns limelight off
        // NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);  //turns limelight off

        // System.out.println("End X: " + xDistance);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() 
    {
        // strafeError = vision.getX();
        // rotateError = 180 - gyro.getYaw();
        // foundTarget = vision.foundTarget();

        if(!foundTarget && limelightTimer.hasElapsed(0.2))
        {
            vision.setIsAligned(false);
            return true;
        }

        if(Math.abs(rotateError) < POST_ALIGNMENT_ROTATE_TOLERANCE)
        {
            doneRotating = true;

            if(Math.abs(strafeError) > POST_ALIGNMENT_TOLERANCE)
            {
                alignmentState = AlignmentState.kNotAligned;
            }
            else if(Math.abs(strafeError) <= POST_ALIGNMENT_TOLERANCE && alignmentState == AlignmentState.kNotAligned)
            {
                alignmentTimer.reset();
                alignmentTimer.start();
                alignmentState = AlignmentState.kAligned;
            }
            else if(alignmentState == AlignmentState.kAligned)
            {
                alignmentState = AlignmentState.kBeenAligned;
            }
            else if(!(alignmentState == AlignmentState.kBeenAligned && !alignmentTimer.hasElapsed(0.5)))
            {
                doneStrafing = true;
            }
        }
        else
        {
            doneRotating = false;
        }

        

        if(doneStrafing && doneRotating)
        {
            vision.setIsAligned(true);
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
        return "AlignToPost(drivetrian, gyro, vision)";
    }

    public BooleanSupplier foundTarget()
    {
        return ()-> foundTarget;
    }
    
}

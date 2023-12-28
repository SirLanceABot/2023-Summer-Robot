package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.sensors.Gyro4237;
import frc.robot.sensors.Vision;
import frc.robot.subsystems.Drivetrain;

/**
 * Use this class as a template to create other subsystems.
 */
public class PoseEstimator extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
    
    private final Gyro4237 gyro;
    private final Drivetrain drivetrain;
    private final SwerveDrivePoseEstimator poseEstimator;
    private final Vision vision;

    private Pose3d botPose;
    private DriverStation.Alliance alliance;
    private double totalLatency;

    private class PeriodicIO
    {
        // INPUTS

        // OUTPUTS

    }

    private PeriodicIO periodicIO = new PeriodicIO();

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public PoseEstimator(Drivetrain drivetrain, Gyro4237 gyro, Vision vision)
    {
        this.gyro = gyro;
        this.drivetrain = drivetrain;
        this.vision = vision;

        poseEstimator = new SwerveDrivePoseEstimator(
            drivetrain.kinematics,
            gyro.getRotation2d(),
            drivetrain.getSwerveModulePositions(),
            drivetrain.getPose());
    }


    private String getFomattedPose() 
    {
        var pose = getCurrentPose();
        return String.format("(%.2f, %.2f) %.2f degrees", 
            pose.getX(), 
            pose.getY(),
            pose.getRotation().getDegrees());
    }
    
    public Pose2d getCurrentPose() 
    {
        return poseEstimator.getEstimatedPosition();
    }


    @Override
    public void readPeriodicInputs()
    {}

    @Override
    public void writePeriodicOutputs()
    {}

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run

        //update pose estimator with drivetrain encoders (odometry part)
        poseEstimator.update(gyro.getRotation2d(), drivetrain.getSwerveModulePositions());

        // check the alliance color, get correct pose accordingly
        alliance = DriverStation.getAlliance();
        if(alliance == DriverStation.Alliance.Blue)
        {
            botPose = vision.toPose3d(vision.getBotPoseWPIBlue());
            totalLatency = vision.getBotPoseWPIBlue()[Constants.Vision.totalLatencyIndex];
        }
        else if(alliance == DriverStation.Alliance.Red)
        {
            botPose = vision.toPose3d(vision.getBotPoseWPIRed());
            totalLatency = vision.getBotPoseWPIRed()[Constants.Vision.totalLatencyIndex];
        }

        poseEstimator.addVisionMeasurement(botPose.toPose2d(), Timer.getFPGATimestamp() - (totalLatency / 1000));
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}

package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.sensors.Camera;
import frc.robot.sensors.Gyro4237;


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
    private final Camera cam1;
    private final Camera cam2;
    private final Camera cam3;
    private final Camera cam4;

    // custom network table to make pose readable for AdvantageScope
    // private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable("ASTable");
    
    private class Cam
    {
        private Pose3d pose;
        private double totalLatency;
        private boolean isTargetFound;
    }


    private class PeriodicIO
    {
        // INPUTS
        private Rotation2d gyroRotation;
        private SwerveModulePosition[] swerveModulePositions;
        private DriverStation.Alliance alliance;

        private Cam cam1;
        private Cam cam2;
        private Cam cam3;
        private Cam cam4;

        // OUTPUTS
        private Pose2d estimatedPose;
        private Pose2d poseForAS;

    }

    private PeriodicIO periodicIO = new PeriodicIO();

    /** 
     * Creates a new PoseEstimator. 
     */
    public PoseEstimator(Drivetrain drivetrain, Gyro4237 gyro, Camera cam1, Camera cam2, Camera cam3, Camera cam4)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.gyro = gyro;
        this.drivetrain = drivetrain;
        this.cam1 = cam1;
        this.cam2 = cam2;
        this.cam3 = cam3;
        this.cam4 = cam4;

        if(drivetrain != null && gyro != null)
        {
            poseEstimator = new SwerveDrivePoseEstimator(
                drivetrain.kinematics,
                gyro.getRotation2d(),
                drivetrain.getSwerveModulePositions(),
                drivetrain.getPose());
        }
        else
        {
            poseEstimator = null;
        }

        System.out.println(fullClassName + " : Constructor Finished");
    }
    
    /** @return the estimated pose (Pose2d)*/
    public Pose2d getEstimatedPose() 
    {
        if(poseEstimator != null)
        {
            return periodicIO.estimatedPose;
        }
        else
        {
            return new Pose2d();
        }
        
    }

    @Override
    public void readPeriodicInputs()
    {
        if(drivetrain != null && gyro != null)
        {
            periodicIO.gyroRotation = gyro.getRotation2d();
            periodicIO.swerveModulePositions = drivetrain.getSwerveModulePositions();
        }
        
        periodicIO.alliance = DriverStation.getAlliance();
        
        if(cam1 != null)
        {
            periodicIO.cam1.pose = cam1.getBotPose(periodicIO.alliance);
            periodicIO.cam1.totalLatency = cam1.getTotalLatency(periodicIO.alliance);
            periodicIO.cam1.isTargetFound = cam1.isTargetFound();
        }
    
        
        if(cam2 != null)
        {
            periodicIO.cam2.pose = cam2.getBotPose(periodicIO.alliance);
            periodicIO.cam2.totalLatency = cam2.getTotalLatency(periodicIO.alliance);
            periodicIO.cam2.isTargetFound = cam2.isTargetFound();
        }

        if(cam3 != null)
        {
            periodicIO.cam3.pose = cam3.getBotPose(periodicIO.alliance);
            periodicIO.cam3.totalLatency = cam3.getTotalLatency(periodicIO.alliance);
            periodicIO.cam3.isTargetFound = cam3.isTargetFound();
        }

        if(cam4 != null)
        {
            periodicIO.cam4.pose = cam4.getBotPose(periodicIO.alliance);
            periodicIO.cam4.totalLatency = cam4.getTotalLatency(periodicIO.alliance);
            periodicIO.cam4.isTargetFound = cam4.isTargetFound();
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        if(poseEstimator != null)
        {
            // update pose estimator with drivetrain encoders (odometry part)
            periodicIO.estimatedPose = poseEstimator.update(periodicIO.gyroRotation, periodicIO.swerveModulePositions);

            if(cam1 != null && periodicIO.cam1.isTargetFound)
            {
                // update pose esitmator with limelight data (vision part)
                poseEstimator.addVisionMeasurement(
                    periodicIO.cam1.pose.toPose2d(), 
                    Timer.getFPGATimestamp() - (periodicIO.cam1.totalLatency / 1000));
            }

            if(cam2 != null && periodicIO.cam2.isTargetFound)
            {
                // update pose esitmator with limelight-two data (vision part)
                poseEstimator.addVisionMeasurement(
                    periodicIO.cam2.pose.toPose2d(), 
                    Timer.getFPGATimestamp() - (periodicIO.cam2.totalLatency / 1000));
            }

            if(cam3 != null && periodicIO.cam3.isTargetFound)
            {
                // update pose esitmator with limelight-three data (vision part)
                poseEstimator.addVisionMeasurement(
                    periodicIO.cam3.pose.toPose2d(), 
                    Timer.getFPGATimestamp() - (periodicIO.cam3.totalLatency / 1000));
            }

            if(cam4 != null && periodicIO.cam4.isTargetFound)
            {
                // update pose esitmator with limelight-four data (vision part)
                poseEstimator.addVisionMeasurement(
                    periodicIO.cam4.pose.toPose2d(), 
                    Timer.getFPGATimestamp() - (periodicIO.cam4.totalLatency / 1000));
            }

            periodicIO.estimatedPose = poseEstimator.getEstimatedPosition();
            periodicIO.poseForAS = poseEstimator.getEstimatedPosition(); // variable for testing in AdvantageScope

            // put the pose onto the NT so AdvantageScope can read it
            // ASTable.getEntry("poseEstimator").setDoubleArray(cam1.toQuaternions(periodicIO.poseForAS));
        }
    }

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}

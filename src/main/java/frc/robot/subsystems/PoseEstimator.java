package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;
import java.sql.Driver;

import frc.robot.Constants;

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
import frc.robot.sensors.Vision;
import frc.robot.subsystems.Drivetrain;

// Hi my name is sam

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

    // custom network table to make pose readable for AdvantageScope
    private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable("ASTable"); // custom table for AdvantageScope testing
    
    private class PeriodicIO
    {
        // INPUTS
        private Rotation2d gyroRotation;
        private SwerveModulePosition[] swerveModulePositions;
        private DriverStation.Alliance alliance;

        private Pose3d poseCam1;
        private double totalLatencyCam1;
        private boolean isTargetFoundCam1;

        private Pose3d poseCam2;
        private double totalLatencyCam2;
        private boolean isTargetFoundCam2;

        // OUTPUTS
        private Pose2d estimatedPose;
        private Pose2d poseForAS;

    }

    private PeriodicIO periodicIO = new PeriodicIO();

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public PoseEstimator(Drivetrain drivetrain, Gyro4237 gyro, Camera cam1, Camera cam2)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.gyro = gyro;
        this.drivetrain = drivetrain;
        this.cam1 = cam1;
        this.cam2 = cam2;

        poseEstimator = new SwerveDrivePoseEstimator(
            drivetrain.kinematics,
            gyro.getRotation2d(),
            drivetrain.getSwerveModulePositions(),
            drivetrain.getPose());
        
        System.out.println(fullClassName + " : Constructor Finished");
    }
    
    public Pose2d getEstimatedPose() 
    {
        return poseEstimator.getEstimatedPosition();
    }


    @Override
    public void readPeriodicInputs()
    {
        periodicIO.gyroRotation = gyro.getRotation2d();
        periodicIO.swerveModulePositions = drivetrain.getSwerveModulePositions();
        periodicIO.alliance = DriverStation.getAlliance();
        
        periodicIO.poseCam1 = cam1.toPose3d(cam1.getBotPose(periodicIO.alliance));
        periodicIO.totalLatencyCam1 = cam1.getTotalLatency(periodicIO.alliance);
        periodicIO.isTargetFoundCam1 = cam1.isTargetFound();

        periodicIO.poseCam2 = cam2.toPose3d(cam2.getBotPose(periodicIO.alliance));
        periodicIO.totalLatencyCam2 = cam2.getTotalLatency(periodicIO.alliance);
        periodicIO.isTargetFoundCam2 = cam2.isTargetFound();
    }

    @Override
    public void writePeriodicOutputs()
    {
        // update pose estimator with drivetrain encoders (odometry part)
        periodicIO.estimatedPose = poseEstimator.update(periodicIO.gyroRotation, periodicIO.swerveModulePositions);
       
        // camera one
        if(periodicIO.isTargetFoundCam1)
        {
            // update pose esitmator with vision pose
            poseEstimator.addVisionMeasurement(
                periodicIO.poseCam1.toPose2d(), 
                Timer.getFPGATimestamp() - (periodicIO.totalLatencyCam1 / 1000));
        }

        // camera two
        if(periodicIO.isTargetFoundCam2)
        {
            // update pose esitmator with vision pose
            poseEstimator.addVisionMeasurement(
                periodicIO.poseCam2.toPose2d(), 
                Timer.getFPGATimestamp() - (periodicIO.totalLatencyCam2 / 1000));
        }

        periodicIO.estimatedPose = poseEstimator.getEstimatedPosition();
        periodicIO.poseForAS = poseEstimator.getEstimatedPosition(); // variable for testing in AdvantageScope

        // put the pose onto the NT so AdvantageScope can read it
        ASTable.getEntry("poseEstimator").setDoubleArray(cam1.toQuaternions(periodicIO.poseForAS));
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

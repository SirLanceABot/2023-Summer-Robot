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

    // custom network table to make pose readable for AdvantageScope
    private NetworkTable tagsTable = NetworkTableInstance.getDefault().getTable("apriltagsLL");

    private Pose3d botPose;
    private DriverStation.Alliance alliance;
    private double totalLatency;
    private Pose2d poseForAS;

    private class PeriodicIO
    {
        // INPUTS
        private Rotation2d gyroRotation;
        private SwerveModulePosition[] swerveModulePositions;
        private DriverStation.Alliance alliance;
        private Pose3d limelightPoseBlue;
        private Pose3d limelightPoseRed;
        private double totalLatencyBlue;
        private double totalLatencyRed;
        private boolean isTargetFound;

        // OUTPUTS
        private Pose2d estimatedPose;
        private Pose2d poseForAS;

    }

    private PeriodicIO periodicIO = new PeriodicIO();

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public PoseEstimator(Drivetrain drivetrain, Gyro4237 gyro, Vision vision)
    {
        System.out.println(fullClassName + " : Constructor Started");

        this.gyro = gyro;
        this.drivetrain = drivetrain;
        this.vision = vision;

        poseEstimator = new SwerveDrivePoseEstimator(
            drivetrain.kinematics,
            gyro.getRotation2d(),
            drivetrain.getSwerveModulePositions(),
            drivetrain.getPose());
        
        System.out.println(fullClassName + " : Constructor Finished");
    }


    // makes the pose easy to read
    private String getFomattedPose() 
    {
        var pose = getEstimatedPose();
        return String.format("(%.2f, %.2f) %.2f degrees", 
            pose.getX(), 
            pose.getY(),
            pose.getRotation().getDegrees());
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
        periodicIO.limelightPoseBlue = vision.toPose3d(vision.getBotPoseWPIBlue());
        periodicIO.limelightPoseRed = vision.toPose3d(vision.getBotPoseWPIRed());
        periodicIO.totalLatencyBlue = vision.getTotalLatencyBlue();
        periodicIO.totalLatencyRed = vision.getTotalLatencyRed();
        periodicIO.isTargetFound = vision.isTargetFound();
    }

    @Override
    public void writePeriodicOutputs()
    {
        // update pose estimator with drivetrain encoders (odometry part)
        periodicIO.estimatedPose = poseEstimator.update(periodicIO.gyroRotation, periodicIO.swerveModulePositions);
       
        // check alliance color, get vision pose and total latency accordingly
        if(periodicIO.alliance == DriverStation.Alliance.Blue && periodicIO.isTargetFound)
        {
            // update pose esitmator with vision pose
            poseEstimator.addVisionMeasurement(
                periodicIO.limelightPoseBlue.toPose2d(), 
                Timer.getFPGATimestamp() - (periodicIO.totalLatencyBlue / 1000));
        }
        else if(periodicIO.alliance == DriverStation.Alliance.Red && periodicIO.isTargetFound)
        {
            poseEstimator.addVisionMeasurement(
                periodicIO.limelightPoseRed.toPose2d(),
                Timer.getFPGATimestamp() - (periodicIO.totalLatencyRed / 1000));
        }

        periodicIO.estimatedPose = poseEstimator.getEstimatedPosition();
        periodicIO.poseForAS = poseEstimator.getEstimatedPosition(); // variable for testing in AdvantageScope

        tagsTable       // put the pose onto the NT so AdvantageScope can read it
        .getEntry("poseEstimator")
        .setDoubleArray(
            new double[] {
                periodicIO.poseForAS.getTranslation().getX(), periodicIO.poseForAS.getTranslation().getY(),
                periodicIO.poseForAS.getRotation().getRadians()
            });
    }

    @Override
    public void periodic()
    {
        // // This method will be called once per scheduler run

        // //update pose estimator with drivetrain encoders (odometry part)
        // poseEstimator.update(gyro.getRotation2d(), drivetrain.getSwerveModulePositions());

        // // check the alliance color, get correct pose accordingly
        // alliance = DriverStation.getAlliance();

        // if(alliance == DriverStation.Alliance.Blue)
        // {
        //     botPose = vision.toPose3d(vision.getBotPoseWPIBlue());
        //     totalLatency = vision.getBotPoseWPIBlue()[Constants.Vision.totalLatencyIndex];  //total latency is the 7th number in the botPose double array
        // }
        // else if(alliance == DriverStation.Alliance.Red)
        // {
        //     botPose = vision.toPose3d(vision.getBotPoseWPIRed());
        //     totalLatency = vision.getBotPoseWPIRed()[Constants.Vision.totalLatencyIndex];   //total latency is the 7th number in the botPose double array
        // }

        // if(vision.isTargetFound())    // if the LL can see a tag, update the PoseEstimator with those measurements
        // {
        //     poseEstimator.addVisionMeasurement(botPose.toPose2d(), Timer.getFPGATimestamp() - (totalLatency / 1000));
        // }

        // poseForAS = getEstimatedPose();   // variable for testing in AdvantageScope


        // // put the pose onto the Network Table so AdvantageScope can read it
        // tagsTable
        // .getEntry("poseEstimator-robotpose")
        // .setDoubleArray(
        //     new double[] {
        //         poseForAS.getTranslation().getX(), poseForAS.getTranslation().getY(),
        //         poseForAS.getRotation().getRadians()
        //     });
    }

    @Override
    public void simulationPeriodic()
    {
        // This method will be called once per scheduler run during simulation
    }
}

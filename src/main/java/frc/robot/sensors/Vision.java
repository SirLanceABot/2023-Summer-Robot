package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.Constants.Sensor;

public class Vision extends Sensor4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public class PeriodicIO
    {
        //INPUTS
        
        // Entry variables named with LL convention (not camelcase)
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry tv = table.getEntry("tv");
        NetworkTableEntry botpose = table.getEntry("botpose");
        NetworkTableEntry botpose_wpiblue = table.getEntry("botpose_wpiblue");
        NetworkTableEntry botpose_wpired = table.getEntry("botpose_wpired");

        // Our class variables named with our convention (yes camelcase)
        private double x;
        private double y;
        private double area;
        private boolean foundTarget;
        private double[] botPose;
        private double[] botPoseWPIBlue;
        private double[] botPoseWPIRed;
    }

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");   // official limelight camera table
    private NetworkTable tagsTable = NetworkTableInstance.getDefault().getTable("apriltagsLL"); // custom table for AdvantageScope testing
    private PeriodicIO periodicIO;
    private boolean isAligned;
    private Pose3d poseForAS;
    private final double A = 4.8940189;
    private final double B = -0.5459146235;

    public Vision()
    {   
        System.out.println(fullClassName + " : Constructor Started");

        periodicIO = new PeriodicIO();

        System.out.println(fullClassName + " : Constructor Finished");
    }


    /** @return the x distance from the center of the target (double) */
    public double getX()
    {
        return periodicIO.x;
    }

    /** @return the y distance from the center of the target (double) */
    public double getY()
    {
        return periodicIO.y;
    }

    /** @return the area of view that the target takes up (double) */
    public double getArea()
    {
        return periodicIO.area;
    }

    /** @return the distance in FEET from the limelight to the target (double) */
    public double getAprilTagDistance()
    {
        return A * Math.pow(periodicIO.area, B);
    }

    /** @return false if no target is found, true if target is found */
    public boolean isTargetFound()
    {
        return periodicIO.foundTarget;
    }

    public boolean isAligned()
    {
        return isAligned;
    }

    public void setIsAligned(boolean isAligned)
    {
        this.isAligned = isAligned;
    }

    // converts the double array from NT into Pose3d
    public Pose3d toPose3d(double[] array)
    {
        return new Pose3d(
            new Translation3d(
                array[Constants.Vision.translationXMetersIndex],
                array[Constants.Vision.translationYMetersIndex],
                array[Constants.Vision.translationZMetersIndex]
                ),
            new Rotation3d(
                Units.degreesToRadians(array[Constants.Vision.rotationRollDegreesIndex]),
                Units.degreesToRadians(array[Constants.Vision.rotationPitchDegreesIndex]),
                Units.degreesToRadians(array[Constants.Vision.rotationYawDegreesIndex])
                )
        );
    }

    /** @return the robot pose on the field (double[])*/
    public double[] getBotPose()
    {
        return periodicIO.botPose;
    }

    /** @return the robot pose on the field (double[]) blue driverstration origin*/
    public double[] getBotPoseWPIBlue()
    {
        return periodicIO.botPoseWPIBlue;
    }

    
    /** @return the robot pose on the field (double[]) red driverstration origin*/
    public double[] getBotPoseWPIRed()
    {
        return periodicIO.botPoseWPIRed;
    }

    /** @return the total latency from botpose measurements (double)*/
    public double getTotalLatency()
    {
        return periodicIO.botPose[Constants.Vision.totalLatencyIndex];
    }

    /** @return the total latency from WPIBlue botpose measurements (double)*/
    public double getTotalLatencyBlue()
    {
        return periodicIO.botPoseWPIBlue[Constants.Vision.totalLatencyIndex];
    }

    /** @return the total latency from WPIRed botpose measurements (double)*/
    public double getTotalLatencyRed()
    {
        return periodicIO.botPoseWPIRed[Constants.Vision.totalLatencyIndex];
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicIO.x = periodicIO.tx.getDouble(0.0);
        periodicIO.y = periodicIO.ty.getDouble(0.0);
        periodicIO.area = periodicIO.ta.getDouble(0.0);
        periodicIO.foundTarget = periodicIO.tv.getDouble(0.0) == 1.0;
        periodicIO.botPose = periodicIO.botpose.getDoubleArray(new double[7]);
        periodicIO.botPoseWPIBlue = periodicIO.botpose_wpiblue.getDoubleArray(new double[7]);
        periodicIO.botPoseWPIRed = periodicIO.botpose_wpired.getDoubleArray(new double[7]);
    }

    @Override
    public void writePeriodicOutputs() 
    {
        // SmartDashboard.putNumber("LimelightX", periodicIO.x);
        // SmartDashboard.putNumber("LimelightY", periodicIO.y);
        // SmartDashboard.putNumber("LimelightArea", periodicIO.area);
        // SmartDashboard.putBoolean("LimelightFoundTarget", periodicIO.foundTarget);
        // SmartDashboard.putNumberArray("LimelightBotPose", periodicIO.botPose);

        poseForAS = toPose3d(periodicIO.botPoseWPIBlue);    // variable for testing in AdvantageScope

        // put the pose from LL onto the Network Table so AdvantageScope can read it
        tagsTable
        .getEntry("robotpose")
        .setDoubleArray(
            new double[] {
                poseForAS.getTranslation().getX(), poseForAS.getTranslation().getY(), poseForAS.getTranslation().getZ(),
                poseForAS.getRotation().getQuaternion().getW(), poseForAS.getRotation().getQuaternion().getX(),
                poseForAS.getRotation().getQuaternion().getY(), poseForAS.getRotation().getQuaternion().getZ()
            });
    }
}

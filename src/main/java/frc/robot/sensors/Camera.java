package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import frc.robot.Constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;

public class Camera extends Sensor4237
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
        NetworkTableEntry tv;
        NetworkTableEntry botpose_wpiblue;
        NetworkTableEntry botpose_wpired;

        // Our class variables named with our convention (yes camelcase)
        private boolean isTargetFound;
        private double[] botPoseWPIBlue;
        private double[] botPoseWPIRed;
    }

    private PeriodicIO periodicIO;
    private Pose3d poseForAS;
    private NetworkTable cameraTable;

    private NetworkTable ASTable = NetworkTableInstance.getDefault().getTable("ASTable"); // custom table for AdvantageScope testing


    public Camera(String camName)
    {   
        System.out.println(fullClassName + " (" + camName + ")" + " : Constructor Started");

        periodicIO = new PeriodicIO();

        // Assign the Network Table variable in the constructor so the camName parameter can be used
        cameraTable = NetworkTableInstance.getDefault().getTable(camName);   // official limelight table

        periodicIO.tv = cameraTable.getEntry("tv");
        periodicIO.botpose_wpiblue = cameraTable.getEntry("botpose_wpiblue");
        periodicIO.botpose_wpired = cameraTable.getEntry("botpose_wpired");


        System.out.println(fullClassName + " (" + camName + ")" + " : Constructor Finished");
    }

    /** @return false if no target is found, true if target is found */
    public boolean isTargetFound()
    {
        return periodicIO.isTargetFound;
    }

    // converts the double array from NT into Pose3d
    public Pose3d toPose3d(double[] poseArray)
    {
        return new Pose3d(
            new Translation3d(
                poseArray[Constants.Vision.translationXMetersIndex],
                poseArray[Constants.Vision.translationYMetersIndex],
                poseArray[Constants.Vision.translationZMetersIndex]
                ),
            new Rotation3d(
                Units.degreesToRadians(poseArray[Constants.Vision.rotationRollDegreesIndex]),
                Units.degreesToRadians(poseArray[Constants.Vision.rotationPitchDegreesIndex]),
                Units.degreesToRadians(poseArray[Constants.Vision.rotationYawDegreesIndex])
                )
        );
    }

    // converts Pose3d to Quaternions for AdvantageScope usage
    public double[] toQuaternions(Pose3d pose)
    {
        return new double[] {
            pose.getTranslation().getX(), pose.getTranslation().getY(), pose.getTranslation().getZ(),
            pose.getRotation().getQuaternion().getW(), pose.getRotation().getQuaternion().getX(),
            pose.getRotation().getQuaternion().getY(), pose.getRotation().getQuaternion().getZ()
        };
    }

    // converts Pose2d to Quaternions for AdvantageScope usage
    public double[] toQuaternions(Pose2d pose)
    {
        return new double[] {
            pose.getTranslation().getX(), pose.getTranslation().getY(),
            pose.getRotation().getRadians()
        };
    }

    /** @return the robot pose on the field (double[]) blue driverstration origin*/
    public Pose3d getBotPoseWPIBlue()
    {
        return toPose3d(periodicIO.botPoseWPIBlue);
    }

    /** @return the robot pose on the field (double[]) red driverstration origin*/
    public Pose3d getBotPoseWPIRed()
    {
        return toPose3d(periodicIO.botPoseWPIRed);
    }

    /** @return the robot pose on the field (double[]) red driverstration origin*/
    /** @param allianceColor the allaince color */
    public Pose3d getBotPose(DriverStation.Alliance allianceColor)
    {
        if(allianceColor == DriverStation.Alliance.Red)
        {
            return toPose3d(periodicIO.botPoseWPIRed);
        }
        else if(allianceColor == DriverStation.Alliance.Blue)
        {
            return toPose3d(periodicIO.botPoseWPIBlue);
        }
        else
        {
            return null;
        }
            
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

    /** @return the total latency from botpose measurements (double)*/
    /** @param allianceColor the allaince color */
    public double getTotalLatency(DriverStation.Alliance allianceColor)
    {
        if(allianceColor == DriverStation.Alliance.Red)
        {
            return periodicIO.botPoseWPIRed[Constants.Vision.totalLatencyIndex];
        }
        else if(allianceColor == DriverStation.Alliance.Blue)
        {
            return periodicIO.botPoseWPIBlue[Constants.Vision.totalLatencyIndex];
        }
        else
        {
            return 0.0;
        }
            
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicIO.isTargetFound = periodicIO.tv.getDouble(0.0) == 1.0;
        periodicIO.botPoseWPIBlue = periodicIO.botpose_wpiblue.getDoubleArray(new double[7]);
        periodicIO.botPoseWPIRed = periodicIO.botpose_wpired.getDoubleArray(new double[7]);
    }

    @Override
    public void writePeriodicOutputs() 
    {
        poseForAS = toPose3d(periodicIO.botPoseWPIBlue);    // variable for testing in AdvantageScope

        // put the pose from LL onto the Network Table so AdvantageScope can read it
        ASTable.getEntry("robotpose").setDoubleArray(toQuaternions(poseForAS));
    }
}

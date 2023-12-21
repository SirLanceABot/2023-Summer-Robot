package frc.robot.sensors;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
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
        NetworkTableEntry tx = table.getEntry("tx");
        NetworkTableEntry ty = table.getEntry("ty");
        NetworkTableEntry ta = table.getEntry("ta");
        NetworkTableEntry tv = table.getEntry("tv");
        NetworkTableEntry botpose = table.getEntry("botpose");
        private double x;
        private double y;
        private double area;
        private boolean foundTarget;
        private double[] botPose;
    }

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private PeriodicIO periodicIO;
    private boolean isAligned;
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
    public boolean foundTarget()
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

    /** @return the robot pose on the field (double[])*/
    public Pose3d getBotPose()
    {
        return new Pose3d(
            periodicIO.botPose[0],
            periodicIO.botPose[1],
            periodicIO.botPose[2],
            new Rotation3d(
                periodicIO.botPose[3],
                periodicIO.botPose[4],
                periodicIO.botPose[5]
            )
        );

        // return periodicIO.botPose;
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicIO.x = periodicIO.tx.getDouble(0.0);
        periodicIO.y = periodicIO.ty.getDouble(0.0);
        periodicIO.area = periodicIO.ta.getDouble(0.0);
        periodicIO.foundTarget = periodicIO.tv.getDouble(0.0) == 1.0 ? true : false;
        periodicIO.botPose = periodicIO.botpose.getDoubleArray(new double[6]);
    }

    @Override
    public void writePeriodicOutputs() 
    {
        // SmartDashboard.putNumber("LimelightX", periodicIO.x);
        // SmartDashboard.putNumber("LimelightY", periodicIO.y);
        // SmartDashboard.putNumber("LimelightArea", periodicIO.area);
        // SmartDashboard.putBoolean("LimelightFoundTarget", periodicIO.foundTarget);
    }
    
}

package frc.robot.vision;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PeriodicIO;

public class Vision implements PeriodicIO
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
        private double x;
        private double y;
        private double area;
    }

    private NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    private PeriodicIO periodicIO;

    public Vision()
    {   
        registerPeriodicIO();
        periodicIO = new PeriodicIO();
    }

    public double getX()
    {
        return periodicIO.x;
    }

    public double getY()
    {
        return periodicIO.y;
    }

    public double getArea()
    {
        return periodicIO.area;
    }

    @Override
    public void readPeriodicInputs() 
    {
        periodicIO.x = periodicIO.tx.getDouble(0.0);
        periodicIO.y = periodicIO.ty.getDouble(0.0);
        periodicIO.area = periodicIO.ta.getDouble(0.0);
    }

    @Override
    public void writePeriodicOutputs() 
    {
        SmartDashboard.putNumber("LimelightX", periodicIO.x);
        SmartDashboard.putNumber("LimelightY", periodicIO.y);
        SmartDashboard.putNumber("LimelightArea", periodicIO.area);
    }
}


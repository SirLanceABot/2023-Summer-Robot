package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.RobotContainer;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Arm;

public class SensorTab
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    private ShuffleboardTab sensorTab = Shuffleboard.getTab("Sensor");

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS CONSTRUCTOR ***
    public SensorTab()
    {
        System.out.println(fullClassName + " : Constructor Started");
        // this.operatorController = operatorController;

        // initOperatorControllerTab();
        createArmSensorBox();
        createGrabberSensorBox();
        createDrivetrainSensorBox();
        createShoulderSensorBox();


        System.out.println(fullClassName + ": Constructor Finished");
    }

    private GenericEntry createTextBox(String title, String defaultValue, int column, int row, int width, int height)
    {
        return sensorTab.add(title, defaultValue)
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(column, row)  // sets position of widget
        .withSize(width, height)    // sets size of widget
        .getEntry();
    }

    private void createArmSensorBox()
    {
        SmartDashboard.putNumber("Arm Encoder", 5);
    }

    private void createGrabberSensorBox()
    {

    }

    private void createDrivetrainSensorBox()
    {

    }

    private void createShoulderSensorBox()
    {
        // SmartDashboard.putNumber("Arm Encoder", Shoulder.getPosition());
    }
}
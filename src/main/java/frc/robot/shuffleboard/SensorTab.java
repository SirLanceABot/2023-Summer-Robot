package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.datalog.DoubleLogEntry;
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
// import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.SwerveModule;

public class SensorTab
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    private ShuffleboardTab sensorTab = Shuffleboard.getTab("Sensor");
    private Shoulder shoulder;
    private Grabber grabber;
    private Arm arm;
    private Drivetrain drivetrain;
    private SwerveModule swerveModule;
    private Translation2d startingPosition;

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS CONSTRUCTOR ***
    public SensorTab(Shoulder shoulder, Grabber grabber, Arm arm, Drivetrain drivetrain)
    {
        System.out.println(fullClassName + " : Constructor Started");
        // this.operatorController = operatorController;
        this.shoulder = shoulder;
        this.grabber = grabber;
        this.arm = arm;
        this.swerveModule = swerveModule;
        this.drivetrain = drivetrain;

        // initOperatorControllerTab();
        createSensorBox();


        System.out.println(fullClassName + ": Constructor Finished");
    }

    private GenericEntry createTextBox(String title, double defaultValue, int column, int row, int width, int height)
    {
        return sensorTab.add(title, defaultValue)
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(column, row)  // sets position of widget
        .withSize(width, height)    // sets size of widget
        .getEntry();
    }

    public void createSensorBox()
    {
        createTextBox("Shoulder Encoder", shoulder.getPosition(), 0, 0, 4, 4);
        createTextBox("Grabber Enocder", grabber.getGrabberEncoder(), 5, 0, 4, 4);
        createTextBox("Arm Encoder", arm.getArmPosition(), 10, 0, 4, 4);
        // createTextBox("Front Left Spin", drivetrain.flsLog(), 0, 5, 4, 4);
        // createTextBox("Front Left Drive", drivetrain.fldLog(), 0, 10, 4, 4);
        // createTextBox("Front Right Spin", drivetrain.frsLog(), 5, 5, 4, 4);
        // createTextBox("Front Right Drive", drivetrain.frdLog(), 5, 10, 4, 4);
        // createTextBox("Back Left Spin", drivetrain.blsLog(), 10, 5, 4, 4);
        // createTextBox("Back Left Drive", drivetrain.bldLog(), 10, 10, 4, 4);
        // createTextBox("Back Right Spin", drivetrain.brsLog(), 15, 5, 4, 4);
        // createTextBox("Back Right Drive", drivetrain.flsLog(), 15, 10, 4, 4);


    }
}
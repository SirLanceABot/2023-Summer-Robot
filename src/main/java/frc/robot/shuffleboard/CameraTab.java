package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;

public class CameraTab 
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Create a Shuffleboard Tab
    private ShuffleboardTab cameraTab = Shuffleboard.getTab("Camera");
    private Shoulder shoulder;
    private Arm arm;
    private Grabber grabber;
    
    private int oldTime = 0;

    // Create text output boxes
    private NetworkTableEntry timeRemaining;
    private GenericEntry shoulderEncoderBox;
    private GenericEntry armEncoderBox;
    // private GenericEntry grabberMotorBottomCurrentBox;
    // private GenericEntry grabberMotorTopCurrentBox;
    // private GenericEntry grabberBottomDigitalInputBox;
    // private GenericEntry grabberTopDigitalInputBox;
    private GenericEntry topVacuumPressureBox;
    private GenericEntry bottomVacuumPressureBox;



    private Double timeRemainingData = 0.0;
    String compressorStateString = "No data";

    // *** CLASS CONSTRUCTOR ***
    CameraTab(Shoulder shoulder, Arm arm, Grabber grabber)
    {
        System.out.println(fullClassName + " : Constructor Started");

        // // limelight on shuffleboard
        CameraWidget cw = new CameraWidget(cameraTab);
        cw.name("Limelight");
        cw.setLocation(0, 0, 16, 20); // small screen
        cw.setProperties(false, "white", false, "NONE");

        cw.createCameraShuffleboardWidgetLL("limelight-", new String[]{"http://10.42.37.11:5800"}); // could get URLs from NT

        // createTimeRemainingBox();

        this.shoulder = shoulder;
        this.arm = arm;
        this.grabber = grabber;

        if(shoulder != null)
        {
            shoulderEncoderBox = createShoulderEncoderBox();
        }
        if(grabber != null)
        {
            // grabberMotorBottomCurrentBox = createGrabberBottomCurrentBox();
            // grabberMotorTopCurrentBox = createGrabberTopCurrentBox();
            //grabberBottomDigitalInputBox = createGrabberBottomDigitalInputBox();
            //grabberTopDigitalInputBox = createGrabberTopDigitalInputBox();
            topVacuumPressureBox = createTopVacuumPressureBox();
            bottomVacuumPressureBox = createBottomVacuumPressureBox();
        }
        if(arm != null)
        {
            armEncoderBox = createArmEncoderBox();
        }

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***

    private GenericEntry createShoulderEncoderBox()
    {
        return cameraTab.add("Shoulder Encoder", shoulder.getPosition())
        .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
        .withPosition(24, 0)  // sets position of widget
        .withSize(3, 2)    // sets size of widget
        .getEntry();
    }

    // private GenericEntry createGrabberBottomCurrentBox()
    // {
    //     return cameraTab.add("Grabber Motor Bottom Current", grabber.getVacuumBottomCurrent())
    //     .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
    //     .withPosition(20, 0)  // sets position of widget
    //     .withSize(4, 2)    // sets size of widget
    //     .getEntry();
    // }

    // private GenericEntry createGrabberTopCurrentBox()
    // {
    //     return cameraTab.add("Grabber Motor Top Current", grabber.getVacuumTopCurrent())
    //     .withWidget(BuiltInWidgets.kTextView)   // specifies type of widget: "kTextView"
    //     .withPosition(20, 3)  // sets position of widget
    //     .withSize(4, 2)    // sets size of widget
    //     .getEntry();
    // }

    // private GenericEntry createGrabberBottomDigitalInputBox()
    // {
    //     return cameraTab.add("Grabber Bottom Digital Input", grabber.getBottomDigitalInput())
    //     .withWidget(BuiltInWidgets.kBooleanBox)   // specifies type of widget: "kTextView"
    //     .withPosition(20, 6)  // sets position of widget
    //     .withSize(4, 2)    // sets size of widget
    //     .getEntry();
    // }

    // private GenericEntry createGrabberTopDigitalInputBox()
    // {
    //     return cameraTab.add("Grabber Top Digital Input", grabber.getTopDigitalInput())
    //     .withWidget(BuiltInWidgets.kBooleanBox)   // specifies type of widget: "kTextView"
    //     .withPosition(20, 9)  // sets position of widget
    //     .withSize(4, 2)    // sets size of widget
    //     .getEntry();
    // }

    private GenericEntry createTopVacuumPressureBox()
    {
        return cameraTab.add("Top Vacuum Pressure", grabber.getAnalogSensorVoltageTop())
        .withWidget(BuiltInWidgets.kTextView)
        .withPosition(20, 5)
        .withSize(3, 2)
        .getEntry();
    }

    private GenericEntry createBottomVacuumPressureBox()
    {
        return cameraTab.add("Bottom Vacuum Pressure", grabber.getAnalogSensorVoltageBottom())
        .withWidget(BuiltInWidgets.kTextView)
        .withPosition(24, 5)
        .withSize(3, 2)
        .getEntry();
    }

    private GenericEntry createArmEncoderBox()
    {
        return cameraTab.add("Arm Endocer", arm.getArmPosition())
        .withWidget(BuiltInWidgets.kTextView)
        .withPosition(20, 0)
        .withSize(3, 2)
        .getEntry();
    }

    public void updateEncoderData()
    {
        if(shoulder != null)
        {
            shoulderEncoderBox.setDouble(shoulder.getPosition());
        }
        if(grabber != null)
        {
            // grabberMotorBottomCurrentBox.setDouble(grabber.getVacuumBottomCurrent());
            // grabberMotorTopCurrentBox.setDouble(grabber.getVacuumTopCurrent());
            // grabberBottomDigitalInputBox.setBoolean(grabber.getBottomDigitalInput());
            // grabberTopDigitalInputBox.setBoolean(grabber.getTopDigitalInput());
            topVacuumPressureBox.setDouble(grabber.convertVoltageToPsi(grabber.getAnalogSensorVoltageTop()));
            bottomVacuumPressureBox.setDouble(grabber.convertVoltageToPsi(grabber.getAnalogSensorVoltageBottom()));
        }
        if(arm != null)
        {
            armEncoderBox.setDouble(arm.getArmPosition());
        }
    }

    // private void createTimeRemainingBox()
    // {
    //     cameraTab.add("Time Remaining", timeRemainingData.toString())
    //         .withWidget(BuiltInWidgets.kTextView)
    //         .withPosition(24, 5)
    //         .withSize(4, 2)
    //         .getEntry();
    // }

    public void updateTimeRemaining()
    {
        timeRemainingData = Timer.getMatchTime();
        int timeRemainingInt = timeRemainingData.intValue();

        if (timeRemainingInt == -1)
        {
            timeRemaining.setString("0");
        }
        else if (timeRemainingInt != oldTime)
        {
            timeRemaining.setString("" + timeRemainingInt);
            oldTime = timeRemainingInt;
        }
    }

    /**
     * This method updates the LimeLight to set how images are seen
     * and set the LEDs
     * 
     * We want to be in targeting vision processing mode in Autonomous or not Intake.
     * That is, driver mode is not autonomous and intaking (taking in).
     */

    public void updateLimeLightMode()
    {
        // boolean driverMode = (RobotContainer.INTAKE != null) && (!DriverStation.isAutonomous()) ?
        //     RobotContainer.INTAKE.getIsIntaking() : false;
        boolean isDisabled = DriverStation.isDisabled();
        
        // driverMode = true; // testing force driver mode

        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        NetworkTableEntry camMode = table.getEntry("camMode");
        NetworkTableEntry stream = table.getEntry("stream");
        NetworkTableEntry ledMode = table.getEntry("ledMode");

        // if(true)
        // {
        //     camMode.setNumber(0.); // 0 vision processor
        //     stream.setNumber(0.);  // 0 for both large side by side
        //     ledMode.setNumber(0.); // 0 pipeline setting
        // }
        // else

        if(isDisabled)
        {
            ledMode.setNumber(1.); // led off
        }
        // else if(driverMode)
        {
            camMode.setNumber(1.); // 1 driver
            stream.setNumber(2.);  // 2 driver intake with small target; 0 for both large side by side
            ledMode.setNumber(1.); // 1 off; 0 pipeline setting
        }
        // else
        {
            camMode.setNumber(0.); // 0 vision processor
            stream.setNumber(1.);  // 1 target with small driver; 0 for both large side by side
            ledMode.setNumber(0.); // 0 pipeline setting
        }
    }

    // This method will be run on a slow period - say 1 second
    // LimeLight can't take it any faster and humans don't need it fast, either.
    public void updateCameraTab()
    {
        updateTimeRemaining();
        updateLimeLightMode();
    }
}
/*
ledMode	Sets limelight’s LED state
0	use the LED Mode set in the current pipeline
1	force off
2	force blink
3	force on

camMode	Sets limelight’s operation mode
0	Vision processor
1	Driver Camera (Increases exposure, disables vision processing)

pipeline	Sets limelight’s current pipeline
0 .. 9	Select pipeline 0..9

stream	Sets limelight’s streaming mode
0	Standard - Side-by-side streams if a webcam is attached to Limelight
1	PiP Main - The secondary camera stream is placed in the lower-right corner of the primary camera stream
2	PiP Secondary - The primary camera stream is placed in the lower-right corner of the secondary camera stream

snapshot	Allows users to take snapshots during a match
0	Stop taking snapshots
1	Take two snapshots per second

*/

//     boolean shooterMode = RobotContainer.SHOOTER.getIsShooting();

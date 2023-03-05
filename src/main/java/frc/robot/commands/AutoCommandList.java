package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.TargetPosition;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.AutonomousTabData;
import frc.robot.shuffleboard.AutonomousTabData.StartingLocation;
// import frc.robot.shuffleboard.AutonomousTabData.TeamColor;
import frc.robot.shuffleboard.AutonomousTabData.PlayPreload;
import frc.robot.shuffleboard.AutonomousTabData.MoveOntoChargingStation;
import frc.robot.shuffleboard.AutonomousTabData.PickUpGamePieces;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece1;
import frc.robot.shuffleboard.AutonomousTabData.AutonomousCommands;
import frc.robot.shuffleboard.AutonomousTabData.RowPlayedPiece2;
import frc.robot.shuffleboard.AutonomousTabData.ScoreSecondPiece;
import frc.robot.shuffleboard.AutonomousTabData.ContainingPreload;
import frc.robot.shuffleboard.AutonomousTabData.DriveToSecondPiece;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Arm;
// import frc.robot.subsystems.Arm.TargetPosition;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Shoulder;
// import frc.robot.subsystems.Shoulder.TargetPosition;

public class AutoCommandList extends SequentialCommandGroup
{
    //This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }


    // *** INNER ENUMS and INNER CLASSES ***
    private static enum CommandState
    {
        kInit, kExecute, kEnd, kAllDone;
    }

    // *** CLASS & INSTANCE VARIABLES ***
    private AutonomousTabData autonomousTabData;
    // private final Autonomous1 autonomous1;
    private final Drivetrain drivetrain;
    private final Gyro4237 gyro;
    private final Grabber grabber;
    private final Shoulder shoulder;
    private final Arm arm;
    private final Wrist wrist;
    private String commandString = "\n***** AUTONOMOUS COMMAND LIST *****\n";
    // private final Command currentCommand;
    
    

    // private static final ArrayList<Command> commandList = new ArrayList<>();
 
    // private  teamColor;
    // private static startingLocation;
    
    // *** CLASS CONSTRUCTOR ***
    public AutoCommandList(RobotContainer robotContainer)
    {
        this.autonomousTabData = robotContainer.mainShuffleboard.autonomousTab.getAutonomousTabData();
        // this.autonomous1 = autonomous1;
        this.drivetrain = robotContainer.drivetrain;
        this.gyro = robotContainer.gyro;
        this.grabber = robotContainer.grabber;
        this.shoulder = robotContainer.shoulder;
        this.arm = robotContainer.arm;
        this.wrist = robotContainer.wrist;
        
        // commandList.clear();
        build();

        System.out.println(this);
    }

    // *** CLASS & INSTANCE METHODS ***
    private void build()
    {
        // commandList.clear();
        Constants.TargetPosition angle1 = TargetPosition.kGather;
        Constants.TargetPosition angle2 = TargetPosition.kGather;
        Constants.TargetPosition piece1 = TargetPosition.kGather;
        Constants.TargetPosition piece2 = TargetPosition.kGather;
        double distance = 0.0;
        // RowPlayedPiece1 row1 = autonomousTabData.rowPlayedPiece1.getSelected();

        add(new StopDrive(drivetrain));

        angle1 = getAngle1Shoulder(autonomousTabData.rowPlayedPiece1);
        angle2 = getAngle2Shoulder(autonomousTabData.rowPlayedPiece2);
        piece1 = getAngle1Arm(autonomousTabData.rowPlayedPiece1);
        piece2 = getAngle2Arm(autonomousTabData.rowPlayedPiece2);
        distance = getAllianceAndLocation();

        switch(autonomousTabData.autonomousCommands)
        {
            case kNeither:
                if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    // add( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kClamp));
                    // add( new InstantCommand(() -> grabber.grabGamePiece(), grabber));
                    add( new GrabGamePiece(grabber));
                    add( new WaitCommand(1.0));
                    //Commented out this line below
                    // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, angle1));
                    add( new ExtendScorer(shoulder, arm, grabber, wrist, angle1));
                    add( new WaitCommand(0.5));
                    //Commented out line below between Calvin and St. Joe
                    // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                    add( new RetractScorer(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                    add( new InstantCommand( () -> grabber.closeSolenoid()));
                }

                if(autonomousTabData.driveToSecondPiece == DriveToSecondPiece.kYes)
                {
                    // turnRobot180();
                    goToSecondGamePiece();
                    // add( new AutoDriveDistance(drivetrain, gyro, 0.75, 0, 0.5));
                }

                if(autonomousTabData.pickUpGamePieces == PickUpGamePieces.kYes)
                {
                    add( new GrabGamePiece(grabber));
                }

                if(autonomousTabData.scoreSecondPiece == ScoreSecondPiece.kYes)
                {
                    turnRobot180();
                    driveOut(4.4);
                    add( new ScoreGamePiece(shoulder, arm, grabber, wrist, angle2));
                    add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                }

                if(autonomousTabData.moveOntoChargingStation == MoveOntoChargingStation.kYes)
                {
                    goToChargingStation(distance);
                }
                break;
            case kChargingStation:
                add( new ScoreGamePiece(shoulder, arm, grabber, wrist, angle1));
                add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                driveOut(4.26);
                goToChargingStation(distance);
                break;
            case kTwoGamePieces:
                add( new ScoreGamePiece(shoulder, arm, grabber, wrist, angle1));
                add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                goToSecondGamePiece();
                turnRobot180();
                add( new GrabGamePiece(grabber));
                break;
        }
    
    }

    private Constants.TargetPosition getAngle1Shoulder(AutonomousTabData.RowPlayedPiece1 rpp1)
    {
        TargetPosition angle = TargetPosition.kGather;
        switch(rpp1)
        {
            case kNone:
                angle = TargetPosition.kGather;
                break;
            case kBottom:
                angle = TargetPosition.kLow;
                break;
            case kMiddle:
                angle = TargetPosition.kMiddle;
                break;
            case kTop:
                angle = TargetPosition.kHigh;
                break;
        }

        return angle;
    }

    private Constants.TargetPosition getAngle1Arm(AutonomousTabData.RowPlayedPiece1 rpp1)
    {
        TargetPosition piece1 = TargetPosition.kGather;
        switch(rpp1)
        {
            case kNone:
                piece1 = TargetPosition.kGather;
                break;
            case kBottom:
                piece1 = TargetPosition.kLow;
                break;
            case kMiddle:
                piece1 = TargetPosition.kMiddle;
                break;
            case kTop:
                piece1 = TargetPosition.kHigh;
                break;
        }

        return piece1;
    }

    private Constants.TargetPosition getAngle2Shoulder(AutonomousTabData.RowPlayedPiece2 rpp2)
    {
        TargetPosition angle = TargetPosition.kGather;
        switch(rpp2)
        {
            case kNone:
                angle = TargetPosition.kGather;
                break;
            case kBottom:
                angle = TargetPosition.kLow;
                break;
            case kMiddle:
                angle = TargetPosition.kMiddle;
                break;
            case kTop:
                angle = TargetPosition.kHigh;
                break;
        }

        return angle;
    }

    private Constants.TargetPosition getAngle2Arm(AutonomousTabData.RowPlayedPiece2 rpp2)
    {
        TargetPosition piece2 = TargetPosition.kGather;
        switch(rpp2)
        {
            case kNone:
                piece2 = TargetPosition.kGather;
                break;
            case kBottom:
                piece2 = TargetPosition.kLow;
                break;
            case kMiddle:
                piece2 = TargetPosition.kMiddle;
                break;
            case kTop:
                piece2 = TargetPosition.kHigh;
                break;
        }

        return piece2;
    }

    private double getAllianceAndLocation()
    {
        DriverStation.Alliance alliance = DriverStation.getAlliance();
        boolean isRedLeft = (alliance == DriverStation.Alliance.Red && autonomousTabData.startingLocation == StartingLocation.kLeft);
        boolean isBlueRight = (alliance == DriverStation.Alliance.Blue && autonomousTabData.startingLocation == StartingLocation.kRight);
        if(isRedLeft || isBlueRight)
        {
            return 1.0;
        }
        else if(!isRedLeft || !isBlueRight)
        {
            return -1.0;
        }
        else
        {
            return 0.0;
        }
    }



    private void driveOut(double distance)
    {
        add(new AutoDriveDistance(drivetrain, gyro, 0.5, 0.0, distance));
    }

    // private void strafeDrive(double distance)
    // {
    //     add(new AutoDriveDistance(drivetrain, gyro, 0.0, 0.5, distance)); 
    // }

    // private void releasePiece()
    // {
    //     add(new ReleaseGamePiece(grabber));
    //     moveArm(ArmPosition.kGather);
    //     moveShoulder(ShoulderPosition.kGather);
    
    // }

    // private void moveShoulder(ShoulderPosition level)
    // {
    //     add(new MoveShoulderToScoringPosition(shoulder, level));
    // }

    // private void moveArm(ArmPosition position)
    // {
    //     add(new MoveArmToScoringPosition(arm, position));
    // }



    private void turnRobot180()
    {
        // add(new AutoDriveDistance(drivetrain, gyro, 0.5, -0.5, 2.0));
    }

    private void goToSecondGamePiece()
    {
        add(new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 3.75));
    }

    private void goToChargingStation(double distance)
    {
        add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 1.75));
	    add( new AutoBalance(drivetrain, gyro));
		add( new LockWheels(drivetrain));
        
        // if(distance == 0.0)
        // {
        //     // add( new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, 4.4));
		// 	// add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 2.9));
        //     add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 3.0));
		// 	add( new AutoBalance(drivetrain, gyro));
		// 	add( new LockWheels(drivetrain));
        // }
        // else
        // {
        //     add(new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, 4.4));
        //     add(new AutoDriveDistance(drivetrain, gyro, 0.0, distance, 1.5));
        //     add(new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 1.4));
        //     add( new AutoBalance(drivetrain, gyro));
		// 	add( new LockWheels(drivetrain));
        // }
        
    }



    // private void addCommand(Command command)
    // {
        // Command commandList[20];
        // Command startingCommand = kDelay;
        // int counter;
        // for(counter = 0; counter <= size(commandList); counter++)
        // {
        //     startingCommand.andThen(commandList[counter]);
        // }
        
    //     commandList.add(command);
    // }

    // ***** Use these methods in AutonomousMode to execute the AutonomousCommandList

    private void add(Command command)
    {

        addCommands(command);
        // commandList.add(command);
        commandString += command + "\n";
    }

    public String toString()
    {
        return commandString;
    }



}

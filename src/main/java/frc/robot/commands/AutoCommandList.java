package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Constants.SuctionState;
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
import frc.robot.subsystems.Drivetrain.ArcadeDriveDirection;
import frc.robot.subsystems.Wrist.WristPosition;
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
        Constants.TargetPosition shoulderPositionPiece1 = TargetPosition.kGather;
        Constants.TargetPosition shoulderPositionPiece2 = TargetPosition.kGather;
        Constants.TargetPosition armPositionPiece1 = TargetPosition.kGather;
        Constants.TargetPosition armPositionPiece2 = TargetPosition.kGather;
        int location = 0;
        // RowPlayedPiece1 row1 = autonomousTabData.rowPlayedPiece1.getSelected();

        add(new StopDrive(drivetrain));

        shoulderPositionPiece1 = getShoulderPositionPiece1(autonomousTabData.rowPlayedPiece1);
        shoulderPositionPiece2 = getShoulderPositionPiece2(autonomousTabData.rowPlayedPiece2);
        armPositionPiece1 = getArmPositionPiece1(autonomousTabData.rowPlayedPiece1);
        armPositionPiece2 = getArmPosition2(autonomousTabData.rowPlayedPiece2);
        location = getAllianceAndLocation();

        switch(autonomousTabData.autonomousCommands)
        {
            case kNeither:
                if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    // add( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kClamp));
                    // add( new InstantCommand(() -> grabber.grabGamePiece(), grabber));

                    // add( new GrabGamePiece(grabber));
                    add( new SuctionControl(grabber, SuctionState.kOn));
                    add( new InstantCommand( () -> shoulder.initialPinch(), shoulder));
                    // add( new InstantCommand( () -> shoulder.setPositionAfterPinch()));
                    add( new WaitCommand(0.25));
                    //Commented out this line below
                    // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, shoulderPositionPiece1));
                    add( new ExtendScorer(shoulder, arm, wrist, grabber, shoulderPositionPiece1));
                    add( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kHighConeLock));
                    add( new SuctionControl(grabber, SuctionState.kOff));
                    add( new WaitCommand(0.25));
                    //Commented out line below between Calvin and St. Joe
                    // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                    if(autonomousTabData.moveOntoChargingStation == MoveOntoChargingStation.kYes)
                    {
                        // add( new MoveArmToScoringPosition(arm, TargetPosition.kGather));
                        // add( new MoveWrist(wrist, WristPosition.kDown));
                    }
                    else
                    {
                        add( new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather));
                    }
                    
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
                    // add( new GrabGamePiece(grabber));
                    add( new SuctionControl(grabber, SuctionState.kOn));
                }

                if(autonomousTabData.scoreSecondPiece == ScoreSecondPiece.kYes)
                {
                    turnRobot180();
                    driveOut(4.4);
                    //Updated between Calvin and St. Joe
                    // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, shoulderPositionPiece2));
                    // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                    add( new ExtendScorer(shoulder, arm, wrist, grabber, shoulderPositionPiece2));
                    add( new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather));
                }

                if(autonomousTabData.moveOntoChargingStation == MoveOntoChargingStation.kYes)
                {
                    goToChargingStation(location);
                }
                break;
            case kChargingStation:
                // Changed between Calvin and St. Joe
                // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, shoulderPositionPiece2));
                // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                driveOut(4.26);
                goToChargingStation(location);
                break;
            case kTwoGamePieces:
                // Changed between Calvin and St. Joe
                // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, angle1));
                // add( new ScoreGamePiece(shoulder, arm, grabber, wrist, TargetPosition.kGather));
                add( new ExtendScorer(shoulder, arm, wrist, grabber, shoulderPositionPiece1));
                add( new WaitCommand(0.5));
                // add( new ReleaseGamePiece(grabber));
                add( new SuctionControl(grabber, SuctionState.kOff));
                add( new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather));
                goToSecondGamePiece();
                turnRobot180();
                // add( new GrabGamePiece(grabber));
                add( new SuctionControl(grabber, SuctionState.kOn));
                break;
        }
    
    }

    private Constants.TargetPosition getShoulderPositionPiece1(AutonomousTabData.RowPlayedPiece1 rpp1)
    {
        TargetPosition position = TargetPosition.kGather;
        switch(rpp1)
        {
            case kNone:
                position = TargetPosition.kGather;
                break;
            case kBottom:
                position = TargetPosition.kLowCone;
                break;
            case kMiddle:
                position = TargetPosition.kMiddleCone;
                break;
            case kTop:
                position = TargetPosition.kHighCone;
                break;
        }

        return position;
    }

    private Constants.TargetPosition getArmPositionPiece1(AutonomousTabData.RowPlayedPiece1 rpp1)
    {
        TargetPosition position = TargetPosition.kGather;
        switch(rpp1)
        {
            case kNone:
                position = TargetPosition.kGather;
                break;
            case kBottom:
                position = TargetPosition.kLowCone;
                break;
            case kMiddle:
                position = TargetPosition.kMiddleCone;
                break;
            case kTop:
                position = TargetPosition.kHighCone;
                break;
        }

        return position;
    }

    private Constants.TargetPosition getShoulderPositionPiece2(AutonomousTabData.RowPlayedPiece2 rpp2)
    {
        TargetPosition position = TargetPosition.kGather;
        switch(rpp2)
        {
            case kNone:
                position = TargetPosition.kGather;
                break;
            case kBottom:
                position = TargetPosition.kLowCone;
                break;
            case kMiddle:
                position = TargetPosition.kMiddleCone;
                break;
            case kTop:
                position = TargetPosition.kHighCone;
                break;
        }

        return position;
    }

    private Constants.TargetPosition getArmPosition2(AutonomousTabData.RowPlayedPiece2 rpp2)
    {
        TargetPosition position = TargetPosition.kGather;
        switch(rpp2)
        {
            case kNone:
                position = TargetPosition.kGather;
                break;
            case kBottom:
                position = TargetPosition.kLowCone;
                break;
            case kMiddle:
                position = TargetPosition.kMiddleCone;
                break;
            case kTop:
                position = TargetPosition.kHighCone;
                break;
        }

        return position;
    }

    private int getAllianceAndLocation()
    {
        DriverStation.Alliance alliance = DriverStation.getAlliance();
        boolean isRedLeft = (alliance == DriverStation.Alliance.Red && autonomousTabData.startingLocation == StartingLocation.kLeft);
        boolean isBlueRight = (alliance == DriverStation.Alliance.Blue && autonomousTabData.startingLocation == StartingLocation.kRight);
        boolean isRedRight = (alliance == DriverStation.Alliance.Red && autonomousTabData.startingLocation == StartingLocation.kRight);
        boolean isBlueLeft = (alliance == DriverStation.Alliance.Blue && autonomousTabData.startingLocation == StartingLocation.kLeft);
        if(isRedLeft || isBlueRight)
        {
            return 1;
        }
        else if(isRedRight || isBlueLeft)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }



    private void driveOut(double distance)
    {
        add(new ArcadeAutoDriveDistance(drivetrain, gyro, 0.5, 0.0, ArcadeDriveDirection.kStraight, distance));
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
        add(new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 3.75));
    }

    private void goToChargingStation(double location)
    {
        switch(autonomousTabData.startingLocation)
        {
            case kLeft:
                if(autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                                new AutoDriveDistance(drivetrain, gyro, 0.0, 1.5, 1.75),
                                new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8)),
                            new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather)));
                }
                else
                {
                    add( new SequentialCommandGroup(
                            new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                            new AutoDriveDistance(drivetrain, gyro, 0.0, 1.5, 1.75),
                            new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8)));
                }

                add( new AutoBalance(drivetrain, gyro, 1));
                break;
            
            case kMiddle:
                if(autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 4.3),  //4.1
                                new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 2.1),   //1.9
                                // new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 1.75),
                                // new AutoBalance(drivetrain, gyro, -1)),
                                new AutoBalance(drivetrain, gyro, 1)),
                            new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather))); 
                }
                else
                {
                    add( new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 4.1));
                    add( new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.9));
                    add( new AutoBalance(drivetrain, gyro, 1));

                    // add( new ArcadeAutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 1.75));
                    // add( new AutoBalance(drivetrain, gyro, -1));
                }
                break;
            
            case kRight:
                if(autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    add( new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                                new AutoDriveDistance(drivetrain, gyro, 0.0, -1.5, 1.75),
                                new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8)),
                            new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather)));
                }
                else
                {
                    add( new SequentialCommandGroup(
                            new ArcadeAutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
                            new AutoDriveDistance(drivetrain, gyro, 0.0, -1.5, 1.75),
                            new ArcadeAutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.8)));
                }

                add( new AutoBalance(drivetrain, gyro, 1));
                break;
        }

		add( new LockWheels(drivetrain));

        // if(autonomousTabData.playPreload == PlayPreload.kYes)
        // {
        //     // add( new ParallelCommandGroup( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kGather), new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 1.75)));
        //     add( new ParallelCommandGroup(
        //          new AutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75),
        //          new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather)));

        //     //      new MoveArmToScoringPosition(arm, TargetPosition.kGather), 
        //     //      new MoveShoulderToScoringPosition(shoulder, TargetPosition.kGather),
        //     //      new AutoDriveDistance(drivetrain, gyro, -3.0, 0.0, ArcadeDriveDirection.kStraight, 3.75)));
        //         //  new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 2.0)));
        // }
        // else
        // {
        //     // add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, 1.75));
        //     add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStraight, 3.75));
        // }

        // if(autonomousTabData.startingLocation == StartingLocation.kLeft)
        // {
        //     add( new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStrafe, 1.6));
        // }

        // if(autonomousTabData.startingLocation == StartingLocation.kRight)
        // {
        //     add( new AutoDriveDistance(drivetrain, gyro, -1.5, 0.0, ArcadeDriveDirection.kStrafe, 1.6));
        // }

        // add( new AutoDriveDistance(drivetrain, gyro, 1.5, 0.0, ArcadeDriveDirection.kStraight, 1.5));

	    
        
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

package frc.robot.commands;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
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
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmPosition;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Shoulder.ScoringPosition;

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
    private final Grabber grabber;
    private final Shoulder shoulder;
    private final Arm arm;
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
        this.grabber = robotContainer.grabber;
        this.shoulder = robotContainer.shoulder;
        this.arm = robotContainer.arm;
        
        // commandList.clear();
        build();

        System.out.println(this);
    }

    // *** CLASS & INSTANCE METHODS ***
    private void build()
    {
        // commandList.clear();
        Shoulder.ScoringPosition angle = ScoringPosition.kGatherer;
        Shoulder.ScoringPosition angle2 = ScoringPosition.kGatherer;
        Arm.ArmPosition piece1 = ArmPosition.kIn;
        Arm.ArmPosition piece2 = ArmPosition.kIn;
        double distance = 0.0;
        // RowPlayedPiece1 row1 = autonomousTabData.rowPlayedPiece1.getSelected();

        add(new StopDrive(drivetrain));

        angle = getAngle1(autonomousTabData.rowPlayedPiece1);
        angle2 = getAngle2(autonomousTabData.rowPlayedPiece2);
        distance = getAllianceAndLocation();

        switch(autonomousTabData.autonomousCommands)
        {
            case kNeither:
                if(autonomousTabData.containingPreload == ContainingPreload.kYes && autonomousTabData.playPreload == PlayPreload.kYes)
                {
                    moveShoulder(angle);
                    releasePiece();
                }

                if(autonomousTabData.driveToSecondPiece == DriveToSecondPiece.kYes)
                {
                    turnRobot180();
                    driveOut(4.4);
                }

                if(autonomousTabData.pickUpGamePieces == PickUpGamePieces.kYes)
                {
                    add(new OpenGrabber(grabber));
                }

                if(autonomousTabData.scoreSecondPiece == ScoreSecondPiece.kYes)
                {
                    turnRobot180();
                    driveOut(4.4);
                    moveShoulder(angle2);
                }

                if(autonomousTabData.moveOntoChargingStation == MoveOntoChargingStation.kYes)
                {
                    goToChargingStation(0.5);
                }
                break;
            case kChargingStation:
                moveShoulder(angle);
                moveArm(piece1);
                releasePiece();
                driveOut(4.26);
                goToChargingStation(distance);
                break;
            case kTwoGamePieces:
                moveShoulder(angle);
                moveArm(piece2);
                releasePiece();
                driveOut(4.4);
                turnRobot180();
                pickUpPiece2();
                break;
        }
    
    }

    private Shoulder.ScoringPosition getAngle1(AutonomousTabData.RowPlayedPiece1 rpp1)
    {
        ScoringPosition angle = ScoringPosition.kGatherer;
        ArmPosition piece1 = ArmPosition.kIn;
        switch(rpp1)
        {
            case kNone:
                angle = ScoringPosition.kGatherer;
                piece1 = ArmPosition.kIn;
                break;
            case kBottom:
                angle = ScoringPosition.kLow;
                piece1 = ArmPosition.kHalfExtended;
                break;
            case kMiddle:
                angle = ScoringPosition.kMiddle;
                piece1 = ArmPosition.kThreeQuarterExtended;
                break;
            case kTop:
                angle = ScoringPosition.kHigh;
                piece1 = ArmPosition.kFullyExtended;
                break;
        }

        return angle;
    }

    private Shoulder.ScoringPosition getAngle2(AutonomousTabData.RowPlayedPiece2 rpp2)
    {
        ScoringPosition angle = ScoringPosition.kGatherer;
        ArmPosition piece2 = ArmPosition.kIn;
        switch(rpp2)
        {
            case kNone:
                angle = ScoringPosition.kGatherer;
                piece2 = ArmPosition.kIn;
                break;
            case kBottom:
                angle = ScoringPosition.kLow;
                piece2 = ArmPosition.kHalfExtended;
                break;
            case kMiddle:
                angle = ScoringPosition.kMiddle;
                piece2 = ArmPosition.kThreeQuarterExtended;
                break;
            case kTop:
                angle = ScoringPosition.kHigh;
                piece2 = ArmPosition.kFullyExtended;
                break;
        }

        return angle;
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
        
        else
        {
            return -1.0;
        }
    }



    private void driveOut(double distance)
    {
        add(new AutoDriveDistance(drivetrain, 0.5, 0.0, distance));
    }

    private void strafeDrive(double distance)
    {
        add(new AutoDriveDistance(drivetrain, 0.0, 0.5, distance)); 
    }

    private void releasePiece()
    {
        add(new OpenGrabber(grabber));
        moveShoulder(ScoringPosition.kGatherer);
    }

    private void moveShoulder(ScoringPosition level)
    {
        add(new MoveShoulderToScoringPosition(shoulder, level));
    }

    private void moveArm(ArmPosition position)
    {
        add(new MoveArm(arm, position));
    }



    private void turnRobot180()
    {
        add(new AutoDriveDistance(drivetrain, 0.5, -0.5, 2));
    }

    private void goToSecondGamePiece()
    {
        add(new AutoDriveDistance(drivetrain, 0.75, 0.0, 4.5));
    }

    private void goToChargingStation(double distance)
    {
        add(new AutoDriveDistance(drivetrain, 0.75, 0.0, 4.27));
        add(new AutoDriveDistance(drivetrain, 0.0, distance, 1.0));
        add(new AutoDriveDistance(drivetrain, -0.75, 0.0, 1.0));
    }

    private void pickUpPiece2()
    {

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

package frc.robot.shuffleboard;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.util.sendable.SendableRegistry;


public class AutonomousTab 
{
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();


    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS & INSTANCE VARIABLES ***
    // Create a Shuffleboard Tab
    private ShuffleboardTab autonomousTab = Shuffleboard.getTab("Autonomous");

    private AutonomousTabData autonomousTabData = new AutonomousTabData();
  
    // Create the Box objects
    private SendableChooser<AutonomousTabData.StartingLocation> startingLocationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.MoveOntoChargingStation> moveOntoChargingStationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.PickUpGamePieces> pickUpGamePiecesBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.RowPlayedPiece1> rowPlayedPiece1Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ColumnPlayedPiece1> columnPlayedPiece1Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.RowPlayedPiece2> rowPlayedPiece2Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ColumnPlayedPiece2> columnPlayedPiece2Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.CurrentlyContainingGamePiece> currentlyContainingGamePieceBox = new SendableChooser<>();

    private NetworkTableEntry successfulDownload;
    private NetworkTableEntry errorMessageBox;

    // Create the Button object
    private SendableChooser<Boolean> sendDataButton = new SendableChooser<>();
 
    private boolean previousStateOfSendButton = false;
    private boolean isDataValid = true;
    private String errorMessage = "No Errors";

    // *** CLASS CONSTRUCTOR ***
    public AutonomousTab()
    {
        System.out.println(fullClassName + " : Constructor Started");

        createStartingLocationBox();
        createMoveOntoChargingStationBox();
        createPickUpGamePiecesBox();
        createRowPlayedPiece1Box();
        createColumnPlayedPiece1Box();
        createRowPlayedPiece2Box();
        createColumnPlayedPiece2Box();
        createCurrentlyContainingGamePieceBox();
        
        // createSendDataButton();
        // successfulDownload = createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        // createMessageBox();

        // errorMessageBox = createErrorMessageBox();
        // errorMessageBox.setString("No Errors");

        System.out.println(fullClassName + ": Constructor Finished");
    }

    // *** CLASS & INSTANCE METHODS ***
    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createStartingLocationBox()
    {
        //create and name the Box
        SendableRegistry.add(startingLocationBox, "Starting Location");
        SendableRegistry.setName(startingLocationBox, "Starting Location");
        
        //add options to  Box
        startingLocationBox.addOption("Left", AutonomousTabData.StartingLocation.kLeft);
        startingLocationBox.setDefaultOption("Middle", AutonomousTabData.StartingLocation.kMiddle);
        startingLocationBox.addOption("Right", AutonomousTabData.StartingLocation.kRight);

        //put the widget on the shuffleboard
        autonomousTab.add(startingLocationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(0, 0)
            .withSize(8, 2);
    }

    /**
    * <b>Move Onto Charging Station</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createMoveOntoChargingStationBox()
    {
        //create and name the Box
        SendableRegistry.add(moveOntoChargingStationBox, "Move Onto Charging Station");
        SendableRegistry.setName(moveOntoChargingStationBox, "Move Onto Charging Station");

        //add options to Box
        moveOntoChargingStationBox.setDefaultOption("No", AutonomousTabData.MoveOntoChargingStation.kNo);
        moveOntoChargingStationBox.addOption("Yes", AutonomousTabData.MoveOntoChargingStation.kYes);

        //put the widget on the shuffleboard
        autonomousTab.add(moveOntoChargingStationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 3)
            .withSize(4, 2);
    }

    /**
    * <b>Pick Up Game Pieces</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPickUpGamePiecesBox()
    {
        //create and name the Box
        SendableRegistry.add(pickUpGamePiecesBox, "Pick Up Game Pieces");
        SendableRegistry.setName(pickUpGamePiecesBox, "Pick Up Game Pieces");

        //add options to Box
        pickUpGamePiecesBox.setDefaultOption("Yes", AutonomousTabData.PickUpGamePieces.kYes);
        pickUpGamePiecesBox.addOption("No", AutonomousTabData.PickUpGamePieces.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(pickUpGamePiecesBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 3)
            .withSize(6, 2);
    }

    /**
    * <b>Row for Piece 1</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRowPlayedPiece1Box()
    {
        //create and name the Box
        SendableRegistry.add(rowPlayedPiece1Box, "Row Played Piece 1");
        SendableRegistry.setName(rowPlayedPiece1Box, "Row Played Piece 1");

        //add options to Box
        rowPlayedPiece1Box.setDefaultOption("1", AutonomousTabData.RowPlayedPiece1.k1);
        rowPlayedPiece1Box.addOption("2", AutonomousTabData.RowPlayedPiece1.k2);
        rowPlayedPiece1Box.addOption("3", AutonomousTabData.RowPlayedPiece1.k3);

        //put the widget on the shuffleboard
        autonomousTab.add(rowPlayedPiece1Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 6)
            .withSize(4, 2);
    }

    /**
    * <b>Column for Piece 1</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createColumnPlayedPiece1Box()
    {
        //create and name the Box
        SendableRegistry.add(columnPlayedPiece1Box, "Column Played Piece 1");
        SendableRegistry.setName(columnPlayedPiece1Box, "Column Played Piece 1");

        //add options to Box
        columnPlayedPiece1Box.setDefaultOption("1", AutonomousTabData.ColumnPlayedPiece1.k1);
        columnPlayedPiece1Box.addOption("2", AutonomousTabData.ColumnPlayedPiece1.k2);
        columnPlayedPiece1Box.addOption("3", AutonomousTabData.ColumnPlayedPiece1.k3);

        //put the widget on the shuffleboard
        autonomousTab.add(columnPlayedPiece1Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(9, 0)
            .withSize(16, 2);
    }

    /**
    * <b>Row for Piece 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRowPlayedPiece2Box()
    {
        //create and name the Box
        SendableRegistry.add(rowPlayedPiece2Box, "Row Played Piece 2");
        SendableRegistry.setName(rowPlayedPiece2Box, "Row Played Piece 2");

        //add options to Box
        rowPlayedPiece2Box.setDefaultOption("1", AutonomousTabData.RowPlayedPiece2.k1);
        rowPlayedPiece2Box.addOption("2", AutonomousTabData.RowPlayedPiece2.k2);
        rowPlayedPiece2Box.addOption("3", AutonomousTabData.RowPlayedPiece2.k3);

        //put the widget on the shuffleboard
        autonomousTab.add(rowPlayedPiece2Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(6, 6)
            .withSize(6, 2);
    }

    /**
    * <b>Column for Piece 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createColumnPlayedPiece2Box()
    {
        //create and name the Box
        SendableRegistry.add(columnPlayedPiece2Box, "Column Played Piece 2");
        SendableRegistry.setName(columnPlayedPiece2Box, "Column Played Piece 2");

        //add options to Box
        columnPlayedPiece2Box.setDefaultOption("1", AutonomousTabData.ColumnPlayedPiece2.k1);
        columnPlayedPiece2Box.addOption("2", AutonomousTabData.ColumnPlayedPiece2.k2);
        columnPlayedPiece2Box.addOption("3", AutonomousTabData.ColumnPlayedPiece2.k3);

        //put the widget on the shuffleboard
        autonomousTab.add(columnPlayedPiece2Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(13, 3)
            .withSize(6, 2);
    }

    /**
    * <b>Column for Piece 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createCurrentlyContainingGamePieceBox()
    {
        //create and name the Box
        SendableRegistry.add(currentlyContainingGamePieceBox, "Currently Containing Game Piece");
        SendableRegistry.setName(currentlyContainingGamePieceBox, "Currently Containing Game Piece");

        //add options to Box
        currentlyContainingGamePieceBox.setDefaultOption("Yes", AutonomousTabData.CurrentlyContainingGamePiece.kYes);
        currentlyContainingGamePieceBox.addOption("No", AutonomousTabData.CurrentlyContainingGamePiece.kNo);
        columnPlayedPiece2Box.addOption("3", AutonomousTabData.ColumnPlayedPiece2.k3);

        //put the widget on the shuffleboard
        autonomousTab.add(columnPlayedPiece2Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(13, 3)
            .withSize(6, 2);
    }
}

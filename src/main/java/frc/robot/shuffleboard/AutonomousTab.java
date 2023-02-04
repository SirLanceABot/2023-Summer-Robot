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
    private final AutonomousTabData autonomousTabDataMain;
  
    // Create the Box objects
    private SendableChooser<AutonomousTabData.StartingLocation> startingLocationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.AreGamePiecesPlayed> areGamePiecesPlayedBox = new SendableChooser<>();
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
    public AutonomousTab(AutonomousTabData autonomousTabData)
    {
        System.out.println(fullClassName + " : Constructor Started");

        autonomousTabDataMain = autonomousTabData;

        createStartingLocationBox();
        createAreGamePiecesPlayedBox();
        createMoveOntoChargingStationBox();
        createPickUpGamePiecesBox();
        createRowPlayedPiece1Box();
        createColumnPlayedPiece1Box();
        createRowPlayedPiece2Box();
        createColumnPlayedPiece2Box();
        createCurrentlyContainingGamePieceBox();
        
        createSendDataButton();
        createSuccessfulDownloadBox();
        successfulDownload.setBoolean(false);

        // createMessageBox();

        createErrorMessageBox();
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
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createAreGamePiecesPlayedBox()
    {
        //create and name the Box
        SendableRegistry.add(areGamePiecesPlayedBox, "Are Game Pieces Played");
        SendableRegistry.setName(areGamePiecesPlayedBox, "Are Game Pieces Played");
        
        //add options to  Box
        areGamePiecesPlayedBox.setDefaultOption("Yes", AutonomousTabData.AreGamePiecesPlayed.kYes);
        areGamePiecesPlayedBox.addOption("No", AutonomousTabData.AreGamePiecesPlayed.kNo);

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
        moveOntoChargingStationBox.setDefaultOption("Yes", AutonomousTabData.MoveOntoChargingStation.kYes);
        moveOntoChargingStationBox.addOption("No", AutonomousTabData.MoveOntoChargingStation.kNo);

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
        rowPlayedPiece1Box.addOption("0", AutonomousTabData.RowPlayedPiece1.k0);
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
        columnPlayedPiece1Box.addOption("0", AutonomousTabData.ColumnPlayedPiece1.k0);
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
        rowPlayedPiece2Box.setDefaultOption("0", AutonomousTabData.RowPlayedPiece2.k0);
        rowPlayedPiece2Box.addOption("1", AutonomousTabData.RowPlayedPiece2.k1);
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
        columnPlayedPiece2Box.setDefaultOption("0", AutonomousTabData.ColumnPlayedPiece2.k0);
        columnPlayedPiece2Box.addOption("1", AutonomousTabData.ColumnPlayedPiece2.k1);
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

    /**
     * <b>Send Data</b> Button
     * <p>
     * Create an entry in the Network Table and add the Button to the Shuffleboard
     * Tab
     * 
     * @return
     */
    private void createSendDataButton()
    {
        SendableRegistry.add(sendDataButton, "Send Data");
        SendableRegistry.setName(sendDataButton, "Send Data");

        sendDataButton.setDefaultOption("No", false);
        sendDataButton.addOption("Yes", true);

        autonomousTab.add(sendDataButton)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(23, 1)
            .withSize(4, 2);
    }

    private void createSuccessfulDownloadBox()
    {
        Map<String, Object> booleanBoxProperties = new HashMap<>();
        booleanBoxProperties.put("Color when true", "Lime");
        booleanBoxProperties.put("Color when false", "Red");
        
        autonomousTab.add("Successful Download?", false)
             .withWidget(BuiltInWidgets.kBooleanBox)
             .withPosition(23, 4)
             .withSize(4, 4)
             .withProperties(booleanBoxProperties)
             .getEntry();
    }

    private void createErrorMessageBox()
    {
         autonomousTab.add("Error Messages", "No Errors")
             .withWidget(BuiltInWidgets.kTextView)
             .withPosition(1, 10)
             .withSize(26, 2)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.startingLocation = startingLocationBox.getSelected();
        autonomousTabData.areGamePiecesPlayed = areGamePiecesPlayedBox.getSelected();
        autonomousTabData.moveOntoChargingStation = moveOntoChargingStationBox.getSelected();
        autonomousTabData.pickUpGamePieces = pickUpGamePiecesBox.getSelected();
        autonomousTabData.rowPlayedPiece1 = rowPlayedPiece1Box.getSelected();
        autonomousTabData.columnPlayedPiece1 = columnPlayedPiece1Box.getSelected();
        autonomousTabData.rowPlayedPiece2 = rowPlayedPiece2Box.getSelected();
        autonomousTabData.columnPlayedPiece2 = columnPlayedPiece2Box.getSelected();
        autonomousTabData.currentlyContainingGamePiece = currentlyContainingGamePieceBox.getSelected();
    }

    public boolean wasSendDataButtonPressed()
    {
        boolean isNewData = false;
        boolean isSendDataButtonPressed = sendDataButton.getSelected();

        updateIsDataValidAndErrorMessage();

        if(isSendDataButtonPressed && !previousStateOfSendButton)
        {
            previousStateOfSendButton = true;
            isNewData = true;

            if(isDataValid)
            {
                successfulDownload.setBoolean(true);
                updateAutonomousTabData();
            }
            else
            {
                successfulDownload.setBoolean(false);
                DriverStation.reportWarning(errorMessage, false);
                errorMessageBox.setString(errorMessage);
            }
        }
        
        if (!isSendDataButtonPressed && previousStateOfSendButton)
        {
            previousStateOfSendButton = false;
        }

        return isNewData;
    }

    public AutonomousTabData getAutonomousTabData()
    {
        return autonomousTabData;
    }

    public void updateIsDataValidAndErrorMessage()
    {
        errorMessage = "No Errors";
        String msg = "";
        boolean isValid = true;

        boolean isMiddle = (startingLocationBox.getSelected() == AutonomousTabData.StartingLocation.kMiddle);
        boolean isPiecesPlayed = (areGamePiecesPlayedBox.getSelected() == AutonomousTabData.AreGamePiecesPlayed.kYes);
        boolean isPiecesPlayedFalse = (areGamePiecesPlayedBox.getSelected() == AutonomousTabData.AreGamePiecesPlayed.kNo);
        boolean isMoveOntoStation = (moveOntoChargingStationBox.getSelected() == AutonomousTabData.MoveOntoChargingStation.kYes);
        boolean isPiecesPickedUp = (pickUpGamePiecesBox.getSelected() == AutonomousTabData.PickUpGamePieces.kNo);
        boolean isRow1 = (rowPlayedPiece1Box.getSelected() == AutonomousTabData.RowPlayedPiece1.k1);
        boolean isColumn1 = (columnPlayedPiece1Box.getSelected() == AutonomousTabData.ColumnPlayedPiece1.k1);
        boolean isRow2False = (rowPlayedPiece2Box.getSelected() == AutonomousTabData.RowPlayedPiece2.k0);
        boolean isRow2True = (rowPlayedPiece2Box.getSelected() == AutonomousTabData.RowPlayedPiece2.k1);
        boolean isColumn2False = (columnPlayedPiece2Box.getSelected() == AutonomousTabData.ColumnPlayedPiece2.k0);
        boolean isColumn2True = (columnPlayedPiece2Box.getSelected() == AutonomousTabData.ColumnPlayedPiece2.k1);
        boolean isPiecesContained = (currentlyContainingGamePieceBox.getSelected() == AutonomousTabData.CurrentlyContainingGamePiece.kYes);
        
        if(!isMiddle && isMoveOntoStation)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }
        
        if(isRow2True && isMoveOntoStation || isColumn2True && isMoveOntoStation)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isPiecesContained && isRow1 || !isPiecesContained && isColumn1)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isPiecesContained && isRow1 || !isPiecesContained && isColumn1)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isPiecesContained && isRow1 || !isPiecesContained && isColumn1)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }

        if(!isPiecesContained && isRow1 || !isPiecesContained && isColumn1)
        {
            isValid = false;
            
            msg += "[ Not Possible ]  \n";
        }
        

        
        
    }   
}

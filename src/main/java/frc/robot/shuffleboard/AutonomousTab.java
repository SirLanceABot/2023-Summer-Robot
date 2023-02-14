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
    private SendableChooser<AutonomousTabData.PlayPreload> playPreloadBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.MoveOntoChargingStation> moveOntoChargingStationBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.PickUpGamePieces> pickUpGamePiecesBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.RowPlayedPiece1> rowPlayedPiece1Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ColumnPlayedPiece1> columnPlayedPiece1Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.RowPlayedPiece2> rowPlayedPiece2Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ColumnPlayedPiece2> columnPlayedPiece2Box = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.ContainingPreload> containingPreloadBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.AutonomousCommands> autonomousCommandsBox = new SendableChooser<>();
    private SendableChooser<AutonomousTabData.DriveToSecondPiece> driveToSecondPieceBox = new SendableChooser<>();
    
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
        createPlayPreloadBox();
        createMoveOntoChargingStationBox();
        createPickUpGamePiecesBox();
        createRowPlayedPiece1Box();
        createColumnPlayedPiece1Box();
        createRowPlayedPiece2Box();
        createColumnPlayedPiece2Box();
        createContainingPreloadBox();
        createAutonomousCommandsBox();
        createDriveToSecondPieceBox();
        
        createSendDataButton();
        createSuccessfulDownloadBox();
        // successfulDownload.setBoolean(false);

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
            .withPosition(0, 2)
            .withSize(6, 2);
    }

    /**
    * <b>Starting Location</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPlayPreloadBox()
    {
        //create and name the Box
        SendableRegistry.add(playPreloadBox, "Play Preload?");
        SendableRegistry.setName(playPreloadBox, "Play Preload?");
        
        //add options to  Box
        playPreloadBox.setDefaultOption("Yes", AutonomousTabData.PlayPreload.kYes);
        playPreloadBox.addOption("No", AutonomousTabData.PlayPreload.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(playPreloadBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(11, 0)
            .withSize(4, 2);
    }

    /**
    * <b>Move Onto Charging Station</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createMoveOntoChargingStationBox()
    {
        //create and name the Box
        SendableRegistry.add(moveOntoChargingStationBox, "Charge Station");
        SendableRegistry.setName(moveOntoChargingStationBox, "Charge Station");

        //add options to Box
        moveOntoChargingStationBox.setDefaultOption("Yes", AutonomousTabData.MoveOntoChargingStation.kYes);
        moveOntoChargingStationBox.addOption("No", AutonomousTabData.MoveOntoChargingStation.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(moveOntoChargingStationBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(1, 5)
            .withSize(4, 2);
    }

    /**
    * <b>Pick Up Game Pieces</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createPickUpGamePiecesBox()
    {
        //create and name the Box
        SendableRegistry.add(pickUpGamePiecesBox, "Pick Up Game Piece");
        SendableRegistry.setName(pickUpGamePiecesBox, "Pick Up Game Piece");

        //add options to Box
        pickUpGamePiecesBox.setDefaultOption("Yes", AutonomousTabData.PickUpGamePieces.kYes);
        pickUpGamePiecesBox.addOption("No", AutonomousTabData.PickUpGamePieces.kNo);

        //put the widget on the shuffleboard
        autonomousTab.add(pickUpGamePiecesBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(16, 0)
            .withSize(4, 2);
    }

    /**
    * <b>Row for Piece 1</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createRowPlayedPiece1Box()
    {
        //create and name the Box
        SendableRegistry.add(rowPlayedPiece1Box, "Row Played Preload");
        SendableRegistry.setName(rowPlayedPiece1Box, "Row Played Preload");

        //add options to Box
        rowPlayedPiece1Box.addOption("None", AutonomousTabData.RowPlayedPiece1.kNone);
        rowPlayedPiece1Box.setDefaultOption("Bottom", AutonomousTabData.RowPlayedPiece1.kBottom);
        rowPlayedPiece1Box.addOption("Middle", AutonomousTabData.RowPlayedPiece1.kMiddle);
        rowPlayedPiece1Box.addOption("Top", AutonomousTabData.RowPlayedPiece1.kTop);

        //put the widget on the shuffleboard
        autonomousTab.add(rowPlayedPiece1Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(8, 3)
            .withSize(6, 2);
    }

    /**
    * <b>Column for Piece 1</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createColumnPlayedPiece1Box()
    {
        //create and name the Box
        SendableRegistry.add(columnPlayedPiece1Box, "Column Played Preload");
        SendableRegistry.setName(columnPlayedPiece1Box, "Column Played Preload");

        //add options to Box
        columnPlayedPiece1Box.addOption("None", AutonomousTabData.ColumnPlayedPiece1.kNone);
        columnPlayedPiece1Box.setDefaultOption("Left", AutonomousTabData.ColumnPlayedPiece1.kLeft);
        columnPlayedPiece1Box.addOption("Middle", AutonomousTabData.ColumnPlayedPiece1.kMiddle);
        columnPlayedPiece1Box.addOption("Right", AutonomousTabData.ColumnPlayedPiece1.kRight);

        //put the widget on the shuffleboard
        autonomousTab.add(columnPlayedPiece1Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(8, 6)
            .withSize(6, 2);
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
        rowPlayedPiece2Box.setDefaultOption("None", AutonomousTabData.RowPlayedPiece2.kNone);
        rowPlayedPiece2Box.addOption("Bottom", AutonomousTabData.RowPlayedPiece2.kBottom);
        rowPlayedPiece2Box.addOption("Middle", AutonomousTabData.RowPlayedPiece2.kMiddle);
        rowPlayedPiece2Box.addOption("Top", AutonomousTabData.RowPlayedPiece2.kTop);

        //put the widget on the shuffleboard
        autonomousTab.add(rowPlayedPiece2Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(15, 3)
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
        columnPlayedPiece2Box.setDefaultOption("None", AutonomousTabData.ColumnPlayedPiece2.kNone);
        columnPlayedPiece2Box.addOption("Left", AutonomousTabData.ColumnPlayedPiece2.kLeft);
        columnPlayedPiece2Box.addOption("Middle", AutonomousTabData.ColumnPlayedPiece2.kMiddle);
        columnPlayedPiece2Box.addOption("Right", AutonomousTabData.ColumnPlayedPiece2.kRight);

        //put the widget on the shuffleboard
        autonomousTab.add(columnPlayedPiece2Box)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(15, 6)
            .withSize(6, 2);
    }

    /**
    * <b>Column for Piece 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createContainingPreloadBox()
    {
        //create and name the Box
        SendableRegistry.add(containingPreloadBox, "Containing Preload?");
        SendableRegistry.setName(containingPreloadBox, "Containing Preload?");

        //add options to Box
        containingPreloadBox.setDefaultOption("Yes", AutonomousTabData.ContainingPreload.kYes);
        containingPreloadBox.addOption("No", AutonomousTabData.ContainingPreload.kNo);
        //put the widget on the shuffleboard
        autonomousTab.add(containingPreloadBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(7, 0)
            .withSize(4, 2);
    }

    /**
    * <b>Autonomous Commands 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createAutonomousCommandsBox()
    {
        //create and name the Box
        SendableRegistry.add(autonomousCommandsBox, "Auto Commands");
        SendableRegistry.setName(autonomousCommandsBox, "Auto Commands");

        //add options to Box
        autonomousCommandsBox.setDefaultOption("None", AutonomousTabData.AutonomousCommands.kNeither);
        autonomousCommandsBox.addOption("Charge Station", AutonomousTabData.AutonomousCommands.kChargingStation);
        autonomousCommandsBox.addOption("2 Pieces", AutonomousTabData.AutonomousCommands.kTwoGamePieces);

        //put the widget on the shuffleboard
        autonomousTab.add(autonomousCommandsBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(0, 0)
            .withSize(6, 2);
    }

    /**
    * <b>Team Color 2</b> Box
    * <p>Create an entry in the Network Table and add the Box to the Shuffleboard Tab
    */
    private void createDriveToSecondPieceBox()
    {
        //create and name the Box
        SendableRegistry.add(driveToSecondPieceBox, "Drive To Second Piece");
        SendableRegistry.setName(driveToSecondPieceBox, "Drive To Second Piece");

        //add options to Box
        driveToSecondPieceBox.setDefaultOption("Yes", AutonomousTabData.DriveToSecondPiece.kYes);
        driveToSecondPieceBox.addOption("No", AutonomousTabData.DriveToSecondPiece.kNo);
        //put the widget on the shuffleboard
        autonomousTab.add(driveToSecondPieceBox)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(0, 10)
            .withSize(4, 2);
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
             .withPosition(22, 10)
             .withSize(6, 2)
             .getEntry();
    }

    private void updateAutonomousTabData()
    {
        autonomousTabData.startingLocation = startingLocationBox.getSelected();
        autonomousTabData.playPreload = playPreloadBox.getSelected();
        autonomousTabData.moveOntoChargingStation = moveOntoChargingStationBox.getSelected();
        autonomousTabData.pickUpGamePieces = pickUpGamePiecesBox.getSelected();
        autonomousTabData.rowPlayedPiece1 = rowPlayedPiece1Box.getSelected();
        autonomousTabData.columnPlayedPiece1 = columnPlayedPiece1Box.getSelected();
        autonomousTabData.rowPlayedPiece2 = rowPlayedPiece2Box.getSelected();
        autonomousTabData.columnPlayedPiece2 = columnPlayedPiece2Box.getSelected();
        autonomousTabData.containingPreload = containingPreloadBox.getSelected();
        autonomousTabData.autonomousCommands = autonomousCommandsBox.getSelected();
        autonomousTabData.driveToSecondPiece = driveToSecondPieceBox.getSelected();
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
                // successfulDownload.setBoolean(true);
                updateAutonomousTabData();
            }
            else
            {
                // successfulDownload.setBoolean(false);
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
        boolean isPiecesPlayed = (playPreloadBox.getSelected() == AutonomousTabData.PlayPreload.kYes);
        boolean isPiecesPlayedFalse = (playPreloadBox.getSelected() == AutonomousTabData.PlayPreload.kNo);
        boolean isMoveOntoStation = (moveOntoChargingStationBox.getSelected() == AutonomousTabData.MoveOntoChargingStation.kYes);
        boolean isPiecesPickedUp = (pickUpGamePiecesBox.getSelected() == AutonomousTabData.PickUpGamePieces.kNo);
        boolean isRow1 = (rowPlayedPiece1Box.getSelected() == AutonomousTabData.RowPlayedPiece1.kBottom);
        boolean isColumn1 = (columnPlayedPiece1Box.getSelected() == AutonomousTabData.ColumnPlayedPiece1.kLeft);
        boolean isRow2False = (rowPlayedPiece2Box.getSelected() == AutonomousTabData.RowPlayedPiece2.kNone);
        boolean isRow2True = (rowPlayedPiece2Box.getSelected() == AutonomousTabData.RowPlayedPiece2.kBottom);
        boolean isColumn2False = (columnPlayedPiece2Box.getSelected() == AutonomousTabData.ColumnPlayedPiece2.kNone);
        boolean isColumn2True = (columnPlayedPiece2Box.getSelected() == AutonomousTabData.ColumnPlayedPiece2.kLeft);
        boolean isPiecesContained = (containingPreloadBox.getSelected() == AutonomousTabData.ContainingPreload.kYes);
        boolean isNoCommand = (autonomousCommandsBox.getSelected() == AutonomousTabData.AutonomousCommands.kNeither);
        boolean isChargingStationCommand = (autonomousCommandsBox.getSelected() == AutonomousTabData.AutonomousCommands.kChargingStation);
        boolean is2GamePieceCommand = (autonomousCommandsBox.getSelected() == AutonomousTabData.AutonomousCommands.kTwoGamePieces);
        
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

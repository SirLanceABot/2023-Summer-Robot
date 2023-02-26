// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Gatherer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Arm.ArmPosition;
import frc.robot.subsystems.Shoulder.ShoulderPosition;
import frc.robot.commands.AutoAimToPost;
import frc.robot.commands.AutoBalance;
import frc.robot.commands.AutoDriveDistance;
import frc.robot.commands.GrabGamePiece;
import frc.robot.commands.LockWheels;
import frc.robot.commands.MoveArmToScoringPosition;
import frc.robot.commands.MoveShoulderToScoringPosition;
import frc.robot.commands.ReleaseGamePiece;
import frc.robot.commands.SwerveDrive;
import frc.robot.controls.DriverController;
import frc.robot.controls.OperatorController;
import frc.robot.controls.Xbox;
import frc.robot.sensors.Accelerometer4237;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.MainShuffleboard;
import frc.robot.sensors.Vision;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }
	
	private boolean useFullRobot			= false;
	private boolean useBindings				= true;

	private boolean useExampleSubsystem		= false;
	private boolean useAccelerometer		= false;
	private boolean useGyro					= false;
	private boolean useDrivetrain   		= false;
	private boolean useGrabber 				= false;
	private boolean useArm 					= true;
	private boolean useShoulder				= true;
	private boolean useGatherer 			= false;
	private boolean useCandle				= false;
	private boolean useDriverController		= false;
	private boolean useOperatorController 	= true;
	private boolean useMainShuffleboard		= true;
	private boolean useVision				= false;
	private boolean useDataLog				= false;
	
	public final boolean fullRobot;
	public final ExampleSubsystem exampleSubsystem;
	public final Drivetrain drivetrain;
	public final Grabber grabber;
	public final Arm arm;
	public final Shoulder shoulder;
	public final Gatherer gatherer;
	public final Candle4237 candle;
	public final DriverController driverController;
	public final OperatorController operatorController;
	public final Vision vision;
	public final MainShuffleboard mainShuffleboard;
	public final Accelerometer4237 accelerometer;
	public final Gyro4237 gyro;
	public final DataLog log;

	/** 
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 * Use the default modifier so that new objects can only be constructed in the same package.
	 */
	RobotContainer()
	{
		// Create the needed subsystems
		if(useDataLog)
			DataLogManager.start();
			
		log					= (useDataLog)								? DataLogManager.getLog()		: null;

		fullRobot = useFullRobot;
		exampleSubsystem 	= (useFullRobot || useExampleSubsystem)		? new ExampleSubsystem() 		: null;
		accelerometer		= (useFullRobot || useAccelerometer)		? new Accelerometer4237()		: null;
		gyro 				= (useFullRobot || useGyro)					? new Gyro4237()				: null;	
		drivetrain 			= (useFullRobot || useDrivetrain) 			? new Drivetrain(gyro, log) 	: null;
		grabber 			= (useFullRobot || useGrabber) 				? new Grabber() 				: null;
		arm 				= (useFullRobot || useArm) 					? new Arm() 					: null;
		shoulder 			= (useFullRobot || useShoulder) 			? new Shoulder() 				: null;
		gatherer 			= (useFullRobot || useGatherer) 			? new Gatherer() 				: null;
		candle 				= (useFullRobot || useCandle)				? new Candle4237() 				: null;
		driverController 	= (useFullRobot || useDriverController) 	? new DriverController(Constants.Controller.DRIVER) 		: null;
		operatorController 	= (useFullRobot || useOperatorController) 	? new OperatorController(Constants.Controller.OPERATOR)	 	: null;
		mainShuffleboard 	= (useFullRobot || useMainShuffleboard)		? new MainShuffleboard(this)	: null;
		vision 				= (useFullRobot || useVision)				? new Vision()					: null;
		


		// Configure the trigger bindings
		if(useFullRobot || useBindings)
			configureBindings();

		configureSchedulerLog();
	}

	/**
	 * Use this method to define your trigger->command mappings. Triggers can be created via the
	 * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
	 * predicate, or via the named factories in {@link
	 * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
	 * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
	 * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
	 * joysticks}.
	 */
	private void configureBindings()
	{
		configureDriverBindings();
		configureOperatorBindings();
	}

	private void configureDriverBindings()
	{
		if(driverController != null)
        {
			//Axis, driving and rotating
			DoubleSupplier leftYAxis = driverController.getAxisSupplier(Xbox.Axis.kLeftY);
			DoubleSupplier leftXAxis = driverController.getAxisSupplier(Xbox.Axis.kLeftX);
			DoubleSupplier rightXAxis = driverController.getAxisSupplier(Xbox.Axis.kRightX);

			// Start Button
			BooleanSupplier startButton = driverController.getButtonSupplier(Xbox.Button.kStart);
			Trigger startButtonTrigger = new Trigger(startButton);
			if(gyro != null)
			{
				startButtonTrigger.toggleOnTrue(new InstantCommand( () -> { gyro.reset(); } ) );
			}

			//A Button
			BooleanSupplier aButton = driverController.getButtonSupplier(Xbox.Button.kA);
			Trigger aButtonTrigger = new Trigger(aButton);
			if(drivetrain != null && vision != null && operatorController != null)
			{
				aButtonTrigger.onTrue( new AutoAimToPost(drivetrain, vision)
							.andThen( () -> operatorController.setRumble(0.5, 0.5, 0.5))         
							.andThen( () -> driverController.setRumble(0.5, 0.5, 0.5)) );							// .andThen( () -> operatorController.setRumble(0.5))         
							// .andThen( () -> driverController.setRumble(0.5))
							// .andThen( new WaitCommand(0.5))
							// .andThen( () -> operatorController.setRumble(0.0))
							// .andThen( () -> driverController.setRumble(0.0)));
			}

			//B Button
			BooleanSupplier bButton = driverController.getButtonSupplier(Xbox.Button.kB);
			Trigger bButtonTrigger = new Trigger(bButton);
			if(drivetrain != null)
			{
				bButtonTrigger.onTrue( new AutoBalance(drivetrain, gyro));
			}
			// bButtonTrigger.onTrue( new AutoDriveDistance(drivetrain, 0.5, 0.0, 1.0)
			// 			  .andThen( new AutoBalance(drivetrain, gyro)));

			//X Button-lockwheels
			BooleanSupplier xButton = driverController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);
			if(drivetrain != null)
			{
				xButtonTrigger.onTrue( new RunCommand( () -> drivetrain.lockWheels(), drivetrain )
										.until(driverController.tryingToMoveRobot()) );
				// xButtonTrigger.onTrue( Commands.run( () -> drivetrain.lockWheels(), drivetrain )
				// 					.until(driverController.tryingToMoveRobot()) );
			}
			
			//Y Button
			BooleanSupplier yButton = driverController.getButtonSupplier(Xbox.Button.kY);
			Trigger yButtonTrigger = new Trigger(yButton);
			yButtonTrigger.onTrue( new InstantCommand(() -> driverController.setRumble(0.5, 0.0, 0.5)) );
			// yButtonTrigger.onTrue( new AutoAimToPost(drivetrain, vision)         
			// 			  .andThen( () -> driverController.setRumble(0.5))
			// 			  .andThen( new WaitCommand(0.5))
			// 			  .andThen( () -> driverController.setRumble(0.0)));
			// yButtonTrigger.onTrue( new AutoAimToPost(drivetrain, vision)).andThen(() -> driverController.rumbleNow()));

			//Left Trigger
			BooleanSupplier leftTrigger = driverController.getButtonSupplier(Xbox.Button.kLeftTrigger);
			Trigger lefTriggerTrigger = new Trigger(leftTrigger);
			// lefTriggerTrigger.onTrue( new AutoBalance(drivetrain, gyro));

			if(drivetrain != null)
				drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
			// drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, () -> 0.5, () -> 0.0, () -> 0.0, false));
			
        }
	}

	private void configureOperatorBindings()
	{
		if(operatorController != null)
		{
			//Left trigger 
			BooleanSupplier leftTrigger = operatorController.getButtonSupplier(Xbox.Button.kLeftTrigger);
			Trigger leftTriggerTrigger = new Trigger(leftTrigger);
			if(shoulder != null)
			{
				// leftTriggerTrigger.whileTrue( new InstantCommand (() -> shoulder.moveUp(), shoulder));
				leftTriggerTrigger.whileTrue( new StartEndCommand(
					() -> shoulder.moveUp(),
					() -> shoulder.off(),
					shoulder));
			}

			//Right trigger 
			BooleanSupplier rightTrigger = operatorController.getButtonSupplier(Xbox.Button.kRightTrigger);
			Trigger rightTriggerTrigger = new Trigger(rightTrigger);
			if(arm != null)
			{
				// rightTriggerTrigger.whileTrue( new InstantCommand (() -> arm.extendArm(), arm));
				rightTriggerTrigger.whileTrue( new StartEndCommand(
					() -> arm.extendArm(),
					() -> arm.off(),
					arm));
			}

			//Left bumper
			BooleanSupplier leftBumper = operatorController.getButtonSupplier(Xbox.Button.kLeftBumper);
			Trigger leftBumperTrigger = new Trigger(leftBumper);
			if(shoulder != null)
			{
				// leftBumperTrigger.whileTrue( new InstantCommand (() -> shoulder.moveDown(), shoulder));
				leftBumperTrigger.whileTrue( new StartEndCommand(
					() -> shoulder.moveDown(),
					() -> shoulder.off(),
					shoulder));
			}
			
			//Right bumper
			BooleanSupplier rightBumper = operatorController.getButtonSupplier(Xbox.Button.kRightBumper);
			Trigger rightBumperTrigger = new Trigger(rightBumper);
			if(arm != null)
			{
				// rightBumperTrigger.whileTrue( new InstantCommand (() -> arm.retractArm(), arm));
				rightBumperTrigger.whileTrue( new StartEndCommand(
					() -> arm.retractArm(),
					() -> arm.off(),
					arm));
			}

			//Dpad up button
			BooleanSupplier dPadUp = operatorController.getDpadSupplier(Xbox.Dpad.kUp);
			Trigger dPadUpTrigger = new Trigger(dPadUp);
			if(arm != null && shoulder != null)
			{
				dPadUpTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kHigh)
							// .andThen( new PrintCommand("Shoulder done moving"))
							// .andThen( new WaitCommand(2))
							.andThen( new MoveArmToScoringPosition(arm, ArmPosition.kHigh)));
							// .andThen(new PrintCommand("Arm done moving")));
				// dPadUpTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kHigh));
			}
			// if(shoulder != null)
			// {
			// 	dPadUpTrigger.onTrue(( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kHigh))
			// 				 .andThen( new PrintCommand("DPad UP")));
			// }
			
			//Dpad down button
			BooleanSupplier dPadDown = operatorController.getDpadSupplier(Xbox.Dpad.kDown);
			Trigger dPadDownTrigger = new Trigger(dPadDown);
			if(arm != null && shoulder != null)
			{
				dPadDownTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kGather)
							  .andThen( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather)));
				// dPadDownTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather));
				// dPadDownTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kGather));
			}
			// if(shoulder != null)
			// {
			// 	dPadDownTrigger.onTrue(( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather))
			// 				 .andThen( new PrintCommand("DPad DOWN")));
			// }
			
			//Dpad left button
			BooleanSupplier dPadLeft = operatorController.getDpadSupplier(Xbox.Dpad.kLeft);
			Trigger dPadLeftTrigger = new Trigger(dPadLeft);
			if(arm != null && shoulder != null)
			{
				dPadLeftTrigger.onTrue(  new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kMiddle)
							   .andThen( new MoveArmToScoringPosition(arm, ArmPosition.kMiddle)));
				// dPadLeftTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kMiddle));
			}
			// if(shoulder != null)
			// {
			// 	dPadLeftTrigger.onTrue(( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kMiddle))
			// 				   .andThen( new PrintCommand("DPad LEFT")));
			// }

			//Dpad right button
			BooleanSupplier dPadRight = operatorController.getDpadSupplier(Xbox.Dpad.kRight);
			Trigger dPadRightTrigger = new Trigger(dPadRight);
			if(arm != null && shoulder != null)
			{
				dPadRightTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kLow)
							    .andThen( new MoveArmToScoringPosition(arm, ArmPosition.kLow)));
					// dPadRightTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kLow));
			}
			// if(shoulder != null)
			// {
			// 	dPadRightTrigger.onTrue(( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kLow))
			// 				 	.andThen( new PrintCommand("DPad RIGHT")));
			// }

			//X button
			BooleanSupplier xButton = operatorController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);
			if(candle != null)
			{
				xButtonTrigger.whileTrue( new InstantCommand (() -> candle.signalCube(), candle));
				// xButtonTrigger.toggleOnTrue(new StartEndCommand( () -> { shoulder.moveDown(); }, () -> { shoulder.off(); }, shoulder ) );
				//xButtonTrigger.toggleOnTrue(new SuctionControl));
			}	

			//Y button 
			BooleanSupplier yButton = operatorController.getButtonSupplier(Xbox.Button.kY);
			Trigger yButtonTrigger = new Trigger(yButton);
			if(candle != null)
			{
				yButtonTrigger.whileTrue( new InstantCommand (() -> candle.signalCone(), candle));
				//yButtonTrigger.toggleOnTrue( new MoveWrist());
			}

			//B button 
			BooleanSupplier bButton = operatorController.getButtonSupplier(Xbox.Button.kB);
			Trigger bButtonTrigger = new Trigger(yButton);
			if(candle != null)
			{
				bButtonTrigger.whileTrue( new InstantCommand (() -> candle.turnOffLight(), candle));
			}

			//A button 
			BooleanSupplier aButton = operatorController.getButtonSupplier(Xbox.Button.kA);
			Trigger aButtonTrigger = new Trigger(aButton);
			if(arm != null && shoulder != null)
			{
				// aButtonTrigger.onTrue( new ReleaseGamePiece(grabber));
	
				aButtonTrigger.whileTrue( new InstantCommand (() -> shoulder.moveDown(), shoulder)
							.alongWith( new InstantCommand (() -> arm.retractArm(), arm)));
			// DoubleSupplier leftYAxis = operatorController.getAxisSupplier(Xbox.Axis.kLeftY);
				// shoulder.setDefaultCommand(new RunCommand( () -> { shoulder.on(leftYAxis.getAsDouble()/2.0); }, shoulder) );
			}
		}
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand()
	{
		// AutoBalance command = new AutoBalance(drivetrain, gyro);
		// AutoDriveDistance command = new AutoDriveDistance(drivetrain, 0.2, 0.0, 2.0);
		// Command command = new AutoDriveDistance(drivetrain, gyro, -1.0, 0.0, 3.90)
		// 		// .andThen( new AutoBalance(drivetrain, gyro))
		// 		// .andThen( new AutoDriveDistance(drivetrain, -0.5, 0.0, 1.5))
		// 		.andThen( new AutoDriveDistance(drivetrain, gyro, 1.0, 0.0, 1.90))
		// 		.andThen( new AutoBalance(drivetrain, gyro))
		// 		.andThen( new LockWheels(drivetrain));
		// return command;
		return null;
	}

	
	/////////////////////////////////////////
	// Command Event Loggers
	/////////////////////////////////////////
	void configureSchedulerLog()
	{
		boolean useShuffleBoardLog = true;
		StringLogEntry commandLogEntry = null;

		if(useShuffleBoardLog || useDataLog)
		{
		// Set the scheduler to log events for command initialize, interrupt,
		// finish, execute
		// Log to the ShuffleBoard and the WPILib data log tool.
		// If ShuffleBoard is recording these events are added to the recording. Convert
		// recording to csv and they show nicely in Excel. 
		// If using data log tool, the recording is automatic so run that tool to retrieve and convert the log.
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandInitialize(
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " initialized");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command initialized", command.getName(), EventImportance.kNormal);
						System.out.println("Command initialized " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandInterrupt(
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " interrupted");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command interrupted", command.getName(), EventImportance.kNormal);
						System.out.println("Command interrupted " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandFinish(
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " finished");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command finished", command.getName(), EventImportance.kNormal);
						System.out.println("Command finished " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________

		CommandScheduler.getInstance()
			.onCommandExecute( // this can generate a lot of events
				command ->
				{
					if(useDataLog) commandLogEntry.append(command.getClass() + " " + command.getName() + " executed");
					if(useShuffleBoardLog)
					{
						Shuffleboard.addEventMarker("Command executed", command.getName(), EventImportance.kNormal);
						// System.out.println("Command executed " + command.getName());
					}
				}
			);
		//_________________________________________________________________________________
		}
	}

}


// ------------------------------------------------------------------------------------------
// COMMAND EXAMPLES
// ------------------------------------------------------------------------------------------
// 
// Here are other options ways to create "Suppliers"
// DoubleSupplier leftYAxis =  () -> { return driverController.getRawAxis(Xbox.Axis.kLeftY) * 2.0; };
// DoubleSupplier leftXAxis =  () -> { return driverController.getRawAxis(Xbox.Axis.kLeftX) * 2.0; };
// DoubleSupplier rightXAxis = () -> { return driverController.getRawAxis(Xbox.Axis.kRightX) * 2.0; };
// BooleanSupplier aButton =   () -> { return driverController.getRawButton(Xbox.Button.kA); };
//
// ------------------------------------------------------------------------------------------
//
// Here are 4 ways to perform the "LockWheels" command
// Press the X button to lock the wheels, unlock when the driver moves left or right axis
// 
// Option 1
// xButtonTrigger.onTrue( new RunCommand( () -> drivetrain.lockWheels(), drivetrain )
//						.until(driverController.tryingToMoveRobot()) );
//
// Option 2
// xButtonTrigger.onTrue(new LockWheels(drivetrain)
// 						.until(driverController.tryingToMoveRobot()));
//
// Option 3
// xButtonTrigger.onTrue(new FunctionalCommand(
// 		() -> {}, 								// onInit
// 		() -> { drivetrain.lockWheels(); }, 	// onExec
// 		(interrupted) -> {}, 					// onEnd
// 		driverController.tryingToMoveRobot(),	// isFinished
// 		drivetrain ) );							// requirements
// 
// Option 4
// xButtonTrigger.onTrue( run( () -> drivetrain.lockWheels() )	//run(drivetrain::lockWheels)
// 						.until(driverController.tryingToMoveRobot()) );
//

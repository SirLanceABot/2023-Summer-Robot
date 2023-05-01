// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import javax.swing.plaf.TreeUI;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Candle4237.LedStatus;
import frc.robot.Constants.SuctionState;
import frc.robot.Constants.TargetPosition;
import frc.robot.commands.AlignToPost;
import frc.robot.commands.DriveToSubstationSensor;
import frc.robot.commands.AutoBalance;
import frc.robot.commands.AutoCommandList;
import frc.robot.commands.ArcadeAutoDriveDistance;
import frc.robot.commands.ClampCone;
import frc.robot.commands.ExtendScorer;
import frc.robot.commands.ExtendScorerCube;
import frc.robot.commands.ExtendScorerSubstation;
import frc.robot.commands.GrabGamePiece;
import frc.robot.commands.LockWheels;
import frc.robot.commands.SuctionControl;
import frc.robot.commands.MoveArmToScoringPosition;
import frc.robot.commands.MoveShoulderToScoringPosition;
import frc.robot.commands.NewDriveToSubstation;
import frc.robot.commands.ReleaseGamePiece;
import frc.robot.commands.RetractScorer;
import frc.robot.commands.SwerveDrive;
import frc.robot.controls.DriverController;
import frc.robot.controls.OperatorController;
import frc.robot.controls.Xbox;
import frc.robot.sensors.Accelerometer4237;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.MainShuffleboard;
import frc.robot.sensors.Vision;
import frc.robot.sensors.Ultrasonic4237;

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
	private boolean useScorer				= false;
	private boolean useBindings				= false;

	private boolean useExampleSubsystem		= false;
	private boolean useAccelerometer		= false;
	private boolean useGyro					= false;
	private boolean useDrivetrain   		= false;
	private boolean useGrabber 				= false;
	private boolean useWrist				= false;
	private boolean useArm 					= false;
	private boolean useShoulder				= false;
	private boolean useGatherer 			= false;
	private boolean useCandle				= false;
	private boolean useDriverController		= false;
	private boolean useOperatorController 	= false;
	private boolean useMainShuffleboard		= false;
	private boolean useVision				= false;
	private boolean useUltrasonic			= false;

	private boolean useDataLog				= false;
	
	
	public final boolean fullRobot;
	public final ExampleSubsystem exampleSubsystem;
	public final Drivetrain drivetrain;
	public final Grabber grabber;
	public final Wrist wrist;
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
	private final Ultrasonic4237 ultrasonic;
	// public final PowerDistribution pdh;
	public final Compressor compressor;
	public DataLog log = null;
	// public static final DataLog log = DataLogManager.getLog();


	/** 
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 * Use the default modifier so that new objects can only be constructed in the same package.
	 */
	RobotContainer()
	{
		// Create the needed subsystems
		if(useFullRobot || useDataLog)
		{
			DataLogManager.start();
			log = DataLogManager.getLog();
		}
			
			
		// log					= (useDataLog)									? DataLogManager.getLog()								: null;

		fullRobot 			= (useFullRobot);
		exampleSubsystem 	= (useExampleSubsystem)							? new ExampleSubsystem() 								: null;
		accelerometer		= (useAccelerometer)							? new Accelerometer4237()								: null;
		gyro 				= (useFullRobot || useGyro)						? new Gyro4237()										: null;	
		drivetrain 			= (useFullRobot || useDrivetrain) 				? new Drivetrain(gyro, log) 							: null;
		grabber 			= (useFullRobot || useScorer || useGrabber) 	? new Grabber(log) 										: null;
		wrist				= (useFullRobot || useScorer || useWrist)		? new Wrist()											: null;
		arm 				= (useFullRobot || useScorer || useArm) 		? new Arm(log) 											: null;
		shoulder 			= (useFullRobot || useScorer || useShoulder) 	? new Shoulder(log) 									: null;
		gatherer 			= (useGatherer) 								? new Gatherer() 										: null;
		candle 				= (useFullRobot || useCandle)					? new Candle4237() 										: null;
		driverController 	= (useFullRobot || useDriverController) 		? new DriverController(Constants.Controller.DRIVER) 	: null;
		operatorController 	= (useFullRobot || useOperatorController) 		? new OperatorController(Constants.Controller.OPERATOR)	: null;
		mainShuffleboard 	= (useFullRobot || useMainShuffleboard)			? new MainShuffleboard(this)							: null;
		vision 				= (useFullRobot || useVision)					? new Vision()											: null;
		compressor			= (useGrabber 	|| useWrist)					? new Compressor(0, PneumaticsModuleType.CTREPCM)		: null;
		ultrasonic			= (useFullRobot || useUltrasonic)				? new Ultrasonic4237()									: null;

		// pdh = new PowerDistribution(1, ModuleType.kRev);
		// compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
		

		// Configure the trigger bindings
		if(useFullRobot || useBindings)
			configureBindings();

		// configureSchedulerLog();
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
			DoubleSupplier leftYAxisCrawl = driverController.getAxisSupplier(Xbox.Axis.kLeftY, 0.15);
			DoubleSupplier leftXAxisCrawl = driverController.getAxisSupplier(Xbox.Axis.kLeftX, 0.15);
			DoubleSupplier rightXAxisCrawl = driverController.getAxisSupplier(Xbox.Axis.kRightX, 0.5);
			DoubleSupplier leftYAxisCrawlCommunity = driverController.getAxisSupplier(Xbox.Axis.kLeftY, 0.25);
			DoubleSupplier leftXAxisCrawlCommunity = driverController.getAxisSupplier(Xbox.Axis.kLeftX, 0.25);
			DoubleSupplier rightXAxisCrawlCommunity = driverController.getAxisSupplier(Xbox.Axis.kRightX, 0.5);
			DoubleSupplier zero = () -> 0.0;

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
			if(drivetrain != null && gyro != null && vision != null && candle != null)
			{
				aButtonTrigger.onTrue( new ParallelCommandGroup(
											new AlignToPost(drivetrain, gyro, vision),
											new SequentialCommandGroup(
												new WaitCommand(0.2),
												new ConditionalCommand(
													new MoveShoulderToScoringPosition(shoulder, TargetPosition.kLimelight), 
													new PrintCommand("Target Not Found"),
													() -> vision.foundTarget())))
							  .andThen( new ConditionalCommand(
											new RunCommand( () -> candle.signalGreen()),
											new RunCommand( () -> candle.signalWhite()),
											() -> vision.isAligned()))
							  .until( driverController.tryingToMoveRobot()));


				// aButtonTrigger.onTrue( new AutoAimToPost(drivetrain, gyro, vision, candle)
				// 			  .andThen( new ConditionalCommand(
				// 							new RunCommand( () -> candle.signalGreen()),
				// 							new RunCommand( () -> candle.signalWhite()),
				// 							() -> vision.isAligned()))
				// 			  .until( driverController.tryingToMoveRobot()));
			}

			//B Button
			BooleanSupplier bButton = driverController.getButtonSupplier(Xbox.Button.kB);
			Trigger bButtonTrigger = new Trigger(bButton);
			if(drivetrain != null)
			{
				// bButtonTrigger.toggleOnTrue(new SwerveDriveXOnly(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
				bButtonTrigger.whileTrue(new SwerveDrive(drivetrain, leftYAxisCrawl, zero, zero, true));
			}

			//X Button-lockwheels
			BooleanSupplier xButton = driverController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);
			if(drivetrain != null)
			{
				xButtonTrigger.onTrue( new RunCommand( () -> drivetrain.lockWheels(), drivetrain )
							  .until(driverController.tryingToMoveRobot()) );
			}
			
			//Y Button
			BooleanSupplier yButton = driverController.getButtonSupplier(Xbox.Button.kY);
			Trigger yButtonTrigger = new Trigger(yButton);
			// if(drivetrain != null)
			{
				// yButtonTrigger.toggleOnTrue(new SwerveDriveCrawl(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
				// yButtonTrigger.toggleOnTrue(new SwerveDrive(drivetrain, leftYAxisCrawl, leftXAxisCrawl, rightXAxisCrawl, true));
			}

			//Right trigger 
			BooleanSupplier rightTrigger = driverController.getButtonSupplier(Xbox.Button.kRightTrigger);
			Trigger rightTriggerTrigger = new Trigger(rightTrigger);
			if(grabber != null)
			{
				rightTriggerTrigger.onTrue( new SuctionControl(grabber, SuctionState.kOff)
								   .andThen( new  WaitCommand(0.5))
								   .andThen( new InstantCommand( () -> grabber.closeSolenoid())));
			}

			// //Right Bumper
			BooleanSupplier rightBumper = driverController.getButtonSupplier(Xbox.Button.kRightBumper);
			Trigger rightBumperTrigger = new Trigger(rightBumper);
			if(drivetrain != null)
			{
				rightBumperTrigger.whileTrue(new SwerveDrive(drivetrain, leftYAxisCrawlCommunity, leftXAxisCrawlCommunity, rightXAxisCrawlCommunity, true));

			}
			

			//Left trigger 
			BooleanSupplier leftTrigger = driverController.getButtonSupplier(Xbox.Button.kLeftTrigger);
			Trigger leftTriggerTrigger = new Trigger(leftTrigger);
			// if(drivetrain != null && gyro != null && vision != null)
			// {
			// 	// leftTriggerTrigger.whileTrue( new NewDriveToSubstation(drivetrain, ultrasonic, leftYAxis, leftXAxis, rightXAxis, true, 0.15));
			// }

			//Left Bumper
			BooleanSupplier leftBumper = driverController.getButtonSupplier(Xbox.Button.kLeftBumper);
			Trigger leftBumperTrigger = new Trigger(leftBumper);
			if(drivetrain != null)
			{

				leftBumperTrigger.whileTrue(new SwerveDrive(drivetrain, leftYAxisCrawl, leftXAxisCrawl, rightXAxisCrawl, true));
				// leftBumperTrigger.whileTrue(
				// 	new ConditionalCommand(

				// 		new PrintCommand("Regular Swerve").andThen(
				// 		new SwerveDrive(drivetrain, leftYAxis, leftXAxis, rightXAxis, true)),
				// 		new PrintCommand("Crawl Swerve").andThen( 
				// 		new SwerveDrive(drivetrain, leftYAxisCrawl, leftXAxisCrawl, rightXAxisCrawl, true)), 
				// 		() -> ultrasonic.getDistance() > 6.0));
				// leftBumperTrigger.toggleOnFalse(new DriveToSubstationSensor(drivetrain, gyro, ultrasonic));
			}


			//Dpad down button
			BooleanSupplier dPadDown = driverController.getDpadSupplier(Xbox.Dpad.kDown);
			Trigger dPadDownTrigger = new Trigger(dPadDown);
			// if(shoulder != null)
			// {
			// 	dPadDownTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kStartingPosition));
			// }

			// Rumble when gamepiece has full suction
			BooleanSupplier vacuumSuction = grabber.vacuumSuctionSupplier();
			Trigger vacuumSuctionTrigger = new Trigger(vacuumSuction);
			if(grabber != null)
			{
				vacuumSuctionTrigger.onTrue( new InstantCommand(() -> driverController.setRumble(0.5, 0.5, 0.5)));
			}
			
			// Default Command
			if(drivetrain != null)
			{
				drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
			}
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
					() -> shoulder.hold(),
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
					() -> shoulder.hold(),
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
			if(shoulder != null && arm != null && wrist != null && grabber != null)
			{
				dPadUpTrigger.onTrue( new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kHighCone));
				// dPadUpTrigger.onTrue( new ExtendScorer(shoulder, arm, wrist, TargetPosition.kHigh)
				// 			 .andThen( new ReleaseGamePiece(grabber))
				// 			 );
				// dPadUpTrigger.onTrue( new ScoreGamePieceV2(shoulder, arm, grabber, wrist, TargetPosition.kHigh));
				// dPadUpTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kHigh)
				// 			// .andThen( new PrintCommand("Shoulder done moving"))
				// 			// .andThen( new WaitCommand(2))
				// 			.andThen( new MoveArmToScoringPosition(arm, ArmPosition.kHigh)));
				// 			// .andThen(new PrintCommand("Arm done moving")));
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
			if(shoulder != null && arm != null && wrist != null)
			{
				dPadDownTrigger.onTrue( new RetractScorer(shoulder, arm, wrist, TargetPosition.kGather));
				// dPadDownTrigger.onTrue( new ReturnToGather(arm, shoulder));
				// dPadDownTrigger.onTrue( new ScoreGamePieceV2(shoulder, arm, grabber, wrist, TargetPosition.kGather));
				// dPadDownTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kGather)
				// 			  .andThen( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather)));
				// // dPadDownTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather));
				// // dPadDownTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kGather));
			}
			// if(shoulder != null)
			// {
			// 	dPadDownTrigger.onTrue(( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather))
			// 				 .andThen( new PrintCommand("DPad DOWN")));
			// }
			
			//Dpad left button
			BooleanSupplier dPadLeft = operatorController.getDpadSupplier(Xbox.Dpad.kLeft);
			Trigger dPadLeftTrigger = new Trigger(dPadLeft);
			if(shoulder != null && arm != null && wrist != null && grabber != null)
			{
				dPadLeftTrigger.onTrue(
					new ConditionalCommand(
						new RetractScorer(shoulder, arm, wrist, TargetPosition.kMiddleCone),
						new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kMiddleCone),
						() -> shoulder.getPosition() > TargetPosition.kMiddleCone.shoulder)
					);
				// dPadLeftTrigger.onTrue(
				// 	new ConditionalCommand(
				// 		new RetractScorer(shoulder, arm, wrist, TargetPosition.kMiddle),
				// 		new ExtendScorer(shoulder, arm, wrist, TargetPosition.kMiddle)
				// 		.andThen( new ReleaseGamePiece(grabber)),
				// 		() -> shoulder.getPosition() > TargetPosition.kMiddle.shoulder)
				// 	);
				// dPadLeftTrigger.onTrue( new ScoreGamePieceV2(shoulder, arm, grabber, wrist, TargetPosition.kMiddle));
				// dPadLeftTrigger.onTrue(  new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kMiddle)
				// 			   .andThen( new MoveArmToScoringPosition(arm, ArmPosition.kMiddle)));
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
			if(shoulder != null && arm != null && wrist != null && grabber != null)
			{
				dPadRightTrigger.onTrue( new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kSubstation));

				// dPadRightTrigger.onTrue( new ExtendScorerSubstation(shoulder, arm, grabber));

				// dPadRightTrigger.onTrue(
				// 	new ConditionalCommand(
				// 		new RetractScorer(shoulder, arm, grabber, wrist, TargetPosition.kLow),
				// 		new ExtendScorer(shoulder, arm, grabber, wrist, TargetPosition.kLow)
				// 		.andThen( new ReleaseGamePiece(grabber)),
				// 		() -> shoulder.getPosition() > TargetPosition.kLow.shoulder)
				// 	);
				// dPadRightTrigger.onTrue( new ScoreGamePieceV2(shoulder, arm, grabber, wrist, TargetPosition.kLow));
				// dPadRightTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kLow)
				// 			    .andThen( new MoveArmToScoringPosition(arm, ArmPosition.kLow)));
					// dPadRightTrigger.onTrue( new MoveArmToScoringPosition(arm, ArmPosition.kLow));
			}
			// if(shoulder != null)
			// {
			// 	dPadRightTrigger.onTrue(( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kLow))
			// 				 	.andThen( new PrintCommand("DPad RIGHT")));
			// }

			// Start Button
			BooleanSupplier startButton = operatorController.getButtonSupplier(Xbox.Button.kStart);
			Trigger startButtonTrigger = new Trigger(startButton);
			if(shoulder != null)
			{
				startButtonTrigger.onTrue( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kStartingPosition));
			}

			// Start Button and dPad Up
			Trigger startAndUpTrigger  = startButtonTrigger.and(dPadUpTrigger);
			if(shoulder != null && arm != null && wrist != null && grabber != null)
			{
				startAndUpTrigger.onTrue( new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kHighCube));
				
				// startAndUpTrigger.onTrue( new ExtendScorerCube(shoulder, arm, wrist, TargetPosition.kHighCube));
			}

			// Start Button and dPad Left
			Trigger startAndLeftTrigger  = startButtonTrigger.and(dPadLeftTrigger);
			if(shoulder != null && arm != null && wrist != null && grabber != null)
			{
				startAndLeftTrigger.onTrue(
					new ConditionalCommand(
						new RetractScorer(shoulder, arm, wrist, TargetPosition.kMiddleCube),
						new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kMiddleCube),
						() -> shoulder.getPosition() > TargetPosition.kMiddleCube.shoulder)
					);

				// startAndLeftTrigger.onTrue( new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kMiddleCube));

				// startAndLeftTrigger.onTrue( new ExtendScorerCube(shoulder, arm, wrist, TargetPosition.kMiddleCube));
			}

			// Start Button and dPad Down
			Trigger startAndDownTrigger  = startButtonTrigger.and(dPadDownTrigger);
			if(shoulder != null && arm != null && wrist != null && grabber != null)
			{
				startAndDownTrigger.onTrue(
					new ConditionalCommand(
						new RetractScorer(shoulder, arm, wrist, TargetPosition.kLowCube),
						new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kLowCube),
						() -> shoulder.getPosition() > TargetPosition.kLowCube.shoulder)
					);

				// startAndDownTrigger.onTrue( new ExtendScorer(shoulder, arm, wrist, grabber, TargetPosition.kLowCube));

				// startAndLeftTrigger.onTrue( new ExtendScorerCube(shoulder, arm, wrist, TargetPosition.kMiddleCube));
			}

			//X button
			BooleanSupplier xButton = operatorController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);
			if(candle != null)
			{
				
				xButtonTrigger.whileTrue( new StartEndCommand(() -> candle.signalCube(), () ->  candle.turnOffLight(), candle));
				// xButtonTrigger.onTrue( new InstantCommand (() -> candle.signalCube(), candle))
				// 			  .onFalse( new InstantCommand (() -> candle.turnOffLight(), candle));
				// xButtonTrigger.toggleOnTrue(new StartEndCommand( () -> { shoulder.moveDown(); }, () -> { shoulder.off(); }, shoulder ) );
				//xButtonTrigger.toggleOnTrue(new SuctionControl));
			}	

			//Y button 
			BooleanSupplier yButton = operatorController.getButtonSupplier(Xbox.Button.kY);
			Trigger yButtonTrigger = new Trigger(yButton);
			if(candle != null)
			{
				yButtonTrigger.whileTrue( new StartEndCommand(() -> candle.signalCone(), () ->  candle.turnOffLight(), candle));
				// yButtonTrigger.onTrue( new MoveArmToScoringPosition(arm, TargetPosition.kClamp)
				// 			  .andThen( new MoveShoulderToScoringPosition(shoulder, TargetPosition.kClamp)));

			// 				  .onFalse( new InstantCommand (() -> candle.turnOffLight(), candle));
				//yButtonTrigger.toggleOnTrue( new MoveWrist());
			}

			//B button 
			BooleanSupplier bButton = operatorController.getButtonSupplier(Xbox.Button.kB);
			Trigger bButtonTrigger = new Trigger(bButton);
			if(wrist != null)
			{
				// wrist
				bButtonTrigger.toggleOnTrue( new StartEndCommand( 
					() -> wrist.wristUp(), 
					() -> wrist.wristDown(),
					wrist));
				// bButtonTrigger.toggleOnFalse( new MoveWristDown(grabber));

			}

			//A button 
			BooleanSupplier aButton = operatorController.getButtonSupplier(Xbox.Button.kA);
			Trigger aButtonTrigger = new Trigger(aButton);
			if(grabber != null)
			{
				// suction
				aButtonTrigger.onTrue( new InstantCommand( () -> grabber.grabGamePiece()));
				// aButtonTrigger.onTrue(grabber.runVacuum());

				// aButtonTrigger.toggleOnTrue( new StartEndCommand(
				// 	() -> grabber.grabGamePiece(),
				// 	() -> grabber.releaseGamePiece(),
				// 	grabber));
				
				
				// aButtonTrigger.onTrue( new ReleaseGamePiece(grabber));
	
				// aButtonTrigger.toggleOnTrue(new GrabGamePiece(grabber));
				// aButtonTrigger.toggleOnFalse( new ReleaseGamePiece(grabber));
				// 			  .andThen(new ReleaseGamePiece(grabber)));
				
				// aButtonTrigger.toggleOnFalse(new MoveWristDown(grabber));
			// DoubleSupplier leftYAxis = operatorController.getAxisSupplier(Xbox.Axis.kLeftY);
				// shoulder.setDefaultCommand(new RunCommand( () -> { shoulder.on(leftYAxis.getAsDouble()/2.0); }, shoulder) );
			}

			// Rumble when shoulder encoder resets
			BooleanSupplier shoulderEncoderReset = shoulder.encoderResetSupplier();
			Trigger shoulderEncoderResetTrigger = new Trigger(shoulderEncoderReset);
			shoulderEncoderResetTrigger.onTrue( new InstantCommand(() -> operatorController.setRumble(0.5, 0.5, 0.5)));

			// Default Command
			if(candle != null)
			{
				candle.setDefaultCommand(new RunCommand (() -> candle.signalRed(), candle));
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
		if(mainShuffleboard != null && mainShuffleboard.autoCommandList != null)
        {
            return mainShuffleboard.autoCommandList;
        }
		else
		{
			// Command command = new GrabGamePiece(grabber)
			Command command = null;
			 //command = new InstantCommand(() -> grabber.grabGamePiece())

								// .andThen( new WaitCommand(0.5))
								// .andThen( new ScoreGamePieceV2(shoulder, arm, grabber, wrist, TargetPosition.kMiddle))
								// .andThen( new ScoreGamePieceV2(shoulder, arm, grabber, wrist, TargetPosition.kGather))
								// .andThen( new InstantCommand(() -> compressor.disable()))
								// .andThen( new MoveWristUp(wrist))
								// .andThen( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kMiddle))
								// .andThen( new InstantCommand(() -> wrist.wristUp()))
								// .andThen( new WaitCommand(0.5))
								// .andThen( new MoveArmToScoringPosition(arm, ArmPosition.kMiddle))
								// .andThen( new ReleaseGamePiece(grabber))
								// .andThen( new InstantCommand(() -> grabber.releaseGamePiece()))
								// .andThen( new ScoreGamePiece( arm, shoulder, ArmPosition.kGather, ShoulderPosition.kGather))
								// .andThen( new MoveWristDown(wrist))

								// .andThen( new MoveArmToScoringPosition(arm, ArmPosition.kGather))
								// .andThen( new InstantCommand(() -> wrist.wristDown()))
								
								// .andThen( new MoveShoulderToScoringPosition(shoulder, ShoulderPosition.kGather));
								// .andThen( new AutoDriveDistance(drivetrain, gyro, -2.0, 0.0, 3.90))
								// .andThen( new WaitCommand(1.0))
								// .andThen( new AutoDriveDistance(drivetrain, gyro, -1.7, 0.0, 1.8))
								// .andThen( new AutoDriveDistance(drivetrain, gyro, 1.7, 0.0, 1.50))
								// .andThen( new AutoBalance(drivetrain, gyro))
								// .andThen( new LockWheels(drivetrain))
								// .andThen( new InstantCommand(() -> compressor.enableDigital()));

			// AutoBalance command = new AutoBalance(drivetrain, gyro);
			// AutoDriveDistance command = new AutoDriveDistance(drivetrain, 0.2, 0.0, 2.0);
			// Command command = new AutoDriveDistance(drivetrain, gyro, -1.0, 0.0, 3.90)
			// 		// .andThen( new AutoBalance(drivetrain, gyro))
			// 		// .andThen( new AutoDriveDistance(drivetrain, -0.5, 0.0, 1.5))
					
			// return command;
			return command;
		}
	}

	public void resetRobot()
	{
		gyro.reset();
		System.out.println("Gyro Reset");
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

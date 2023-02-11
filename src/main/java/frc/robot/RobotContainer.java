// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Gatherer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Candle4237;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Shoulder;
import frc.robot.commands.LockWheels;
import frc.robot.commands.SwerveDrive;
import frc.robot.controls.DriverController;
import frc.robot.controls.OperatorController;
import frc.robot.controls.Xbox;
import frc.robot.sensors.Accelerometer4237;
import frc.robot.sensors.Gyro4237;
import frc.robot.shuffleboard.AutonomousTabData;
import frc.robot.shuffleboard.MainShuffleboard;
import frc.robot.vision.Vision;

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
	private boolean useDrivetrain   		= true;
	private boolean useGrabber 				= false;
	private boolean useArm 					= false;
	private boolean useShoulder				= false;
	private boolean useGatherer 			= false;
	private boolean useCandle				= false;
	private boolean useDriverController		= true;
	private boolean useOperatorController 	= false;
	private boolean useAutonomousTabData	= false;
	private boolean useMainShuffleboard		= false;
	private boolean useVision				= false;
	private boolean useDataLog				= false;


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
	public final AutonomousTabData autonomousTabData;
	public final MainShuffleboard mainShuffleboard;
	//FIXME should these be done the same way
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

		exampleSubsystem 	= (useFullRobot || useExampleSubsystem)		? new ExampleSubsystem() 		: null;
		accelerometer		= (useFullRobot || useAccelerometer)		? new Accelerometer4237()		: null;
		gyro 				= (useFullRobot || useGyro)					? new Gyro4237()				: null;	
		drivetrain 			= (useFullRobot || useDrivetrain) 			? new Drivetrain(Constants.DrivetrainSetup.DRIVETRAIN_DATA, gyro, log) 	 : null;
		grabber 			= (useFullRobot || useGrabber) 				? new Grabber() 				: null;
		arm 				= (useFullRobot || useArm) 					? new Arm() 					: null;
		shoulder 			= (useFullRobot || useShoulder) 			? new Shoulder() 				: null;
		gatherer 			= (useFullRobot || useGatherer) 			? new Gatherer() 				: null;
		candle 				= (useFullRobot || useCandle)				? new Candle4237() 				: null;
		driverController 	= (useFullRobot || useDriverController) 	? new DriverController(0) 		: null;
		operatorController 	= (useFullRobot || useOperatorController) 	? new OperatorController(1)	 	: null;
		autonomousTabData	= (useFullRobot || useAutonomousTabData ) 	? new AutonomousTabData()		: null;
		mainShuffleboard 	= (useFullRobot || useMainShuffleboard)		? new MainShuffleboard(this)	: null;
		vision 				= (useFullRobot || useVision)				? new Vision()					: null;
		


		// Configure the trigger bindings
		if(useFullRobot || useBindings)
			configureBindings();
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
		if(driverController != null && drivetrain != null)
        {
			//BooleanSupplier aButton = () -> {return driverController.getRawButton(Xbox.Button.kA); };
			BooleanSupplier xButton = driverController.getButtonSupplier(Xbox.Button.kX);
			Trigger xButtonTrigger = new Trigger(xButton);
			//aButtonTrigger.onTrue(new LockWheels(drivetrain));
			xButtonTrigger.toggleOnTrue(new LockWheels(drivetrain));
			//JoystickButton drivetrainA = new JoystickButton(joystick,1);
			//DoubleSupplier leftYAxis = () -> { return driverController.getRawAxis(Xbox.Axis.kLeftY) * 2.0; };
			//DoubleSupplier leftXAxis = () -> { return driverController.getRawAxis(Xbox.Axis.kLeftX) * 2.0; };
			//DoubleSupplier rightXAxis = () -> {return driverController.getRawAxis(Xbox.Axis.kRightX) * 2.0; };
			
			DoubleSupplier leftYAxis = driverController.getAxisSupplier(Xbox.Axis.kLeftY);
			DoubleSupplier leftXAxis = driverController.getAxisSupplier(Xbox.Axis.kLeftX);
			DoubleSupplier rightXAxis = driverController.getAxisSupplier(Xbox.Axis.kRightX);

			drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, leftYAxis, leftXAxis, rightXAxis, true));
			// drivetrain.setDefaultCommand(new SwerveDrive(drivetrain, () -> 0.5, () -> 0.0, () -> 0.0, false));
        }
	}

	private void configureOperatorBindings()
	{
		// operatorController.configureAxes();
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand()
	{
		return null;
	}
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.subsystems.DrivetrainConfig;
import frc.robot.subsystems.SwerveModuleConfig;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public static class CANbusConstants
    {
        public static final int CANDLE_PORT = 1;
    }
    
    public static class OperatorConstants
    {
        public static final int kDriverControllerPort = 0;


    }

    public static class MotorConstants
    {
        public static final int GATHERER_MOTOR_PORT = 1;
        public static final int GRABBER_MOTOR_PORT = 2;
        public static final int ARM_MOTOR_PORT = 3;
        public static final int SHOULDER_MOTOR_PORT = 4;
    }

    public static class Motor
    {
        private static final int FRONT_LEFT_DRIVE   =  7;  // MM 2/28/22
        private static final int FRONT_LEFT_TURN    =  9;  // MM 2/28/22

        private static final int FRONT_RIGHT_DRIVE  = 10;  // MM 2/28/22
        private static final int FRONT_RIGHT_TURN   = 12;  // MM 2/28/22

        private static final int BACK_LEFT_DRIVE    =  4;  // MM 2/28/22
        private static final int BACK_LEFT_TURN     =  6;  // MM 2/28/22

        private static final int BACK_RIGHT_DRIVE   =  1;  // MM 2/28/22
        private static final int BACK_RIGHT_TURN    =  3;  // MM 2/28/22

        public static final String CAN_BUS = "CANivore";
    }

    public static class Sensor
    {
        public static final int COMPETITION_ROBOT           =  9;
        
        private static final int FRONT_LEFT_ENCODER         =  8;
        private static final int FRONT_RIGHT_ENCODER        = 11;
        private static final int BACK_LEFT_ENCODER          =  5;
        private static final int BACK_RIGHT_ENCODER         =  2;

        public static final int PDH_CAN_ID                  =  1;

        public static final int PIGEON = 0;
    }

    public static class Controller
    {
        public static final int DRIVER = 0;
        public static final int OPERATOR = 1;
    }

    public static class Drivetrain
    {
        public static final double INCHES_TO_METERS = 0.0254;
        public static final double DRIVETRAIN_WHEELBASE_METERS = 23.5 * INCHES_TO_METERS;
        public static final double DRIVETRAIN_TRACKWIDTH_METERS = 23.5 * INCHES_TO_METERS;
        public static final double MAX_MODULE_TURN_SPEED = 1080.0; // degrees per second, this is 3.0 rev/sec, used to be 1980 and 5.5 rev/sec
        public static final double MAX_MODULE_TURN_ACCELERATION = 1728.0; // degrees per second per second, this is 4.8 rev/sec^2, used to be 17280 and 48 rev/sec^2
        public static final double MAX_BATTERY_VOLTAGE = 12.0;
        public static final int DRIVE_MOTOR_ENCODER_RESOLUTION = 2048;
        public static final double DRIVE_MOTOR_GEAR_RATIO = 8.14;
        public static final double WHEEL_RADIUS_METERS = 2.0 * INCHES_TO_METERS;
        public static final double DRIVE_ENCODER_RATE_TO_METERS_PER_SEC = 
        ((10.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
        public static final double DRIVE_ENCODER_POSITION_TO_METERS =
        ((1.0 / DRIVE_MOTOR_ENCODER_RESOLUTION) / DRIVE_MOTOR_GEAR_RATIO) * (2.0 * Math.PI * WHEEL_RADIUS_METERS);
        public static final double MAX_DRIVE_SPEED = 4.4; // meters per second

    }

    private static class SwerveModuleSetup
    {
        // public static final SwerveModuleData FRONT_LEFT = new SwerveModuleData("Front Left", 7, true, 8, -167.255859375, 9);
        // public static final SwerveModuleData FRONT_RIGHT = new SwerveModuleData("Front Right", 10, false, 11, -305.947265625, 12);
        // public static final SwerveModuleData BACK_LEFT = new SwerveModuleData("Back Left", 4, true, 5, -348.75, 6);
        // public static final SwerveModuleData BACK_RIGHT = new SwerveModuleData("Back Right", 1, false, 2, -101.953125, 3);

        // private static final double FRONT_LEFT_ENCODER_OFFSET   = -167.255859375; //old encoder value
        private static final double FRONT_LEFT_ENCODER_OFFSET   = -338.730;
        private static final double FRONT_RIGHT_ENCODER_OFFSET  = -287.578;
        private static final double BACK_LEFT_ENCODER_OFFSET    = -348.75;
        // private static final double BACK_RIGHT_ENCODER_OFFSET   = -101.953125;
        private static final double BACK_RIGHT_ENCODER_OFFSET   = -108.809; //new swerve module

        private static final Translation2d FRONT_LEFT_LOCATION = new Translation2d(Drivetrain.DRIVETRAIN_WHEELBASE_METERS / 2, Drivetrain.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d FRONT_RIGHT_LOCATION = new Translation2d(Drivetrain.DRIVETRAIN_WHEELBASE_METERS / 2, -Drivetrain.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_LEFT_LOCATION = new Translation2d(-Drivetrain.DRIVETRAIN_WHEELBASE_METERS / 2, Drivetrain.DRIVETRAIN_TRACKWIDTH_METERS / 2);
        private static final Translation2d BACK_RIGHT_LOCATION = new Translation2d(-Drivetrain.DRIVETRAIN_WHEELBASE_METERS / 2, -Drivetrain.DRIVETRAIN_TRACKWIDTH_METERS / 2);

        private static final SwerveModuleConfig FRONT_LEFT = new SwerveModuleConfig(
            "Front Left", FRONT_LEFT_LOCATION, Motor.FRONT_LEFT_DRIVE, true, Sensor.FRONT_LEFT_ENCODER, FRONT_LEFT_ENCODER_OFFSET, Motor.FRONT_LEFT_TURN);
        private static final SwerveModuleConfig FRONT_RIGHT = new SwerveModuleConfig(
            "Front Right", FRONT_RIGHT_LOCATION, Motor.FRONT_RIGHT_DRIVE, false, Sensor.FRONT_RIGHT_ENCODER, FRONT_RIGHT_ENCODER_OFFSET, Motor.FRONT_RIGHT_TURN);
        private static final SwerveModuleConfig BACK_LEFT = new SwerveModuleConfig(
            "Back Left", BACK_LEFT_LOCATION, Motor.BACK_LEFT_DRIVE, true, Sensor.BACK_LEFT_ENCODER, BACK_LEFT_ENCODER_OFFSET, Motor.BACK_LEFT_TURN);
        private static final SwerveModuleConfig BACK_RIGHT = new SwerveModuleConfig(
            "Back Right", BACK_RIGHT_LOCATION, Motor.BACK_RIGHT_DRIVE, false, Sensor.BACK_RIGHT_ENCODER, BACK_RIGHT_ENCODER_OFFSET, Motor.BACK_RIGHT_TURN);
    }

    public static class DrivetrainSetup
    {
        public static final DrivetrainConfig DRIVETRAIN_DATA = new DrivetrainConfig(
            SwerveModuleSetup.FRONT_LEFT, SwerveModuleSetup.FRONT_RIGHT, SwerveModuleSetup.BACK_LEFT, SwerveModuleSetup.BACK_RIGHT);
    }
}

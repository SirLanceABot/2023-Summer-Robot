package frc.robot.sensors;

import java.lang.invoke.MethodHandles;
import java.lang.management.OperatingSystemMXBean;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Shoulder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.GrabGamePiece;
import frc.robot.commands.SuctionControl;
import frc.robot.shuffleboard.AutonomousTab;
import frc.robot.shuffleboard.MainShuffleboard;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import frc.robot.controls.Xbox;
import frc.robot.controls.OperatorController;

public class Ultrasonic4237 
{
    private final AnalogInput sonarSensor = new AnalogInput(0);
    private final AnalogPotentiometer pot = new AnalogPotentiometer(sonarSensor, 5000, 0);
    final int kUltrasonicPingPort = 2;
    final int kUltrasonicEchoPort = 1;
    // private final Timer timer;

    // public Ultrasonic4237()
    // {
        
    // }

    public void readPeriodicInputs()
    {

    }

    public void writePeriodicOutputs()
    {

    }
}

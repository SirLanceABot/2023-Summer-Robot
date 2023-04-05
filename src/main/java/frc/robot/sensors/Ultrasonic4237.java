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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import frc.robot.controls.Xbox;
import frc.robot.controls.OperatorController;

public class Ultrasonic4237 extends Sensor4237
{

    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    private class PeriodicIO
    {
        
        private double sensorVoltage;
        private double filteredMeasurement;
        private double sensorDistance;
        // private Timer timer;
    }

    private final AnalogInput sonarSensor = new AnalogInput(0);
    // private final AnalogPotentiometer pot = new AnalogPotentiometer(sonarSensor, 5000, 0);
    
    
    // final int kUltrasonicPingPort = 2;
    // final int kUltrasonicEchoPort = 1;
    private final MedianFilter m_filter = new MedianFilter(5);
    private final PeriodicIO periodicIO = new PeriodicIO();
    private boolean skipChecker = false; 

    public Ultrasonic4237()
    {
        // periodicIO.timer = new Timer();
        // startTimer();
        periodicIO.sensorDistance = calculateDistance();
    }

    // public void startTimer()
    // {
    //     periodicIO.timer.start();
    // }

    private double calculateDistance()
    {
        var voltSensor = sonarSensor.getVoltage();
        SmartDashboard.putNumber("Sensor Voltage", voltSensor);
        var voltsPerCM = edu.wpi.first.wpilibj.RobotController.getVoltage5V() / 512.;
        SmartDashboard.putNumber("RoboRio Voltage", voltsPerCM);
        periodicIO.sensorVoltage = voltSensor/voltsPerCM/30.48/*cm per foot*/;
        periodicIO.filteredMeasurement = m_filter.calculate(periodicIO.sensorVoltage);
        return periodicIO.filteredMeasurement;
    }

    public double getDistance()
    {
        return periodicIO.sensorDistance;
    }

    @Override
    public void readPeriodicInputs()
    {
        if(!skipChecker)
        {
            double temp = calculateDistance();
            SmartDashboard.putNumber("Temp Distance", temp);
            if(temp < 12.0)
            {
                periodicIO.sensorDistance = temp;
                skipChecker = !skipChecker;
            }
        }
        else
        {
            skipChecker = !skipChecker;
        }
    }

    @Override
    public void writePeriodicOutputs()
    {
        // System.out.println("Distance: " + pot.get() / 304.8 + "\n");
        SmartDashboard.putNumber("Distance", periodicIO.sensorDistance);
        
    }

    @Override
    public String toString()
    {
        return String.format("Ultrasonic %f \n", periodicIO.sensorDistance);
    }
}

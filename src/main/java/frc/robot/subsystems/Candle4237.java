package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.ColorFlowAnimation;
import com.ctre.phoenix.led.FireAnimation;
import com.ctre.phoenix.led.LarsonAnimation;
import com.ctre.phoenix.led.RainbowAnimation;
import com.ctre.phoenix.led.RgbFadeAnimation;
import com.ctre.phoenix.led.SingleFadeAnimation;
import com.ctre.phoenix.led.StrobeAnimation;
import com.ctre.phoenix.led.TwinkleOffAnimation;
import com.ctre.phoenix.led.TwinkleAnimation;
import com.ctre.phoenix.led.ColorFlowAnimation.Direction;
import com.ctre.phoenix.led.LarsonAnimation.BounceMode;
import com.ctre.phoenix.led.TwinkleAnimation.TwinklePercent;
import com.ctre.phoenix.led.TwinkleOffAnimation.TwinkleOffPercent;

import frc.robot.Constants.CANbusConstants;

/**
 * Class containing one CANdle
 */
public class Candle4237 extends Subsystem4237
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    public enum ledStatus
    {
        kPurple, kYellow, kWhite, kAnimated, kOff;
    }

    public enum ledAnimation
    {
        kColorFlow, kFire, kLarson, kRainbow, kRgbFade, 
        kSingleFade, kStrobe, kTwinkle, kTwinkleOff, kDisabled;
    }

    private class PeriodicIO
    {
        // Inputs

        // Outputs
        private ledStatus status = ledStatus.kOff;
        private ledAnimation toAnimate = ledAnimation.kDisabled;
    }

    private PeriodicIO periodicIO = new PeriodicIO();
    private final CANdle candle = new CANdle(CANbusConstants.CANDLE_PORT, "rio");
    private final int ledCount = 68; // CANdle = 8, LED Strip = 60, LED Strip + CANdle = 68
    private Animation animation = null;

    public Candle4237()
    {
        periodicIO.status = ledStatus.kOff;
    }

    /**
     * Sets the LED(s) to cube purple
     */
    public void signalCube()
    {
        periodicIO.status = ledStatus.kPurple;
    }

    /**
     * Sets the LEDs to cone yellow
     */
    public void signalCone()
    {
        periodicIO.status = ledStatus.kYellow;
    }

    /**
     * Sets the LEDs to Bright Blinding White
     */
    public void signalReadyToDrop()
    {
        periodicIO.status = ledStatus.kWhite;
    }

    /**
     * Turns off the LEDs
     */
    public void turnOffLight()
    {
        periodicIO.status = ledStatus.kOff;
        animation = null;
    }

    public void animate()
    {
        switch (periodicIO.toAnimate)
        {
            case kColorFlow: 
                animation = new ColorFlowAnimation(128, 20, 70, 0, 0.7, ledCount, Direction.Forward);
                break;
            case kFire: 
                animation = new FireAnimation(0.5, 0.7, ledCount, 0.7, 0.5);
                break;
            case kLarson: 
                animation = new LarsonAnimation(0, 255, 46, 0, 1, ledCount, BounceMode.Front, 3);
                break;
            case kRainbow: 
                animation = new RainbowAnimation(1, 0.1, ledCount);
                break;
            case kRgbFade: 
                animation = new RgbFadeAnimation(0.7, 0.4, ledCount);
                break;
            case kSingleFade: 
                animation = new SingleFadeAnimation(50, 2, 200, 0, 0.5, ledCount);
                break;
            case kStrobe: 
                animation = new StrobeAnimation(240, 10, 180, 0, 98.0 / 256.0, ledCount);
                break;
            case kTwinkle: 
                animation = new TwinkleAnimation(30, 70, 60, 0, 0.4, ledCount, TwinklePercent.Percent6);
                break;
            case kTwinkleOff: 
                animation = new TwinkleOffAnimation(70, 90, 175, 0, 0.8, ledCount, TwinkleOffPercent.Percent100);
                break;
            case kDisabled: 
                animation = null;
                break;
        }
    }

    public void rainbowAnimation()
    {
        periodicIO.toAnimate = ledAnimation.kRainbow;
        periodicIO.status = ledStatus.kAnimated;
    }

    @Override
    public synchronized void readPeriodicInputs()
    {}

    /**
     * Sets the color of all LEDs based on the current status
     */
    @Override  
    public synchronized void writePeriodicOutputs() 
    {
        switch (periodicIO.status)
        {
            case kPurple: 
                candle.setLEDs(255, 0, 255, 50, 0, ledCount);
                break;
            case kYellow: 
                candle.setLEDs(255, 185, 0, 50, 0, ledCount);
                break; 
            case kWhite: 
                candle.setLEDs(255, 255, 255, 255, 0, ledCount);
                break;
            case kOff: 
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                animation = null;
                break;
            case kAnimated: 
                animate();
                candle.animate(animation); // fix later
                break;
            default:    // redundant? just to be safe
                candle.setLEDs(0, 0, 0, 0, 0, ledCount);
                animation = null;
                break;
        }
    }
}

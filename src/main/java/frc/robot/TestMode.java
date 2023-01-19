package frc.robot;

import java.lang.invoke.MethodHandles;

// *** IMPORTANT - PLEASE READ ***
// 1. Put your test code in your own frc.robot.tests.[yourname]Test.java file
// 2. Uncomment one of the IMPORT statements below
// 3. Uncomment one of the CLASS VARIABLES below
// 4. Test your code
// 5. Comment your IMPORT statement and CLASS VARIABLE statement when finished


// *** IMPORT statements ***
// Uncomment one of these statements

// import frc.robot.tests.ExampleTest;
// import frc.robot.tests.DavidTest;
// import frc.robot.tests.KyleTest;
import frc.robot.tests.LoganTest;
// import frc.robot.tests.MaahishTest;
// import frc.robot.tests.MatthewTest;
// import frc.robot.tests.OwenTest;
// import frc.robot.tests.SamTest;
// import frc.robot.tests.TanuTest;
// import frc.robot.tests.JWoodTest;
// import frc.robot.tests.RThomasTest;


public class TestMode
{
    // This string gets the full name of the class, including the package name
    private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

    // *** STATIC INITIALIZATION BLOCK ***
    // This block of code is run first when the class is loaded
    static
    {
        System.out.println("Loading: " + fullClassName);
    }

    // *** CLASS VARIABLES ***
    // Uncomment one of these statements

    // private static final ExampleTest myTest = new ExampleTest();
    // private static final DavidTest myTest = new DavidTest();
    // private static final KyleTest myTest = new KyleTest();
    private static final LoganTest myTest = new LoganTest();
    // private static final MaahishTest myTest = new MaahishTest();
    // private static final MatthewTest myTest = new MatthewTest();
    // private static final OwenTest myTest = new OwenTest();
    // private static final SamTest myTest = new SamTest();
    // private static final TanuTest myTest = new TanuTest();
    // private static final JWoodTest myTest = new JWoodTest();
    // private static final RThomasTest myTest = new RThomasTest();


    /**
     * This method runs one time before the periodic() method.
     */
    public void init()
    {
        myTest.init();
    }

    /**
     * This method runs periodically (every 20ms).
     */
    public void periodic()
    {
        myTest.periodic();
    }

    /**
     * This method runs one time after the periodic() method.
     */
    public void exit()
    {
        myTest.exit();
    }    

}

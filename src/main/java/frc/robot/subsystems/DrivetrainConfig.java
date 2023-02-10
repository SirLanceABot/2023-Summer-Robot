package frc.robot.subsystems;

public class DrivetrainConfig
{
    public final SwerveModuleConfig frontLeftSwerveModule;
    public final SwerveModuleConfig frontRightSwerveModule;
    public final SwerveModuleConfig backLeftSwerveModule;
    public final SwerveModuleConfig backRightSwerveModule;

    public DrivetrainConfig(SwerveModuleConfig frontLeftSwerveModule,
                          SwerveModuleConfig frontRightSwerveModule,
                          SwerveModuleConfig backLeftSwerveModule,
                          SwerveModuleConfig backRightSwerveModule)
    {
        this.frontLeftSwerveModule = frontLeftSwerveModule;
        this.frontRightSwerveModule = frontRightSwerveModule;
        this.backLeftSwerveModule = backLeftSwerveModule;
        this.backRightSwerveModule = backRightSwerveModule;
    }
}

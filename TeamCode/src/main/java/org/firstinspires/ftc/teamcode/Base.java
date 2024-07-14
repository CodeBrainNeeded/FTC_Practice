package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Modules.DriveTrain;

public abstract class Base extends LinearOpMode{
    public ElapsedTime timer;

    public DriveTrain driveTrain;

    public void initHardware(){
        //Set up the driveTrain object with the hardwareMap references
        driveTrain = new DriveTrain(
                hardwareMap.get(DcMotor.class, "frontLeft"),
                hardwareMap.get(DcMotor.class, "frontRight"),
                hardwareMap.get(DcMotor.class, "backLeft"),
                hardwareMap.get(DcMotor.class, "backRight"),

                hardwareMap.get(IMU.class, "imu"),

                telemetry
        );

        //Configure the IMU with the Control Hub Position
        driveTrain.configureIMU(
                new IMU.Parameters(
                    new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                    )
                )
        );
        driveTrain.resetIMU(true);

    }
}

/* Copyright (c) 2022 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Modules;

import static org.firstinspires.ftc.teamcode.Utils.UtilMethods.*;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/*
a mecanum drivetrain with robot- and field-centric driving capabilities
 */
public class DriveTrain {
    private final static double xK = Math.sqrt(2);
    private final static double yK = 1;
    private final static double turnK = 0.5;

    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;

    private IMU gyro;

    private Telemetry telemetry;

    private int[] curTicks;
    private int[] prevTicks;

    public DriveTrain(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, IMU imuConfiguration, Telemetry tele){
        frontLeftDrive = frontLeft;
        frontRightDrive = frontRight;
        backLeftDrive = backLeft;
        backRightDrive = backRight;

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        gyro = imuConfiguration;

        telemetry = tele;
    }

    public void configureIMU(IMU.Parameters parameters){
        gyro.initialize(parameters);
    }

    public void resetIMU(boolean check){
        if(check){gyro.resetYaw();}
    }

    /**
     * @param x the intended x-axis (left/right) velocity
     * @param y the intended y-axis (forward/backward) velocity (do not flip to account for the y-stick weirdness)
     * @param turn the intended direction of turning
     */
    public void move(double x, double y, double turn){
        double adjX = x * xK;
        // y is negated because the y-stick is flipped (weird)
        double adjY = - y * yK;

        double adjTurn = turn * turnK;

        double[] driveMotorPowers = {
                adjX + adjY + adjTurn, //frontLeftPower
                adjY - adjX - adjTurn, //frontRightPower
                adjY - adjX + adjTurn, //backLeftPower
                adjY + adjX - adjTurn};//backRightPower

        //scale all the items in driveMotorPowers to be within the range [-1, 1]
        scaleTo1(driveMotorPowers);

        frontLeftDrive.setPower(driveMotorPowers[0]);
        frontRightDrive.setPower(driveMotorPowers[1]);
        backLeftDrive.setPower(driveMotorPowers[2]);
        backRightDrive.setPower(driveMotorPowers[3]);

        for(int i = 0; i < driveMotorPowers.length; i++){
            this.telemetry.addData("Motor " + i + " Power:", driveMotorPowers[i] + " ");
        }
    }

    /**
     * @param x the intended x-axis (left/right) velocity
     * @param y the intended y-axis (forward/backward) velocity
     * @param turn the intended direction of turning
     */
    public void fieldCentricMove(double x, double y, double turn){
        double heading = gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotatedX = Math.cos(heading) * x - Math.sin(heading) * y;
        double rotatedY = Math.cos(heading) * y + Math.sin(heading) * x;

        move(rotatedX, rotatedY, turn);
    }
}

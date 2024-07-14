package org.firstinspires.ftc.teamcode.OpModes;

import org.firstinspires.ftc.teamcode.Base;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class TeleOp extends Base{

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Initialization Status: ", "Incomplete");
        initHardware();
        telemetry.addData("Initialization Status: ", "Completed");

        waitForStart();

        while(opModeIsActive()){
            driveTrain.fieldCentricMove(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);
            driveTrain.resetIMU(gamepad1.start || gamepad2.start);

            telemetry.update();
        }
    }
}

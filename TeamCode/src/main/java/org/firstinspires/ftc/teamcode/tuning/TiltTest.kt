package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(group = "tuning")
class TiltTest : LinearOpMode() {
    override fun runOpMode() {
        val servo = hardwareMap.get(Servo::class.java, "intakeTilt")

        waitForStart()

        while (opModeIsActive()) {
            val pos = gamepad1.right_stick_y.toDouble() * -0.5 + 0.5

            servo.position = pos

            telemetry.addData("servo pos", pos)
            telemetry.update()
        }
    }
}
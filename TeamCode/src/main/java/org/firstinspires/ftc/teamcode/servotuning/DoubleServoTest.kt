package org.firstinspires.ftc.teamcode.servotuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class DoubleServoTest : LinearOpMode() {
    override fun runOpMode() {
        val servo = hardwareMap.get(Servo::class.java, "servo")
        val servo2 = hardwareMap.get(Servo::class.java, "servo2")

        servo2.direction = Servo.Direction.REVERSE

        waitForStart()

        while (opModeIsActive()) {
            val pos = gamepad1.right_stick_y.toDouble() * -0.5 + 0.5

            servo.position = pos
            servo2.position = pos

            telemetry.addData("servo pos", pos)
            telemetry.update()
        }
    }
}
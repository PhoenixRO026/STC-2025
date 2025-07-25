package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp(group = "tuning")
class ClawTest : LinearOpMode() {
    override fun runOpMode() {
        val servo = hardwareMap.get(Servo::class.java, "claw")

        servo.direction = Servo.Direction.REVERSE

        waitForStart()

        var pos = 0.5
        var currentTime = System.currentTimeMillis() / 1000.0
        var previousTime = currentTime
        var deltaTime: Double

        while (opModeIsActive()) {
            currentTime = System.currentTimeMillis() / 1000.0
            deltaTime = currentTime - previousTime
            previousTime = currentTime

            if (gamepad1.dpad_up) {
                pos += deltaTime * 0.1
            }
            if (gamepad1.dpad_down) {
                pos -= deltaTime * 0.1
            }

            pos = pos.coerceIn(0.0, 1.0)
            servo.position = pos

            telemetry.addData("arm pos", pos)
            telemetry.update()
        }
    }
}
package org.firstinspires.ftc.teamcode.servotuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class ServoTest : LinearOpMode() {
    override fun runOpMode() {
        val servo = hardwareMap.get(Servo::class.java, "rightArm")
        val servo2 = hardwareMap.get(Servo::class.java, "leftArm")
        val servo3 = hardwareMap.get(Servo::class.java, "elbow")

        servo2.direction = Servo.Direction.REVERSE

        waitForStart()

        var pos = 0.5
        var pos1 = 0.5
        var currentTime = System.currentTimeMillis() / 1000.0
        var previousTime = currentTime
        var deltaTime = 0.0

        while (opModeIsActive()) {
            currentTime = System.currentTimeMillis() / 1000.0
            deltaTime = currentTime - previousTime
            previousTime = currentTime

            if (gamepad1.y) {
                pos1 += deltaTime * 0.1
            }
            if (gamepad1.a) {
                pos1 -= deltaTime * 0.1
            }
            if (gamepad1.dpad_up) {
                pos += deltaTime * 0.1
            }
            if (gamepad1.dpad_down) {
                pos -= deltaTime * 0.1
            }

            pos = pos.coerceIn(0.0, 1.0)
            pos1 = pos1.coerceIn(0.0, 1.0)
            servo.position = pos
            servo2.position = pos
            servo3.position = pos1
            telemetry.addData("servo pos", pos)
            telemetry.addData("servo pos", pos1)
            telemetry.update()
        }
    }
}
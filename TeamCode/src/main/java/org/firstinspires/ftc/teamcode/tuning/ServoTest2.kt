package org.firstinspires.ftc.teamcode.tuning

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp(group = "tuning")
class ServoTest2 : LinearOpMode() {
    @Config
    data object ServoTestConfig {
        @JvmField
        var offset = 0.0
    }
    override fun runOpMode() {
        val servo = hardwareMap.get(Servo::class.java, "rightArm")
        val servo2 = hardwareMap.get(Servo::class.java, "leftArm")
        val servo3 = hardwareMap.get(Servo::class.java, "elbow")

        servo2.direction = Servo.Direction.REVERSE

        //val arm = Robot(hardwareMap).arm

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

            servo.scaleRange(0.0, 1.0 - ServoTestConfig.offset)
            servo2.scaleRange(0.0, 1.0 - ServoTestConfig.offset)

            if (gamepad1.y)  {
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
            telemetry.addData("arm pos", pos)
            telemetry.addData("elbow pos", pos1)
            telemetry.update()
        }
    }
}
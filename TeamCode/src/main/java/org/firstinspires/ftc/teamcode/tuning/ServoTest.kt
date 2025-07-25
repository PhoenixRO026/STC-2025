package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp(group = "tuning")
class ServoTest : LinearOpMode() {
    override fun runOpMode() {
        /*val servo = hardwareMap.get(Servo::class.java, "rightArm")
        val servo2 = hardwareMap.get(Servo::class.java, "leftArm")
        val servo3 = hardwareMap.get(Servo::class.java, "elbow")

        servo2.direction = Servo.Direction.REVERSE*/

        val robot = Robot(hardwareMap)

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

            if (gamepad1.x) {
                pos1 += deltaTime * 0.3
            }

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
            robot.arm.clawPos = gamepad1.right_trigger.toDouble()

            pos = pos.coerceIn(0.0, 1.0)
            pos1 = pos1.coerceIn(0.0, 1.0)
            robot.arm.armPos = pos
            robot.arm.elbowPos = pos1

            telemetry.addData("arm pos", pos)
            telemetry.addData("elbow pos", pos1)
            telemetry.addData("lift pos", robot.lift.position)
            telemetry.addData("claw pos", robot.arm.clawPos)
            telemetry.update()
        }
    }
}
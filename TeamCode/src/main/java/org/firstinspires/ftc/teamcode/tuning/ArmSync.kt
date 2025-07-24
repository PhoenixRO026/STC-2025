package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp(group = "tuning")
class ArmSync : LinearOpMode() {
    override fun runOpMode() {
        val leftArm = hardwareMap.get(Servo::class.java, "leftArm")
        val rightArm = hardwareMap.get(Servo::class.java, "rightArm")

        waitForStart()

        while (opModeIsActive()) {
            val leftPos = gamepad1.left_trigger.toDouble()
            val rightPos = gamepad1.right_trigger.toDouble()

            leftArm.position = leftPos
            rightArm.position = rightPos

            telemetry.addData("left pos", leftPos)
            telemetry.addData("right pos", rightPos)
            telemetry.update()
        }
    }
}
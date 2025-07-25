package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.now
import com.commonlibs.units.Pose
import com.commonlibs.units.cm
import com.commonlibs.units.deg
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.buttons.ToggleButtonReader
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class SleepyDriveManual : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap, Pose(0.0.cm, 0.0.cm, 0.0.deg))
        val wristFlipButton = ToggleButtonReader{gamepad2.a}
        val dumpFlipButton = ToggleButtonReader{gamepad2.y}
        val intakeFlipButton = ToggleButtonReader{gamepad2.b}
        val buttons = listOf(wristFlipButton, dumpFlipButton, intakeFlipButton)

        var previousTime: Double
        var deltaTime : Double
        var now : Double

        waitForStart()
        previousTime = now()

        while (opModeIsActive()) {
            now = now()
            deltaTime = now - previousTime
            previousTime = now
            buttons.forEach{it.readValue()}

            robot.drive.snipperMode = gamepad1.right_trigger >= 0.2
            robot.drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )
            if (gamepad1.y) {
                robot.drive.resetHeadind()
            }
            robot.lift.power = -gamepad2.right_stick_y.toDouble()
            robot.arm.clawPos = gamepad2.right_trigger.toDouble()

            if (wristFlipButton.state) {
                robot.arm.wristUpsideDown()
            }
            else {
                robot.arm.wristUpright()
            }

            if (dumpFlipButton.state) {
                robot.intake.dumpSample()
            }
            else {
                robot.intake.dumpDown()
            }

            if (intakeFlipButton.state) {
                robot.intake.tiltUp()
            }
            else {
                robot.intake.tiltDown()
            }

            if (gamepad2.x) {
                robot.intake.sweeperPower = -1.0
            }
            else {
                robot.intake.sweeperPower = gamepad2.left_trigger.toDouble()
            }
            robot.intake.extendoPower = -gamepad2.left_stick_y.toDouble()

            if (gamepad2.left_bumper) {
                robot.arm.armToBoxIntake()
            }
            if (gamepad2.right_bumper) {
                robot.arm.armToSpecimen()
            }

            //////////////////////////

            if (gamepad2.dpad_up) {
                robot.arm.armPos += 0.1 * deltaTime
            }

            if (gamepad2.dpad_down) {
                robot.arm.armPos -= 0.1 * deltaTime
            }

            if (gamepad2.dpad_left) {
                robot.arm.elbowPos += 0.1 * deltaTime
            }

            if (gamepad2.dpad_right) {
                robot.arm.elbowPos -= 0.1 * deltaTime
            }

            telemetry.addData("armPos", robot.arm.armPos)
            telemetry.addData("elbowPos", robot.arm.elbowPos)
            telemetry.addData("liftPos", robot.lift.position)
            telemetry.addData("extendoPos", robot.intake.extendoPosition)
            telemetry.update()

        }
    }
}
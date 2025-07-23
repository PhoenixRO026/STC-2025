package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.buttons.ToggleButtonReader
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp
class SleepyDrive : LinearOpMode() {
    override fun runOpMode() {
        val robot = Robot(hardwareMap, Pose2d(0.0, 0.0, 0.0))
        val wristFlipButton = ToggleButtonReader{gamepad2.a}
        val dumpFlipButton = ToggleButtonReader{gamepad2.y}
        val intakeFlipButton = ToggleButtonReader{gamepad2.b}
        val buttons = listOf(wristFlipButton, dumpFlipButton, intakeFlipButton)

        waitForStart()

        while (opModeIsActive()) {
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

            if (wristFlipButton.wasJustPressed()) {
                if (wristFlipButton.state) {
                    robot.arm.wristUpsideDown()
                }
                else {
                    robot.arm.wristUpright()
                }
            }

            if (dumpFlipButton.wasJustPressed()) {
                if (dumpFlipButton.state) {
                    robot.intake.dumpSample()
                }
                else {
                    robot.intake.dumpDown()
                }
            }

            if (dumpFlipButton.wasJustPressed()) {
                if (dumpFlipButton.state) {
                    robot.intake.dumpSample()
                }
                else {
                    robot.intake.dumpDown()
                }
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
                robot.arm.armToSpecimen1()
            }
        }
    }
}
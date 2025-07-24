package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Robot

abstract class JenniferDrive: LinearOpMode() {
    enum class Color {
        RED,
        BLUE
    }

    abstract val color: Color

    private var driver1Action: Action? = null
    private var driver2Action: Action? = null
    private var actions = listOf(driver1Action, driver2Action)

    private val timeKeep = TimeKeep()
    private val movementTimekeep = TimeKeep()
    private val systemsTimekeep = TimeKeep()
    private val actionsTimekeep = TimeKeep()
    private val updateTimekeep = TimeKeep()
    private val telemetryTimkeep = TimeKeep()
    private val betweenLoopTimeKeep = TimeKeep()

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addLine("INITIALIZING")
        telemetry.update()

        timeKeep.resetDeltaTime()
        val robot = Robot(
            hardwareMap = hardwareMap,
            resetEncoders = false
        )

        val rightBumper2Button = ButtonReader { gamepad2.right_bumper }
        val x1Button = ButtonReader { gamepad1.x }
        val b1Button = ButtonReader { gamepad1.b }
        val buttons = listOf(rightBumper2Button, x1Button, b1Button)

        telemetry.addLine("Ready")
        telemetry.update()

        while (opModeInInit()) {
            timeKeep.resetDeltaTime()
            sleep(50)
        }

        robot.initTeleop()

        while (isStarted && !isStopRequested) {
            betweenLoopTimeKeep.resetDeltaTime()
            buttons.forEach { it.readValue() }

            movementTimekeep.resetDeltaTime()
            movement(robot)
            movementTimekeep.resetDeltaTime()

            systemsTimekeep.resetDeltaTime()
            normalSystems(robot, rightBumper2Button, x1Button, b1Button)
            systemsTimekeep.resetDeltaTime()

            actionsTimekeep.resetDeltaTime()
            runActions()
            actionsTimekeep.resetDeltaTime()

            timeKeep.resetDeltaTime()
            updateTimekeep.resetDeltaTime()
            robot.update(timeKeep.deltaTime)
            updateTimekeep.resetDeltaTime()

            val telemetryMs = telemetryTimkeep.deltaTime
            telemetryTimkeep.resetDeltaTime()
            robot.addTelemetry(telemetry, timeKeep.deltaTime)
            telemetry.addData("lift pos", robot.lift.position)

            telemetry.addData("shoulder", robot.outtake.shoulderPos)

            telemetry.addData("driver 1 action", driver1Action)
            telemetry.addData("driver 2 action", driver2Action)
            telemetry.addData("right bumper", gamepad1.right_bumper)
            telemetry.addData("left bumper", gamepad1.left_bumper)
            telemetry.addData("movement ms", movementTimekeep.deltaTime.asMs)
            telemetry.addData("systems ms", systemsTimekeep.deltaTime.asMs)
            telemetry.addData("actions ms", actionsTimekeep.deltaTime.asMs)
            telemetry.addData("update ms", updateTimekeep.deltaTime.asMs)
            telemetry.addData("telemetry ms", telemetryMs)
            telemetry.addData("between loop ms", betweenLoopTimeKeep.deltaTime.asMs)
            telemetry.addData("lift pos",robot.lift.position)
            telemetry.addData("claw", robot.outtake.clawPos)
            telemetry.update()
            telemetryTimkeep.resetDeltaTime()
            betweenLoopTimeKeep.resetDeltaTime()
        }
    }

    private fun normalSystems(robot: Robot, armIntakeButton: ButtonReader, takeYellowsButton: ButtonReader, takeColoredButton: ButtonReader) {
        robot.outtake.clawPos = gamepad2.right_trigger.toDouble()

        robot.lift.power = -gamepad2.right_stick_y.toDouble()

        if (gamepad2.left_bumper) {
            robot.outtake.armToBasketInstant()
        }

        if (armIntakeButton.wasJustPressed()) {
            driver2Action = SequentialAction(
                robot.lift.liftToIntakeWaitingAction(),
                InstantAction {
                    robot.intake.extendoTargetPosition = Intake.IntakeConfig.extendoIn
                    robot.intake.extendoMode = Intake.Mode.ACTION
                },
                robot.armAndLiftToIntake(),
                robot.outtake.closeClawAction(),
                InstantAction {
                    robot.intake.extendoMode = Intake.Mode.RAW_POWER
                    robot.intake.extendoPower = 0.0
                }
            )
        }

        if (gamepad2.back) {
            robot.lift.resetLiftPos()
        }

        if (gamepad2.touchpad) {
            driver2Action = robot.turnOffAction()
        }

        if (gamepad2.a) {
            robot.outtake.armToWallInstant()
        }

        if (gamepad2.b) {
            robot.outtake.armToNeutralInstant()
        }

        if (gamepad2.x) {
            robot.robotToBarInstant()
        }

        if (gamepad2.y) {
            robot.outtake.armToScoreInstant()
        }

        if (gamepad1.a) {
            robot.intake.resetExtendoPosition()
        }

        if (gamepad1.dpad_up) {
            robot.intake.tiltUpInstant()
        }

        if (gamepad1.dpad_down) {
            robot.intake.tiltDownInstant()
        }

        if (takeYellowsButton.wasJustPressed()) {
            driver1Action = SequentialAction(
                ParallelAction(
                    SequentialAction(
                        robot.lift.liftToIntakeWaitingAction(),
                        robot.outtake.armToIntakeAction()
                    ),
                    robot.intake.takeSampleSequenceAction(Intake.SensorColor.YELLOW),
                ),
                robot.intake.sampleToBox()
            )
        }

        if (takeColoredButton.wasJustPressed()) {
            driver1Action = robot.intake.takeSampleSequenceAction(when (color) {
                Color.RED -> Intake.SensorColor.RED
                Color.BLUE -> Intake.SensorColor.BLUE
            })
        }

        if (gamepad1.left_bumper) {
            robot.intake.extendoPower = -1.0
        } else if (gamepad1.right_bumper) {
            robot.intake.extendoPower = 1.0
        } else {
            robot.intake.extendoPower = 0.0
        }

        if (gamepad2.left_trigger != 0f) {
            robot.intake.sweeperPower = gamepad2.left_trigger.toDouble()
        } else {
            robot.intake.sweeperPower = -gamepad1.left_trigger.toDouble()
        }

    }

    private fun movement(robot: Robot) {
        robot.updatePoseEstimate()

        if (gamepad1.y)
            robot.drive.resetFieldCentric()

        robot.drive.isSlowMode = gamepad1.right_trigger >= 0.2

        robot.drive.driveFieldCentric(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble()
        )
    }

    private fun runActions() {
        driver1Action?.let {
            if (!it.run(TelemetryPacket())) {
                driver1Action = null
            }
        }
        driver2Action?.let {
            if (!it.run(TelemetryPacket())) {
                driver2Action = null
            }
        }
    }
}
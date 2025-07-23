package org.firstinspires.ftc.teamcode.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Lift
import org.firstinspires.ftc.teamcode.robot.Robot
@TeleOp
class LiftActionTest: LinearOpMode() {

    private var runningAction: Action? = null

    private val timeKeep = TimeKeep()
    private val liftTimekeep = TimeKeep()
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

        while (isStarted && !isStopRequested) {
            betweenLoopTimeKeep.resetDeltaTime()

            actionsTimekeep.resetDeltaTime()
            runActions()
            actionsTimekeep.resetDeltaTime()

            timeKeep.resetDeltaTime()
            updateTimekeep.resetDeltaTime()
            buttons.forEach { it.readValue() }


            if (x1Button.wasJustPressed()) {
                runningAction = robot.lift.liftToBarAction()
            }

            if (b1Button.wasJustPressed()) {
                runningAction = robot.lift.liftDownAction()
            }

            val telemetryMs = telemetryTimkeep.deltaTime
            telemetryTimkeep.resetDeltaTime()
            telemetry.addData("running action", runningAction)
            telemetry.addData("right bumper", gamepad1.right_bumper)
            telemetry.addData("left bumper", gamepad1.left_bumper)
            telemetry.addData("actions ms", actionsTimekeep.deltaTime.asMs)
            telemetry.addData("update ms", updateTimekeep.deltaTime.asMs)
            telemetry.addData("telemetry ms", telemetryMs)
            telemetry.addData("between loop ms", betweenLoopTimeKeep.deltaTime.asMs)
            telemetry.addData("lift pos", robot.lift.position)
            telemetry.update()
            telemetryTimkeep.resetDeltaTime()
            betweenLoopTimeKeep.resetDeltaTime()
        }
    }

    private fun runActions() {
        runningAction?.let {
            if (!it.run(TelemetryPacket())) {
                runningAction = null
            }
        }
    }
}
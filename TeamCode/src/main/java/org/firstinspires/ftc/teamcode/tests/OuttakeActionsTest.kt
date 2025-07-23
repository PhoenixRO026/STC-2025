package org.firstinspires.ftc.teamcode.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.buttons.ButtonReader
import org.firstinspires.ftc.teamcode.robot.Robot
import java.util.LinkedList

class OuttakeActionsTest: LinearOpMode() {
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val outtake = Robot(hardwareMap).outtake

        waitForStart()

        outtake.initTeleop()

        val buttonA = ButtonReader { gamepad1.a }
        val buttonB = ButtonReader { gamepad1.b }
        val buttonX = ButtonReader { gamepad1.x }
        val buttonY = ButtonReader { gamepad1.y }

        val actionQueue = LinkedList<Action>()

        while (opModeIsActive()) {
            buttonA.readValue()
            buttonB.readValue()
            buttonX.readValue()
            buttonY.readValue()

            when {
                buttonA.wasJustPressed() -> {
                    actionQueue.addLast(outtake.armToIntakeAction())
                }
                buttonX.wasJustPressed() -> {
                    actionQueue.addLast(outtake.armToNeutralAction())
                }
                buttonY.wasJustPressed() -> {
                    actionQueue.addLast(outtake.armToBasketAction())
                }
                buttonB.wasJustPressed() -> {
                    actionQueue.clear()
                }
            }

            actionQueue.peekFirst()?.let {
                val running = it.run(TelemetryPacket())
                if (!running) {
                    actionQueue.removeFirst()
                }
            }

            telemetry.addData("actions", actionQueue.toList())
            telemetry.update()
        }
    }
}
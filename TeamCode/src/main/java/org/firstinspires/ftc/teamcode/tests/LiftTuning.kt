package org.firstinspires.ftc.teamcode.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.commonlibs.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

class LiftTuning : LinearOpMode() {
    @Config
    data object LiftTuningConfig {
        @JvmField
        var targetPos = 0.0
    }

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val timeKeep = TimeKeep()
        val lift = Robot(hardwareMap).lift

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            lift.targetPosition = LiftTuningConfig.targetPos

            lift.update(timeKeep.deltaTime)

            telemetry.addData("lift target pos", lift.targetPosition)
            telemetry.addData("lift pos", lift.position)
            telemetry.addData("lift power", lift.power)

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}
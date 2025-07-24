package org.firstinspires.ftc.teamcode.tuning

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.commonlibs.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

@TeleOp(group = "tuning")
class IntakeExtendoTuning : LinearOpMode() {
    @Config
    data object IntakeExtendoTuningConfig {
        @JvmField
        var targetPos = 0
    }

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val timeKeep = TimeKeep()
        val intake = Robot(hardwareMap).intake

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            intake.extendoTargetPosition = IntakeExtendoTuningConfig.targetPos

            intake.update(timeKeep.deltaTime)

            telemetry.addData("extendo pos", intake.extendoPosition)
            telemetry.addData("extendo target pos", intake.extendoTargetPosition)
            telemetry.addData("extendo power", intake.extendoPower)

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}
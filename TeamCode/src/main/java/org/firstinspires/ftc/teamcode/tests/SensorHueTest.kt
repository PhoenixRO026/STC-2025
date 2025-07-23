package org.firstinspires.ftc.teamcode.teleop.tests

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.commonlibs.units.s
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Robot

class SensorHueTest : LinearOpMode() {
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        val timeKeep = TimeKeep()
        val intake = Robot(hardwareMap).intake

        waitForStart()

        while (opModeIsActive()) {
            timeKeep.resetDeltaTime()

            intake.updateHue()

            telemetry.addData("sensor hue", intake.sensorHue)
            telemetry.addData("s", intake.hsv[1])
            telemetry.addData("v", intake.hsv[2])
            telemetry.addData("sensor color", intake.sensorColor)

            telemetry.addData("delta time ms", timeKeep.deltaTime.asMs)
            telemetry.addData("fps", 1.s / timeKeep.deltaTime)
            telemetry.update()
        }
    }
}
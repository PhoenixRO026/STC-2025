package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.psilynx.psikit.Logger
import org.psilynx.psikit.io.RLOGServer

@TeleOp
class TestTele : LinearOpMode() {
    override fun runOpMode() {
        Logger.addDataReceiver(RLOGServer())

    }
}
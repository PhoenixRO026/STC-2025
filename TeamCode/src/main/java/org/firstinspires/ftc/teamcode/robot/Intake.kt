package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo

class Intake (
    val tiltServo : Servo,
    val sweeperMotor : DcMotor,
    val extendoMotor : DcMotor,
    val extendoEncoder: DcMotor,
    val dumpServo : Servo
){
    var tiltPos by tiltServo::position
    var sweeperPower by sweeperMotor::power
    var extendoPower by extendoMotor::power
    val extendoPos get() = extendoEncoder.currentPosition
    var dumpPos by dumpServo::position

    fun tiltUp() {
        tiltPos = 0.2539
    }
    fun tiltDown() {
        tiltPos = 0.6624
    }
    fun dumpSample() {
        dumpPos = 0.1896
    }
    fun dumpDown() {
        dumpPos = 0.4929
    }
}
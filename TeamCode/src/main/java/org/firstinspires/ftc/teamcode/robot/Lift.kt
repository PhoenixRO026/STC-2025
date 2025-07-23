package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ftc.Encoder
import com.qualcomm.robotcore.hardware.DcMotor

class Lift (
    val leftMotor: DcMotor,
    val rightMotor: DcMotor,
    val encoder: DcMotor
){
    var power get() = rightMotor.power
        set(value) {
            leftMotor.power = value
            rightMotor.power = value
        }

    val pos get() = encoder.currentPosition
}
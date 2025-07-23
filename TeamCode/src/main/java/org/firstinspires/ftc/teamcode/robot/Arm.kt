package org.firstinspires.ftc.teamcode.robot

import com.qualcomm.robotcore.hardware.Servo

class Arm (
    val rightArmServo : Servo,
    val leftArmServo : Servo,
    val elbowServo : Servo,
    val wristServo: Servo,
    val clawServo : Servo
) {
    var clawPos by clawServo::position
    var armPos get() = rightArmServo.position
        set(value) {
            rightArmServo.position = value
            leftArmServo.position = value
        }
    var wristPos by wristServo::position
    var elbowPos by elbowServo::position

    fun wristUpright() {
        wristPos = 0.7839
    }
    fun wristUpsideDown() {
        wristPos = 0.2314
    }
    fun armToSpecimen1() {
        armPos = 0.6778
        elbowPos = 0.2877
    }
    fun armToSpecimen2() {
        armPos = 0.6425
        elbowPos = 0.8284
    }
    fun armToBoxIntake() {
        armPos = 0.7426
        elbowPos = 0.0997
    }
}
package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
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

    fun wristUpright() {wristPos = 0.7839}
    fun wristUpsideDown() {wristPos = 0.2314}
    fun armToSpecimen() {
        armPos = 0.3903
        elbowPos = 0.2511
    }
    fun armToBoxIntake() {
        armPos = 0.7513
        elbowPos = 0.1833
    }
    fun armToBasket() {
        armPos = 0.3185
        elbowPos = 0.8514
    }
    fun armToWall() {
        armPos = 0.0
        elbowPos = 0.2833
    }
    fun armToNeutral() {
        armPos = 0.5127
        elbowPos = 0.1461
    }
}
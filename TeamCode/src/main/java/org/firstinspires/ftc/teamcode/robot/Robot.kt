package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.Pose2d
import com.commonlibs.units.Pose
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcontroller.external.samples.RobotHardware
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class Robot (
    hardwareMap: HardwareMap,
    startPose: Pose2d
){
    val arm : Arm
    val intake : Intake
    val lift : Lift
    val drive = Drive(MecanumDrive(hardwareMap, startPose))

    init {
        val wristServo = hardwareMap.get(Servo::class.java, "wrist")
        val clawServo = hardwareMap.get(Servo::class.java, "claw")
        val elbowServo = hardwareMap.get(Servo::class.java, "elbow")
        val leftArmServo = hardwareMap.get(Servo::class.java, "leftArm")
        val rightArmServo = hardwareMap.get(Servo::class.java, "rightArm")

        leftArmServo.direction = Servo.Direction.REVERSE
        clawServo.scaleRange(0.429, 0.8255)

        arm = Arm(rightArmServo, leftArmServo, elbowServo, wristServo, clawServo)

        val tiltServo = hardwareMap.get(Servo::class.java, "intakeTilt")
        val sweeperMotor = hardwareMap.get(DcMotor::class.java, "intake")
        val extendoMotor = hardwareMap.get(DcMotor::class.java, "extendo")
        val extendoEncoder = hardwareMap.get(DcMotor::class.java, "motorLB")
        val dumpServo = hardwareMap.get(Servo::class.java, "box")

        extendoMotor.direction = DcMotorSimple.Direction.REVERSE
        sweeperMotor.direction = DcMotorSimple.Direction.REVERSE

        intake = Intake(tiltServo, sweeperMotor, extendoMotor, extendoEncoder, dumpServo)

        val leftMotor = hardwareMap.get(DcMotor::class.java, "leftLift")
        val rightMotor = hardwareMap.get(DcMotor::class.java, "rightLift")
        val liftEncoder = hardwareMap.get(DcMotor::class.java, "motorLF")

        rightMotor.direction = DcMotorSimple.Direction.REVERSE

        lift = Lift(leftMotor, rightMotor, liftEncoder)
    }
}
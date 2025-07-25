package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.commonlibs.units.Pose
import com.commonlibs.units.cm
import com.commonlibs.units.deg
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.tuning.ServoTest2.ServoTestConfig

class Robot (
    hardwareMap: HardwareMap,
    startPose: Pose = Pose(0.0.cm, 0.0.cm, 0.0.deg),
    resetEncoders: Boolean = true
){
    val arm : Arm
    val intake : Intake
    val lift : Lift
    val drive: Drive = Drive(MecanumDrive(hardwareMap, startPose.pose2d))

    init {
        val liftEncMotor = drive.mecanumDrive.leftFront
        val extendoEncMotor = drive.mecanumDrive.leftBack

        if (resetEncoders) {
            liftEncMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            liftEncMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            extendoEncMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            extendoEncMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        val wristServo = hardwareMap.get(Servo::class.java, "wrist")
        val clawServo = hardwareMap.get(Servo::class.java, "claw")
        val elbowServo = hardwareMap.get(Servo::class.java, "elbow")
        val leftArmServo = hardwareMap.get(Servo::class.java, "leftArm")
        val rightArmServo = hardwareMap.get(Servo::class.java, "rightArm")

        leftArmServo.direction = Servo.Direction.REVERSE
        clawServo.scaleRange(0.429, 0.8255)
        val offset = 0.015
        leftArmServo.scaleRange(0.0, 1.0 - offset)
        rightArmServo.scaleRange(0.0, 1.0 - offset)

        arm = Arm(rightArmServo, leftArmServo, elbowServo, wristServo, clawServo)

        val tiltServo = hardwareMap.get(Servo::class.java, "intakeTilt")
        val sweeperMotor = hardwareMap.get(DcMotor::class.java, "intake")
        val extendoMotor = hardwareMap.get(DcMotor::class.java, "extendo")
        val extendoEncoder = RawEncoder(extendoEncMotor)
        val dumpServo = hardwareMap.get(Servo::class.java, "box")
        val intakeColorSensor = hardwareMap.get(NormalizedColorSensor::class.java, "intakeColorSensor")

        extendoMotor.direction = DcMotorSimple.Direction.REVERSE
        sweeperMotor.direction = DcMotorSimple.Direction.REVERSE

        intake = Intake(tiltServo, sweeperMotor, extendoMotor, extendoEncoder, dumpServo, intakeColorSensor)

        val leftMotor = hardwareMap.get(DcMotor::class.java, "leftLift")
        val rightMotor = hardwareMap.get(DcMotor::class.java, "rightLift")
        val liftEncoder = RawEncoder(liftEncMotor)

        rightMotor.direction = DcMotorSimple.Direction.REVERSE
        liftEncoder.direction = DcMotorSimple.Direction.REVERSE

        lift = Lift(leftMotor, rightMotor, liftEncoder)
    }
}
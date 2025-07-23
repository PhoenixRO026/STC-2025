package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.commonlibs.units.Duration
import com.commonlibs.units.Pose
import com.commonlibs.units.SleepAction
import com.commonlibs.units.cm
import com.commonlibs.units.deg
import com.commonlibs.units.s
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive

class Robot(
    hardwareMap: HardwareMap,
    pose: Pose = Pose(0.0.cm, 0.0.cm, 0.0.deg),
    resetEncoders: Boolean = true
) {
    val drive: Drive
    val intake: Intake
    val lift: Lift
    val outtake: Outtake

    fun initTeleop() {
        intake.initTeleop()
        outtake.initTeleop()
    }

    fun initAuto() {
        intake.initAuto()
        outtake.initAuto()
    }

    fun update(deltaTime: Duration) {
        intake.update(deltaTime)
        lift.update(deltaTime)
        //outtake.update(deltaTime)
    }

    fun updatePoseEstimate() {
        drive.updatePoseEstimate()
    }

    fun armAndLiftToIntakeWaiting() = SequentialAction(
        lift.liftToIntakeWaitingAction(),
        outtake.armToIntakeAction(),
    )

    fun armAndLiftToIntake() = SequentialAction(
        ParallelAction(
            outtake.armToIntakeAction(),
            outtake.openClawAction()
        ),
        lift.liftToIntakeAction()
    )

    fun armAndLiftToWallAction() = SequentialAction(
        lift.liftDownAction(),
        outtake.armToWallAction(),
    )

    fun armAndLiftToNeutralAction() = ParallelAction(
        lift.liftDownAction(),
        outtake.armToNeutralAction()
    )

    fun armAndLiftToBarAction() = ParallelAction(
        lift.liftToBarAction(),
        outtake.armToBarAction()
    )

    fun robotToBarInstant() {
        lift.liftToBarInitInstant()
        outtake.armToBarInstant()
    }

    fun armAndLiftTakeSample() = SequentialAction(
        armAndLiftToIntakeWaiting(),
        SleepAction(1.0.s),
        armAndLiftToIntake()
    )

    fun turnOffAction() = ParallelAction(
        outtake.armToNeutralAction(),
        intake.sweeperOffAction(),
        intake.tiltUpAction(),
        InstantAction { intake.extendoPower = 0.0 },
        InstantAction { lift.power = 0.0 }
    )

    init {
        val mecanumDrive = MecanumDrive(hardwareMap, pose.pose2d)

        if (resetEncoders) {
            val liftEncMotor = mecanumDrive.rightBack
            liftEncMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            liftEncMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            val extendoEncMotor = mecanumDrive.leftFront
            extendoEncMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            extendoEncMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        val liftEncoder = RawEncoder(mecanumDrive.rightBack)
        val intakeExtendoEncoder = RawEncoder(mecanumDrive.leftFront)

        liftEncoder.direction = DcMotorSimple.Direction.FORWARD
        intakeExtendoEncoder.direction = DcMotorSimple.Direction.FORWARD

        val liftLeftMotor = hardwareMap.get(DcMotorEx::class.java, "motorLiftLeft")
        val liftRightMotor = hardwareMap.get(DcMotorEx::class.java, "motorLiftRight")
        val intakeExtendoMotor = hardwareMap.get(DcMotorEx::class.java, "motorExtendoIntake")
        val intakeSweeperMotor = hardwareMap.get(DcMotorEx::class.java, "motorSweeper")

        liftLeftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftRightMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        intakeExtendoMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        intakeSweeperMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        liftLeftMotor.direction = DcMotorSimple.Direction.REVERSE
        liftRightMotor.direction = DcMotorSimple.Direction.FORWARD

        intakeExtendoMotor.direction = DcMotorSimple.Direction.FORWARD
        intakeSweeperMotor.direction = DcMotorSimple.Direction.FORWARD

        liftLeftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRightMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intakeExtendoMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intakeSweeperMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE


        val intakeTiltServo = hardwareMap.get(Servo::class.java, "servoIntakeTilt")
        val intakeBoxServo = hardwareMap.get(Servo::class.java, "servoIntakeBox")
        val outtakeShoulderLeftServo = hardwareMap.get(Servo::class.java, "servoShoulderLeft")
        val outtakeShoulderRightServo = hardwareMap.get(Servo::class.java, "servoShoulderRight")
        val outtakeElbowServo = hardwareMap.get(Servo::class.java, "servoElbow")
        val outtakeWristServo = hardwareMap.get(Servo::class.java, "servoWrist")
        val outtakeClawServo = hardwareMap.get(Servo::class.java, "servoClaw")

        intakeTiltServo.direction = Servo.Direction.FORWARD
        intakeBoxServo.direction = Servo.Direction.FORWARD
        outtakeShoulderLeftServo.direction = Servo.Direction.FORWARD
        outtakeShoulderRightServo.direction = Servo.Direction.REVERSE
        outtakeElbowServo.direction = Servo.Direction.FORWARD
        outtakeWristServo.direction = Servo.Direction.FORWARD
        outtakeClawServo.direction = Servo.Direction.FORWARD

        intakeTiltServo.scaleRange(0.0, 1.0)
        intakeBoxServo.scaleRange(0.0, 1.0)
        outtakeShoulderLeftServo.scaleRange(0.0, 1.0)
        outtakeShoulderRightServo.scaleRange(0.0, 1.0)
        outtakeElbowServo.scaleRange(0.0, 1.0)
        outtakeWristServo.scaleRange(0.0, 1.0)
        outtakeClawServo.scaleRange(0.31, 0.6)

        val intakeColorSensor = hardwareMap.get(NormalizedColorSensor::class.java, "intakeColorSensor")

        drive = Drive(mecanumDrive)
        intake = Intake(
            extendoMotor = intakeExtendoMotor,
            sweeperMotor = intakeSweeperMotor,
            extendoEncoder = intakeExtendoEncoder,
            tiltServo = intakeTiltServo,
            boxServo = intakeBoxServo,
            colorSensor = intakeColorSensor
        )
        lift = Lift(
            leftMotor = liftLeftMotor,
            rightMotor = liftRightMotor,
            encoder = liftEncoder,

        )
        outtake = Outtake(
            shoulderLeftServo = outtakeShoulderLeftServo,
            shoulderRightServo = outtakeShoulderRightServo,
            elbowServo = outtakeElbowServo,
            wristServo = outtakeWristServo,
            clawServo = outtakeClawServo
        )
    }

    fun addTelemetry(telemetry: Telemetry, deltaTime: Duration) {
        intake.addTelemetry(telemetry)
        lift.addTelemetry(telemetry)
        telemetry.addData("delta time ms", deltaTime.asMs)
        telemetry.addData("fps", 1.s / deltaTime)
    }
}
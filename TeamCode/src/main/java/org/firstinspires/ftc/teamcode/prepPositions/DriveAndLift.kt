package org.firstinspires.ftc.teamcode.teleop.prepPositions

import com.acmerobotics.roadrunner.ftc.RawEncoder
import com.acmerobotics.roadrunner.now
import com.commonlibs.units.Pose
import com.commonlibs.units.cm
import com.commonlibs.units.deg
import com.commonlibs.units.pose
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import org.firstinspires.ftc.teamcode.robot.Drive
import org.firstinspires.ftc.teamcode.robot.Lift

@TeleOp
class DriveAndLift: LinearOpMode() {

    override fun runOpMode() {
        val hardwareMap : HardwareMap = hardwareMap
        val mecanumDrive = MecanumDrive(hardwareMap, Pose(0.0.cm,0.0.cm,0.0.deg).pose2d)
        val liftEncoder = RawEncoder(mecanumDrive.rightBack)
        val liftLeftMotor = hardwareMap.get(DcMotorEx::class.java, "motorLiftLeft")
        val liftRightMotor = hardwareMap.get(DcMotorEx::class.java, "motorLiftRight")
        liftLeftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftRightMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        liftLeftMotor.direction = DcMotorSimple.Direction.REVERSE
        liftRightMotor.direction = DcMotorSimple.Direction.FORWARD
        liftLeftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRightMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE


        val drive = Drive(mecanumDrive)
        val lift = Lift(
            leftMotor = liftLeftMotor,
            rightMotor = liftRightMotor,
            encoder = liftEncoder,
        )

        var previousTime: Double
        var deltaTime : Double
        var now : Double


        waitForStart()
        previousTime = now()

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now

            drive.updatePoseEstimate()

            if (gamepad1.y)
                drive.resetFieldCentric()

            drive.isSlowMode = gamepad1.right_trigger >= 0.2

            drive.driveFieldCentric(
                -gamepad1.left_stick_y.toDouble(),
                -gamepad1.left_stick_x.toDouble(),
                -gamepad1.right_stick_x.toDouble()
            )

            lift.power= -gamepad2.left_stick_y.toDouble()

            telemetry.addLine("controler albastru miscare")
            telemetry.addLine("controler rosu left stick vertical power la lift")
            telemetry.addData("a Pressed", gamepad1.a)
            telemetry.addData("y Pressed", gamepad1.y)
            telemetry.addData("x Pressed", gamepad1.x)
            telemetry.addData("b Pressed", gamepad1.b)
            telemetry.addData("deltaTime", deltaTime)
            telemetry.update()
        }
    }
}
package org.firstinspires.ftc.teamcode.tests

import com.acmerobotics.roadrunner.now
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class OuttakePositionsSpecial: LinearOpMode() {

    override fun runOpMode() {

        val servoClaw = hardwareMap.get(Servo::class.java, "claw")
        val servoWrist = hardwareMap.get(Servo::class.java, "wrist")
        val servoElbow = hardwareMap.get(Servo::class.java, "elbow")
        val servoShoulder = hardwareMap.get(Servo::class.java, "leftArm")
        val servoShoulderRight = hardwareMap.get(Servo::class.java, "rightArm")

        var previousTime: Double
        var deltaTime : Double
        var now : Double


        waitForStart()
        previousTime = now()

        servoClaw.position = 0.5
        servoWrist.position = 0.5
        servoElbow.position = 0.5
        servoShoulder.position = 0.5

        while (opModeIsActive()){
            now = now()
            deltaTime = now - previousTime
            previousTime = now

            if(gamepad1.a) {
                servoShoulder.position += 0.1 * deltaTime
            }
            else if(gamepad1.y) {
                servoShoulder.position -= 0.1 * deltaTime
            }
            if (gamepad1.x){
                servoElbow.position += 0.1 * deltaTime
            }
            else if(gamepad1.b){
                servoElbow.position -= 0.1 * deltaTime
            }
            if(gamepad1.dpad_up){
                servoWrist.position += 0.1 * deltaTime
            }
            else if(gamepad1.dpad_down){
                servoWrist.position -= 0.1 * deltaTime
            }
            if(gamepad1.left_bumper){
                servoClaw.position += 0.1 * deltaTime
            }
            else if(gamepad1.right_bumper){
                servoClaw.position -= 0.1 * deltaTime
            }



            telemetry.addData("a Pressed", gamepad1.a)
            telemetry.addData("y Pressed", gamepad1.y)
            telemetry.addData("x Pressed", gamepad1.x)
            telemetry.addData("b Pressed", gamepad1.b)
            telemetry.addData("ServoClaw", servoClaw.position)
            telemetry.addData("ServoWrist", servoWrist.position)
            telemetry.addData("ServoElbow", servoElbow.position)
            telemetry.addData("ServoShoulder", servoShoulder.position)
            telemetry.addData("deltaTime", deltaTime)
            telemetry.update()
        }
    }
}
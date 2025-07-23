package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ftc.Encoder
import com.commonlibs.units.Duration
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import org.firstinspires.ftc.teamcode.robot.Intake.IntakeConfig
import org.firstinspires.ftc.teamcode.robot.Intake.Mode
import kotlin.math.abs

class Lift(
    val leftMotor: DcMotorEx,
    val rightMotor: DcMotorEx,
    val encoder: Encoder,
) {
    @Config
    data object LiftConfig {
        @JvmField
        var controller = PIDController(
            kP = 0.01,
            kD = 0.00025,
            kI = 0.005,
            stabilityThreshold = 0.2
        )
        @JvmField
        var kF: Double = 0.1
        @JvmField
        var targetPosTolerance = 20.0

        @JvmField var basketPos = 2557.0
        @JvmField var intakePos = 400.0
        @JvmField var intakeWaitingPos = 570.0
        @JvmField var barInitPos = 691.0
        @JvmField var parkPose = 1000.0

        @JvmField var gearShiftLeftUp = 0.5244
        @JvmField var gearShiftLeftDown = 0.4328
        @JvmField var gearShiftRightUp = 0.5061
        @JvmField var gearShiftRightDown = 0.5
    }

    enum class Mode {
        PID,
        RAW_POWER
    }

    private var currentMode = Mode.RAW_POWER
    private var offset = 0.0

    val position get() = encoder.getPositionAndVelocity().position - offset

    private var _power
        get() = rightMotor.power
        set(value) {
            rightMotor.power = value
            leftMotor.power = value
        }

    var power
        get() = _power
        set(value) {
            if (currentMode == Mode.PID && value == 0.0) return

            _power = if (value < 0.0) value else value + LiftConfig.kF
            currentMode = Mode.RAW_POWER
        }

    var targetPosition = position
        set(value) {
            field = value
            currentMode = Mode.PID
        }


    fun resetPosition() {
        offset = encoder.getPositionAndVelocity().position
    }

    fun update(deltaTime: Duration) {
        if (currentMode == Mode.PID)
            _power = LiftConfig.controller.calculate(
                position.toDouble(),
                targetPosition.toDouble(),
                deltaTime
            ) + LiftConfig.kF
    }

    fun liftToPosAction(pos: Double) = object : Action {
        var init = true
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                targetPosition = pos
            }
            p.addLine("waiting for lift")
            return abs(targetPosition - position) > LiftConfig.targetPosTolerance
        }
    }

    fun liftToBarInitInstant() {
        targetPosition = LiftConfig.barInitPos
    }

    fun liftToBarAction() = liftToPosAction(LiftConfig.barInitPos)

    fun liftToBasketAction() = liftToPosAction(LiftConfig.basketPos)
    fun liftToIntakeAction() = liftToPosAction(LiftConfig.intakePos)
    fun liftToIntakeWaitingAction() = liftToPosAction(LiftConfig.intakeWaitingPos)
    fun liftDownAction() = liftToPosAction(40.0)
    fun liftToParking() = liftToPosAction(LiftConfig.parkPose)

    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("lift power", power)
        //telemetry.addData("lift current", rightMotor.getCurrent(CurrentUnit.AMPS) + leftMotor.getCurrent(CurrentUnit.AMPS))
    }

    fun resetLiftPos() {
        offset = encoder.getPositionAndVelocity().position
    }
}
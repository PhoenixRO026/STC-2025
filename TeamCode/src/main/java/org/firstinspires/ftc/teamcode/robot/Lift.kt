package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ftc.Encoder
import com.commonlibs.units.Duration
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import kotlin.math.abs
import kotlin.math.roundToInt

class Lift (
    val leftMotor: DcMotor,
    val rightMotor: DcMotor,
    val encoder: Encoder
){
    @Config
    data object LiftConfig {
        @JvmField
        var controller = PIDController(
            kP = 0.0225,
            kI = 0.001,
            kD = 0.0007,
            stabilityThreshold = 0.2
        )
        @JvmField
        var kF = 0.2
        @JvmField
        var targetPosTolerance = 20
    }// 525 is limit

    val maxPos = 810
    val basketPos = maxPos
    val barPos = 300
    val scoreBarPos = 200

    private var internalPower get() = rightMotor.power
        set(value) {
            leftMotor.power = value
            rightMotor.power = value
        }

    var power
        get() = internalPower
        set(value) {
            if (currentMode == Mode.PID && value == 0.0) return

            internalPower = if (value < 0.0) value else value + LiftConfig.kF
            currentMode = Mode.RAW_POWER
        }

    enum class Mode {
        PID,
        RAW_POWER
    }

    private var currentMode = Mode.RAW_POWER

    private var posOffset = 0

    val position get() = encoder.getPositionAndVelocity().position.roundToInt() - posOffset

    var targetPosition = position
        set(value) {
            field = value
            currentMode = Mode.PID
        }

    fun resetPosition() {
        posOffset = encoder.getPositionAndVelocity().position.roundToInt()
    }

    fun update(deltaTime: Duration) {
        if (currentMode == Mode.PID)
            internalPower = LiftConfig.controller.calculate(
                position.toDouble(),
                targetPosition.toDouble(),
                deltaTime
            ) + LiftConfig.kF
    }

    fun liftToPosAction(pos: Int) = object : Action {
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

    fun liftToBasket() = liftToPosAction(basketPos)
    fun liftToBar() = liftToPosAction(barPos)
    fun liftToBarUsedWithLift() = liftToPosAction(scoreBarPos)
    fun liftDown() = liftToPosAction(0)

    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("lift power", power)
    }
}
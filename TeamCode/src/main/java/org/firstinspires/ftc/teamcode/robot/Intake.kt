package org.firstinspires.ftc.teamcode.robot

import android.graphics.Color
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.RaceAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.Encoder
import com.commonlibs.units.Duration
import com.commonlibs.units.SleepAction
import com.commonlibs.units.s
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.NormalizedColorSensor
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.library.controller.PIDController
import kotlin.math.abs
import kotlin.math.roundToInt

class Intake (
    val tiltServo : Servo,
    val sweeperMotor : DcMotor,
    val extendoMotor : DcMotor,
    val extendoEncoder: Encoder,
    val dumpServo : Servo,
    val colorSensor: NormalizedColorSensor
){
    @Config
    data object IntakeConfig {
        @JvmField
        var controller = PIDController(
            kP = 0.015,
            kD = 0.00069420,
            kI = 0.001,
            stabilityThreshold = 0.2
        )
        @JvmField var targetPosTolerance = 10
        @JvmField var extendoLim = 600

        @JvmField var extendoIntake = 190
    }

    enum class SensorColor {
        YELLOW,
        BLUE,
        RED,
        NONE
    }

    enum class Mode {
        PID,
        RAW_POWER,
        ACTION
    }

    var sensorHue: Float = 0f

    var hsv = floatArrayOf(0f, 0f, 0f)

    val sensorColor get() = when {
        hsv[1] != 0f && sensorHue in 0f..40f -> SensorColor.RED
        hsv[1] != 0f && sensorHue in 200f..280f -> SensorColor.BLUE
        hsv[1] != 0f && sensorHue in 40f..110f -> SensorColor.YELLOW
        else -> SensorColor.NONE
    }

    fun updateHue() {
        val normalizedColors = colorSensor.normalizedColors
        Color.RGBToHSV(
            (normalizedColors.red * 256).toInt(),
            (normalizedColors.green * 256).toInt(),
            (normalizedColors.blue * 256).toInt(),
            hsv
        )
        sensorHue = hsv[0]
    }

    private var extendoOffset = 0

    private var extendoMode = Mode.RAW_POWER

    val extendoPosition get() = extendoEncoder.getPositionAndVelocity().position.roundToInt() - extendoOffset

    var extendoTargetPosition = extendoPosition
        set(value) {
            field = value
            extendoMode = Mode.PID
        }

    private var internalExtendoPower by extendoMotor::power

    var extendoPower
        get() = internalExtendoPower
        set(value) {
            if (extendoMode != Mode.RAW_POWER && value == 0.0) return
            internalExtendoPower = if (extendoPosition >= IntakeConfig.extendoLim) value.coerceAtMost(0.0) else value
            extendoMode = Mode.RAW_POWER
        }

    fun resetExtendoPosition() {
        extendoOffset = extendoEncoder.getPositionAndVelocity().position.roundToInt()
    }

    fun update(deltaTime: Duration) {
        if (extendoMode != Mode.RAW_POWER)
            internalExtendoPower = IntakeConfig.controller.calculate(extendoPosition.toDouble(), extendoTargetPosition.toDouble(), deltaTime)
    }

    fun extendoToPosAction(pos: Int) = object : Action {
        var init = true

        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                extendoTargetPosition = pos
                extendoMode = Mode.ACTION
            }
            p.addLine("waiting for extendo")
            return abs(extendoTargetPosition - extendoPosition) > IntakeConfig.targetPosTolerance
        }

    }

    fun addTelemetry(telemetry: Telemetry) {
        telemetry.addData("sensor hue", sensorHue)
        telemetry.addData("sensor color", sensorColor)
        telemetry.addData("sweeper power", sweeperPower)
        telemetry.addData("extendo power", extendoPower)
        telemetry.addData("extendoPos", extendoPosition)
    }

    fun waitForColorAction(waitColor: SensorColor, maxTime: Duration = 1.s) = RaceAction(
        {
            updateHue()
            it.addLine("Waiting for $waitColor")
            sensorColor != waitColor
        },
        SleepAction(maxTime)
    )

    var tiltPos by tiltServo::position
    var sweeperPower by sweeperMotor::power
    var dumpPos by dumpServo::position

    fun tiltUp() {
        tiltPos = 0.2156
    }
    fun tiltDown() {
        tiltPos = 0.6694
    }
    fun dumpSample() {
        dumpPos = 0.1896
    }
    fun dumpDown() {
        dumpPos = 0.4929
    }
    fun tiltUpAction() = InstantAction {tiltPos = 0.2539}
    fun tiltDownAction() = InstantAction {tiltPos = 0.6624}
    fun extendoToMaxPos() = extendoToPosAction(600)
    fun sweeperOnAction() = InstantAction {sweeperPower = 1.0}
    fun sweeperOffAction() = InstantAction {sweeperPower = 0.0}

    fun sweeperBoxAction() = InstantAction {sweeperPower = 1.0}

    fun extendoReadyForSampleling() = ParallelAction (
        extendoToMaxPos(),
        tiltUpAction()
    )

    fun takeOutSample() = SequentialAction(
        sweeperBoxAction(),
        waitForColorAction(SensorColor.NONE),
        sweeperOffAction()
    )

    fun takeSample(color: SensorColor, maxTime: Duration = 0.5.s) = SequentialAction(
        ParallelAction(
            sweeperOnAction(),
            InstantAction {
                tiltDownAction()
            },
            waitForColorAction(color, maxTime),
        ),
        sweeperOffAction()
    )

    fun passSampleWithDump() = InstantAction {dumpPos = 0.1896}
}
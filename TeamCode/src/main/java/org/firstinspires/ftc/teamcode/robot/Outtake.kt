package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.commonlibs.units.SleepAction
import com.commonlibs.units.s
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.abs

class Outtake(
    val shoulderLeftServo: Servo,
    val shoulderRightServo: Servo,
    val elbowServo: Servo,
    val wristServo: Servo,
    val clawServo: Servo
) {
    @Config
    data object OuttakeConfig {
        //SLEEP
        @JvmField var shoulderActionSleepDuration = 2.s
        @JvmField var elbowActionSleepDuration = 2.s
        @JvmField var wristActionSleepDuration = 2.s
        @JvmField var clawActionSleepDuration = 0.1.s
        //NEUTRAL
        @JvmField var shoulderNeutralPos = 0.5233
        @JvmField var elbowNeutralPos = 1.0
        //WRIST
        @JvmField var wristNormalPos = 0.5239
        @JvmField var wristReversePos = 0.0
        //CLAW
        @JvmField var clawOpenPos = 1.0
        @JvmField var clawClosedPos = 0.0
        //INIT TELE
        @JvmField var shoulderTeleInit = shoulderNeutralPos
        @JvmField var elbowTeleInit = elbowNeutralPos
        @JvmField var wristTeleInit = wristNormalPos
        @JvmField var clawTeleInit = clawClosedPos
        //INIT AUTO
        @JvmField var shoulderAutoInit = 0.5228
        @JvmField var elbowAutoInit = 0.939
        @JvmField var wristAutoInit = wristNormalPos
        @JvmField var clawAutoInit = clawClosedPos
        //INTAKE POS
        @JvmField var shoulderIntakePos = 0.7933
        @JvmField var elbowIntakePos = 0.7256
        //BASKET POS
        @JvmField var shoulderBasketPos = 0.3678
        @JvmField var elbowBasketPos = 0.2472
        //WALL POS
        @JvmField var shoulderWallPos = 0.0889
        @JvmField var elbowWallPos = 0.7239
        //BAR POS
        @JvmField var shoulderBarPos = 0.7011
        @JvmField var elbowBarPos = 0.0356
    }

    var shoulderPos
        get() = shoulderLeftServo.position
        set(value) {
            shoulderLeftServo.position = value
            shoulderRightServo.position = value
        }

    var elbowPos
        get() = elbowServo.position
        set(value) {
            elbowServo.position = value
        }

    var wristPos
        get() = wristServo.position
        set(value) {
            wristServo.position = value
        }

    var clawPos: Double = 0.5
        get() = clawServo.position
        set(value) {
            val clampedVal = value.coerceIn(0.0, 1.0)
            if (clampedVal == field) return
            field = clampedVal
            clawServo.position = field
        }

    fun initTeleop() {
        shoulderPos = OuttakeConfig.shoulderTeleInit
        elbowPos = OuttakeConfig.elbowTeleInit
        clawPos = OuttakeConfig.clawTeleInit
        wristPos = OuttakeConfig.wristTeleInit
    }

    fun initAuto() {
        shoulderPos = OuttakeConfig.shoulderAutoInit
        elbowPos = OuttakeConfig.elbowAutoInit
        clawPos =  OuttakeConfig.clawAutoInit
        wristPos = OuttakeConfig.wristAutoInit
    }

    fun shoulderToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == shoulderPos) return false
                sleepAction = SleepAction(OuttakeConfig.shoulderActionSleepDuration * abs(pos - shoulderPos))
                shoulderPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun elbowToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == elbowPos) return false
                sleepAction = SleepAction(OuttakeConfig.elbowActionSleepDuration * abs(pos - elbowPos))
                elbowPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun wristToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == wristPos) return false
                sleepAction = SleepAction(OuttakeConfig.wristActionSleepDuration * abs(pos - wristPos))
                wristPos = pos
            }
            return sleepAction.run(p)
        }
    }

    fun clawToPosAction(pos: Double) = object : Action {
        var init = true
        lateinit var sleepAction: Action
        override fun run(p: TelemetryPacket): Boolean {
            if (init) {
                init = false
                if (pos == clawPos) return false
                sleepAction = SleepAction(OuttakeConfig.clawActionSleepDuration * abs(pos - clawPos))
                clawServo.position = pos
            }
            return sleepAction.run(p)
        }
    }
    // CLAW
    fun openClawAction() = clawToPosAction(OuttakeConfig.clawOpenPos)
    fun closeClawAction() = clawToPosAction(OuttakeConfig.clawClosedPos)

    // WRIST
    fun wristToNormalAction() = wristToPosAction(OuttakeConfig.wristNormalPos)
    fun wristToNormalInstant() {
        wristPos = OuttakeConfig.wristNormalPos
    }
    fun wristToUpsideDownInstant() {
        wristPos = OuttakeConfig.wristReversePos
    }

    fun wristToReverseAction() = wristToPosAction(OuttakeConfig.wristReversePos)

    // ARM
    fun shoulderToNeutralAction() = shoulderToPosAction(OuttakeConfig.shoulderNeutralPos)
    fun elbowToNeutralAction() = elbowToPosAction(OuttakeConfig.elbowNeutralPos)

    fun shoulderToIntakeAction() = shoulderToPosAction(OuttakeConfig.shoulderIntakePos)
    fun elbowToIntakeAction() = elbowToPosAction(OuttakeConfig.elbowIntakePos)

    fun shoulderToBarAction() = shoulderToPosAction(OuttakeConfig.shoulderBarPos)
    fun elbowToBarAction() = elbowToPosAction(OuttakeConfig.elbowBarPos)

    fun shoulderToBasketAction() = shoulderToPosAction(OuttakeConfig.shoulderBasketPos)
    fun elbowToBasketAction() = elbowToPosAction(OuttakeConfig.elbowBasketPos)

    fun shoulderToWallAction() = shoulderToPosAction(OuttakeConfig.shoulderWallPos)
    fun elbowToWallAction() = elbowToPosAction(OuttakeConfig.elbowWallPos)
    fun wristToWallAction() = wristToPosAction(OuttakeConfig.wristReversePos)


    fun armToWallInstant() {
        shoulderPos = OuttakeConfig.shoulderWallPos
        elbowPos = OuttakeConfig.elbowWallPos
    }

    fun armToWallAction() = ParallelAction(
        shoulderToWallAction(),
        elbowToWallAction(),
        wristToWallAction(),
        openClawAction()
    )

    fun armToNeutralAction() = ParallelAction(
        shoulderToNeutralAction(),
        elbowToNeutralAction(),
        wristToNormalAction()
    )

    fun armToNeutralInstant() {
        shoulderPos = OuttakeConfig.shoulderNeutralPos
        elbowPos = OuttakeConfig.elbowNeutralPos
    }

    fun armToIntakeAction() = ParallelAction(
        shoulderToIntakeAction(),
        elbowToIntakeAction(),
        wristToNormalAction()
    )

    fun armToBasketInstant() {
        shoulderPos = OuttakeConfig.shoulderBasketPos
        elbowPos = OuttakeConfig.elbowBasketPos
        wristPos = OuttakeConfig.wristNormalPos
    }

    fun armToBarInstant() {
        shoulderPos = OuttakeConfig.shoulderBarPos
        elbowPos = OuttakeConfig.elbowBarPos
    }

    fun armToBarAction() = ParallelAction(
        wristToNormalAction(),
        shoulderToBarAction(),
        elbowToBarAction(),
    )

    fun armToBasketAction() = ParallelAction(
        shoulderToBasketAction(),
        elbowToBasketAction(),
        wristToNormalAction()
    )
}
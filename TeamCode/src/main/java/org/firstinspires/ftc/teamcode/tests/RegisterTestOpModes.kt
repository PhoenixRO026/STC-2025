package org.firstinspires.ftc.teamcode.teleop.tests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta

@Suppress("unused")
object RegisterTestOpModes {
    private const val GROUP: String = "test"
    private const val DISABLED: Boolean = false

    private fun metaForClass(cls: Class<out OpMode?>): OpModeMeta {
        return OpModeMeta.Builder()
            .setName(cls.simpleName)
            .setGroup(GROUP)
            .setFlavor(OpModeMeta.Flavor.TELEOP)
            .build()
    }

    @JvmStatic
    @OpModeRegistrar
    fun register(manager: OpModeManager) {
        if (DISABLED) return
        manager.register(metaForClass(LiftTuning::class.java), LiftTuning())
        manager.register(metaForClass(IntakeExtendoTuning::class.java), IntakeExtendoTuning())
        manager.register(metaForClass(SensorHueTest::class.java), SensorHueTest())
        manager.register(metaForClass(OuttakeActionsTest::class.java), OuttakeActionsTest())
    }
}
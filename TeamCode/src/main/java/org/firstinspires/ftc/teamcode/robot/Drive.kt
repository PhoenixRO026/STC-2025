package org.firstinspires.ftc.teamcode.robot

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.PoseVelocity2dDual
import com.acmerobotics.roadrunner.Vector2d
import com.commonlibs.units.rotate
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive
import kotlin.math.cos
import kotlin.math.sin

class Drive (
    val mecanumDrive : MecanumDrive
){
    val heading get() = mecanumDrive.localizer.pose.heading.toDouble() - headingOffset
    private var headingOffset = 0.0
    fun resetHeadind() {
        headingOffset = mecanumDrive.localizer.pose.heading.toDouble()
    }

    fun driveFieldCentric(forward : Double, strafe : Double, rotate : Double) {
        val driveVec = PoseVelocity2d(
            Vector2d(
                forward * speed,
                strafe * speed
            ).rotate(-heading),
            rotate * speed
        )
        mecanumDrive.setDrivePowers(driveVec)
    }

    var snipperMode : Boolean = false
    val speed get() = if (snipperMode) 0.5 else 1.0
}
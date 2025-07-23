@file:JvmName("MeepMeep")
package com.meep.meepmeep

import com.commonlibs.roadrunnerext.ex
import com.commonlibs.units.Distance2d
import com.commonlibs.units.Pose
import com.commonlibs.units.cm
import com.commonlibs.units.deg
import com.commonlibs.units.inch
import com.commonlibs.units.s
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

data object RedBasket {
    val startPose = Pose(-38.inch, -61.inch, 90.deg)
    val basketPose = Pose(-52.inch, -52.inch, 45.deg)
    val firstYellowSample = Distance2d(-48.2.inch, -25.4.inch)
    val firstYellowPose = Distance2d(-48.inch, -51.inch).headingTowards(firstYellowSample)
    val secondYellowSample = Distance2d(-58.3.inch, -26.1.inch)
    val secondYellowPose = Distance2d(-52.inch, -50.inch).headingTowards(secondYellowSample)
    val thirdYellowSample = Distance2d(-68.9.inch, -25.6.inch)
    val thirdYellowPose = Distance2d(-47.inch, -45.inch).headingTowards(thirdYellowSample)
    val parkPose = Pose(-24.inch, -12.inch, 0.deg)
}

data object BlueBasket {
    val startPose = Pose(36.inch, 60.inch, -90.deg)
    val basketPose = Pose(54.inch, 54.inch, -135.deg)
    val firstYellowSample = Distance2d(48.inch, 25.5.inch)
    val firstYellowPose = Distance2d(48.inch, 50.inch).headingTowards(firstYellowSample)
    val secondYellowSample = Distance2d(58.5.inch, 25.5.inch)
    val secondYellowPose = Distance2d(52.inch, 50.inch).headingTowards(secondYellowSample)
    val thirdYellowSample = Distance2d(68.5.inch, 25.5.inch)
    val thirdYellowPose = Distance2d(56.inch, 50.inch).headingTowards(thirdYellowSample)
    val parkPose = Pose(24.inch, 12.inch, 180.deg)
}

data object RedSpecimen {
    val startPose = Pose(14.cm, -63.inch, 90.deg)
    val preloadSpecimenPos = Pose(14.cm, -30.5.inch, 90.deg)
    val firstSpecimenPos = Pose(2.inch, -30.5.inch, 90.deg)
    val secondSpecimenPos =Pose(0.inch, -30.5.inch, 90.deg)
    val thirdSpecimenPos =Pose(-1.inch, -30.5.inch, 90.deg)

    val sample3Heading = Distance2d(68.inch, -25.inch)

    val sample1 = Pose(48.inch, -49.inch, 90.deg)
    val sample2 = Pose(58.5.inch, -49.inch, 90.deg)
    val sample3 = Distance2d(61.inch, -49.inch).headingTowards(sample3Heading)

    val zonePos = Distance2d(45.1.inch, -67.2.inch)
    val zonePoze3 = Distance2d(50.inch, -67.2.inch)

    val firstKickPos = Distance2d(30.inch, -50.inch).headingTowards(zonePos)
    val secondKickPos = Distance2d(34.inch, -50.inch).headingTowards(zonePos)
    val thirdKickPos = Distance2d(38.inch, -50.inch).headingTowards(zonePos)
    val takeSpecimenPos = Pose(40.inch, -58.5.inch, 90.deg)
}

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(600)

    val redBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 100.0, Math.PI * 3.0 / 2.0 , Math.PI * 2, 14.5)
            .setDimensions(32.5.cm.asInch, 33.cm.asInch)
            .build()

    val blueBot =
        DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .setDimensions(32.5.cm.asInch, 33.cm.asInch)
            .build()

    //basketAuto(redBot, blueBot)
    specimenAuto(redBot, blueBot)

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(redBot)
        .addEntity(blueBot)
        .start()
}

fun specimenAuto(redBot: RoadRunnerBotEntity, blueBot: RoadRunnerBotEntity) {
    redBot.runAction(redBot.drive.actionBuilder(RedSpecimen.startPose.pose2d).ex()
        .lineToY(RedSpecimen.preloadSpecimenPos.position.y)

        .setTangent(-90.deg)
        .splineToLinearHeading(RedSpecimen.sample1, 0.deg)
        .waitSeconds(2.s)

        .setTangent(0.deg)
        .lineToX(RedSpecimen.sample2.position.x)
        .waitSeconds(2.s)

        .setTangent(0.deg)
        .lineToXLinearHeading(RedSpecimen.sample3.position.x, RedSpecimen.sample3.heading)
        .waitSeconds(2.s)
        .turnTo(90.deg)


        //start to specimen
        .setTangent(135.deg)
        .splineToLinearHeading(RedSpecimen.takeSpecimenPos, -90.deg)

        .setTangent(165.deg)
        .splineToLinearHeading(RedSpecimen.firstSpecimenPos, 90.deg)

        .setTangent(-45.deg)
        .splineToLinearHeading(RedSpecimen.takeSpecimenPos, -90.deg)

        .setTangent(165.deg)
        .splineToLinearHeading(RedSpecimen.secondSpecimenPos, 90.deg)

        .setTangent(-45.deg)
        .splineToLinearHeading(RedSpecimen.takeSpecimenPos, -90.deg)

        .setTangent(165.deg)
        .splineToLinearHeading(RedSpecimen.thirdSpecimenPos, 90.deg)
        .build()
    )

    blueBot.runAction(blueBot.drive.actionBuilder(BlueBasket.startPose.pose2d).ex()
        .build()
    )
}

fun basketAuto(redBot: RoadRunnerBotEntity, blueBot: RoadRunnerBotEntity) {
    redBot.runAction(redBot.drive.actionBuilder(RedBasket.startPose.pose2d).ex()
        .strafeToLinearHeading(RedBasket.basketPose)

        .strafeToLinearHeading(RedBasket.firstYellowPose)
        .strafeToLinearHeading(RedBasket.basketPose)

        .strafeToLinearHeading(RedBasket.secondYellowPose)
        .strafeToLinearHeading(RedBasket.basketPose)

        .strafeToLinearHeading(RedBasket.thirdYellowPose)
        .strafeToLinearHeading(RedBasket.basketPose)

        .setTangent(90.deg)
        .splineToSplineHeading(RedBasket.parkPose - 20.cm.x,0.0.deg)
        .lineToX(RedBasket.parkPose.position.x)
        .build()
    )

    blueBot.runAction(blueBot.drive.actionBuilder(BlueBasket.startPose.pose2d).ex()
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.firstYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.secondYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.thirdYellowPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.basketPose)
        .waitSeconds(1.s)
        .strafeToLinearHeading(BlueBasket.parkPose)
        .build()
    )
}
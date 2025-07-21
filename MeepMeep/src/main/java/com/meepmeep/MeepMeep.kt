@file:JvmName("MeepMeep")
package com.meepmeep

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

fun main() {
    System.setProperty("sun.java2d.opengl", "true")

    val meepMeep = MeepMeep(800);

    val myBot: RoadRunnerBotEntity = DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setConstraints(60.0, 60.0, Math.toRadians(180.0), Math.toRadians(180.0), 15.0)
            .build()

    myBot.runAction(myBot.drive.actionBuilder(Pose2d(0.0, 0.0, 0.0))
            .lineToX(-45.0)
            .turn(Math.toRadians(35.0))
            .lineToY(-45.0)
            .turn(Math.toRadians(90.0))
            .lineToX(0.0)
            .turn(Math.toRadians(90.0))
            .lineToY(0.0)
            .turn(Math.toRadians(90.0))
            .build())

    meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start()
}
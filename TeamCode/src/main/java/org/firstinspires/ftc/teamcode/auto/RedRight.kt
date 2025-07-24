package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.RaceAction
import com.acmerobotics.roadrunner.SequentialAction
import com.commonlibs.roadrunnerext.delayedBy
import com.commonlibs.units.Distance2d
import com.commonlibs.units.Pose
import com.commonlibs.units.SleepAction
import com.commonlibs.units.cm
import com.commonlibs.units.deg
import com.commonlibs.units.inch
import com.commonlibs.units.s
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.library.TimeKeep
import org.firstinspires.ftc.teamcode.robot.Intake
import org.firstinspires.ftc.teamcode.robot.Robot

@Autonomous
class RedRight : LinearOpMode() {
    val startPose = Pose(14.cm, -63.inch, 90.deg)
    val preloadSpecimenPos = Pose(14.cm, -30.5.inch, 90.deg)
    val zerothSpecimenPos = Pose(2.inch, -30.5.inch, 90.deg)
    val firstSpecimenPos = Pose(0.inch, -30.5.inch, 90.deg)
    val secondSpecimenPos =Pose(-2.inch, -30.5.inch, 90.deg)
    val thirdSpecimenPos =Pose(-4.inch, -30.5.inch, 90.deg)

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

    override fun runOpMode() {
        initMessage()

        val timeKeep = TimeKeep()
        val robot = Robot(hardwareMap, startPose)
        val drive = robot.drive
        val intake = robot.intake
        val lift = robot.lift
        val outtake = robot.outtake
        robot.initAuto()

        telemetry.addData("claw", robot.outtake.clawPos)

        val preload = SequentialAction(
            ParallelAction(
            drive.actionBuilder(startPose)
                    .lineToY(preloadSpecimenPos.position.y)
                    .build().delayedBy(0.1.s),
                robot.armAndLiftToIntakeWaiting()
            ),
            outtake.armToScoreAction()
        )

        val sampleToHuman = SequentialAction(
            intake.takeSample(Intake.SensorColor.RED),
            intake.sampleToBox(),
            intake.boxOpenAction()
        )

        val takeOffWall0 = SequentialAction(
            ParallelAction(
                drive.actionBuilder(Pose(sample3.position.x, sample3.position.y, 90.deg))
                    .setTangent(135.deg)
                    .splineToLinearHeading(takeSpecimenPos, -90.deg)
                    .build().delayedBy(0.1.s),
                robot.armAndLiftToWallAction(),
            ),
            SleepAction(0.5.s),
            outtake.closeClawAction()
        )

        val takeOffWall1 = SequentialAction(
            ParallelAction(
                drive.actionBuilder(firstSpecimenPos)
                    .setTangent(135.deg)//fix
                    .splineToLinearHeading(takeSpecimenPos, -90.deg)
                    .build().delayedBy(0.1.s),
                robot.armAndLiftToWallAction(),
            ),
            SleepAction(0.5.s),
            outtake.closeClawAction()
        )

        val takeOffWall2 = SequentialAction(
            ParallelAction(
                drive.actionBuilder(secondSpecimenPos)
                    .setTangent(135.deg)
                    .splineToLinearHeading(takeSpecimenPos, -90.deg)
                    .build().delayedBy(0.1.s),
                robot.armAndLiftToWallAction(),
            ),
            SleepAction(0.5.s),
            outtake.closeClawAction()
        )

        val takeOffWall3 = SequentialAction(
            ParallelAction(
                drive.actionBuilder(thirdSpecimenPos)
                    .setTangent(135.deg)
                    .splineToLinearHeading(takeSpecimenPos, -90.deg)
                    .build().delayedBy(0.1.s),
                robot.armAndLiftToWallAction(),
            ),
            SleepAction(0.5.s),
            outtake.closeClawAction()
        )

        val scoreSpecimen0 = SequentialAction(
            outtake.closeClawAction(),
            ParallelAction(
                drive.actionBuilder(takeSpecimenPos)
                    .setTangent(165.deg)
                    .splineToLinearHeading(firstSpecimenPos, 90.deg)
                    .build(),
                robot.armAndLiftToBarAction()
            ),
            outtake.armToScoreAction()
        )

        val scoreSpecimen1 = SequentialAction(
            outtake.closeClawAction(),
            ParallelAction(
            drive.actionBuilder(takeSpecimenPos)
                .setTangent(165.deg)
                .splineToLinearHeading(firstSpecimenPos, 90.deg)
                .build(),
                robot.armAndLiftToBarAction()
            ),
            outtake.armToScoreAction()
        )

        val scoreSpecimen2 = SequentialAction(
            outtake.closeClawAction(),
            ParallelAction(
                drive.actionBuilder(takeSpecimenPos)
                    .setTangent(165.deg)
                    .splineToLinearHeading(secondSpecimenPos, 90.deg)
                    .build(),
                robot.armAndLiftToBarAction()
            ),
            outtake.armToScoreAction()
        )

        val scoreSpecimen3 = SequentialAction(
            outtake.closeClawAction(),
            ParallelAction(
                drive.actionBuilder(takeSpecimenPos)
                    .setTangent(165.deg)
                    .splineToLinearHeading(thirdSpecimenPos, 90.deg)
                    .build(),
                robot.armAndLiftToBarAction()
            ),
            outtake.armToScoreAction()
        )

        val action = SequentialAction(
            preload,

            ParallelAction(
                drive.actionBuilder(preloadSpecimenPos)
                    .setTangent(-90.deg)
                    .splineToLinearHeading(sample1, 0.deg)
                    .build().delayedBy(0.1.s),
                robot.armAndLiftToNeutralAction()
            ),
            sampleToHuman,

            drive.actionBuilder(sample1)
                .setTangent(0.deg)
                .lineToX(sample2.position.x)
                .build().delayedBy(0.1.s),
            sampleToHuman,

            drive.actionBuilder(sample2)
                .setTangent(0.deg)
                .lineToXLinearHeading(sample3.position.x, sample3.heading)
                .build().delayedBy(0.1.s),
            sampleToHuman,

            drive.actionBuilder(sample3)
                .turnTo(90.deg)
                .build().delayedBy(0.1.s),
            takeOffWall0,
            scoreSpecimen0,
            takeOffWall1,
            scoreSpecimen1,
            takeOffWall2,
            scoreSpecimen2,
            takeOffWall3,
            scoreSpecimen3
        )

        action.preview(previewCanvas)

        readyMessage()

        while (opModeInInit()) {
            timeKeep.resetDeltaTime()
            sleep(50)
        }

        var running = true

        while (isStarted && !isStopRequested && running) {
            timeKeep.resetDeltaTime()
            robot.update(timeKeep.deltaTime)

            running = runAction(action)
            telemetry.addData("wrist Pos", robot.outtake.wristPos)
            robot.addTelemetry(telemetry, timeKeep.deltaTime)
            telemetry.update()
        }
    }

    private val previewCanvas = Canvas()

    private fun runAction(action: Action): Boolean {
        val packet = TelemetryPacket()
        packet.fieldOverlay().operations.addAll(previewCanvas.operations)

        val running = action.run(packet)

        FtcDashboard.getInstance().sendTelemetryPacket(packet)

        return running
    }

    private fun initMessage() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        telemetry.addLine("INITIALIZING")

        telemetry.update()
    }

    private fun readyMessage() {
        telemetry.addLine("READY")
        telemetry.update()
    }
}
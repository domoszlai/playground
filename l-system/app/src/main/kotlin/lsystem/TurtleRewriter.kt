package lsystem

import lsystem.turtle.Turtle
import lsystem.turtle.TurtleCommand

import lsystem.turtle.Forward as TForward
import lsystem.turtle.PenDown as TPenDown
import lsystem.turtle.PenUp as TPenUp
import lsystem.turtle.PushState as TPushState
import lsystem.turtle.PopState as TPopState
import lsystem.turtle.TurnLeft as TTurnLeft
import lsystem.turtle.TurnRight as TTurnRight
import lsystem.turtle.TurnAround as TTurnAround
import lsystem.turtle.PitchDown as TPitchDown
import lsystem.turtle.PitchUp as TPitchUp
import lsystem.turtle.RollLeft as TRollLeft
import lsystem.turtle.RollRight as TRollRight

fun Turtle.execute(
    rewriter: Rewriter<TurtleRewriterNode>,
    axiom: List<TurtleRewriterNode>,
    n: Int
){
    val nodes = rewriter.rewrite(axiom, n)
    var turtleCommands = nodes.flatMap { it.turtleCommands }
    this.execute(turtleCommands)
}

fun Turtle.execute(
    rewriter: ParametricRewriter<TurtleRewriterNode>,
    axiom: List<TurtleRewriterNode>,
    n: Int
){
    val nodes = rewriter.rewrite(axiom, n)
    var turtleCommands = nodes.flatMap { it.turtleCommands }
    this.execute(turtleCommands)
}

open class TurtleRewriterNode(
    val turtleCommands: List<TurtleCommand> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    companion object {
        fun createForwardDrawingNode(name: String = "F"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TPenDown, TForward))

        fun createForwardNonDrawingNode(name: String = "f"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TPenUp, TForward))

        fun createTurnLeftNode(name: String = "+"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TTurnLeft))

        fun createTurnRightNode(name: String = "-"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TTurnRight))

        fun createTurnAroundNode(name: String = "|"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TTurnAround))

        fun createPitchDownNode(name: String = "&"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TPitchDown))

        fun createPitchUpNode(name: String = "^"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TPitchUp))

        fun createRollLeftNode(name: String = "\\"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TRollLeft))

        fun createRollRightNode(name: String = "/"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TRollRight))

        fun createCustomNode(name: String): TurtleRewriterNode =
            TurtleRewriterNode(listOf())

        fun createPushStateNode(name: String = "["): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TPushState))

        fun createPopStateNode(name: String = "]"): TurtleRewriterNode =
            TurtleRewriterNode(listOf(TPopState))
    }
}

open class Forward() : TurtleRewriterNode(listOf(TPenDown, TForward))
open class TurnLeft() : TurtleRewriterNode(listOf(TTurnLeft))
open class TurnRight() : TurtleRewriterNode(listOf(TTurnRight))
open class PushState() : TurtleRewriterNode(listOf(TPushState))
open class PopState() : TurtleRewriterNode(listOf(TPopState))
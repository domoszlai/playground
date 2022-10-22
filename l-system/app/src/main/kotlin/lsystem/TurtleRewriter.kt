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
)

open class ForwardDrawing() : TurtleRewriterNode(listOf(TPenDown, TForward))
open class ForwardNonDrawing() : TurtleRewriterNode(listOf(TPenUp, TForward))
open class TurnLeft() : TurtleRewriterNode(listOf(TTurnLeft))
open class TurnRight() : TurtleRewriterNode(listOf(TTurnRight))
open class TurnAround() : TurtleRewriterNode(listOf(TTurnAround))
open class RollLeft() : TurtleRewriterNode(listOf(TRollLeft))
open class RollRight() : TurtleRewriterNode(listOf(TRollRight))
open class PitchDown() : TurtleRewriterNode(listOf(TPitchDown))
open class PitchUp() : TurtleRewriterNode(listOf(TPitchUp))
open class PushState() : TurtleRewriterNode(listOf(TPushState))
open class PopState() : TurtleRewriterNode(listOf(TPopState))
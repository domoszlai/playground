package lsystem

import lsystem.turtle.Turtle
import lsystem.turtle.TurtleCommand

import lsystem.turtle.Forward as TForward
import lsystem.turtle.PenDown as TPenDown
import lsystem.turtle.PenUp as TPenUp
import lsystem.turtle.SetLineWidth as TSetLineWidth
import lsystem.turtle.PushState as TPushState
import lsystem.turtle.PopState as TPopState
import lsystem.turtle.TurnLeft as TTurnLeft
import lsystem.turtle.TurnRight as TTurnRight
import lsystem.turtle.TurnAround as TTurnAround
import lsystem.turtle.PitchDown as TPitchDown
import lsystem.turtle.PitchUp as TPitchUp
import lsystem.turtle.RollLeft as TRollLeft
import lsystem.turtle.RollRight as TRollRight
import lsystem.turtle.RollUp as TRollUp

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

open class ForwardDrawing(private val stepSize: Double? = null) :
    TurtleRewriterNode(listOf(TPenDown(), TForward(stepSize)))
open class ForwardNonDrawing() : TurtleRewriterNode(listOf(TPenUp(), TForward()))
open class TurnLeft(private val angleIncrementDegrees: Double? = null) :
    TurtleRewriterNode(listOf(TTurnLeft(angleIncrementDegrees)))
open class TurnRight(private val angleIncrementDegrees: Double? = null) :
    TurtleRewriterNode(listOf(TTurnRight(angleIncrementDegrees)))
open class TurnAround() : TurtleRewriterNode(listOf(TTurnAround()))
open class RollLeft(private val angleIncrementDegrees: Double? = null) :
    TurtleRewriterNode(listOf(TRollLeft(angleIncrementDegrees)))
open class RollRight(private val angleIncrementDegrees: Double? = null) :
    TurtleRewriterNode(listOf(TRollRight(angleIncrementDegrees)))
open class RollUp() : TurtleRewriterNode(listOf(TRollUp()))
open class PitchDown(private val angleIncrementDegrees: Double? = null) :
    TurtleRewriterNode(listOf(TPitchDown(angleIncrementDegrees)))
open class PitchUp(private val angleIncrementDegrees: Double? = null) :
    TurtleRewriterNode(listOf(TPitchUp(angleIncrementDegrees)))
open class PushState() : TurtleRewriterNode(listOf(TPushState()))
open class PopState() : TurtleRewriterNode(listOf(TPopState()))
open class SetLineWidth(private val lineWidth: Double):
    TurtleRewriterNode(listOf(TSetLineWidth(lineWidth)))
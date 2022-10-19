package lsystem

import kotlin.math.cos
import kotlin.math.sin

data class TurtleState (
    val x: Double = 0.0,
    val y: Double = 0.0,
    val headingDegrees: Double = 270.0, // faces up, SVG coordinate system
    val drawing: Boolean = true
) {
    val headingRadians = Math.toRadians(headingDegrees)
}

data class TurtleParams (
    val stepSize: Double = 1.0,
    val angleIncrementDegrees: Double = 90.0
) {
    val angleIncrementRadians = Math.toRadians(angleIncrementDegrees)
}

sealed interface PrimTurtleCommand {
    fun execute (params: TurtleParams, st: TurtleState) : TurtleState
}

object PenDown : PrimTurtleCommand {
    override fun execute(params: TurtleParams, st: TurtleState): TurtleState {
        return st.copy(drawing = true)
    }
}

object PenUp : PrimTurtleCommand {
    override fun execute(params: TurtleParams, st: TurtleState): TurtleState {
        return st.copy(drawing = false)
    }
}

object Left : PrimTurtleCommand {
    override fun execute(params: TurtleParams, st: TurtleState): TurtleState {
        return st.copy(headingDegrees = st.headingDegrees - params.angleIncrementDegrees)
    }
}

object Right : PrimTurtleCommand {
    override fun execute(params: TurtleParams, st: TurtleState): TurtleState {
        return st.copy(headingDegrees = st.headingDegrees + params.angleIncrementDegrees)
    }
}

object Forward : PrimTurtleCommand {
    override fun execute(params: TurtleParams, st: TurtleState): TurtleState {
        return st.copy(
            x = st.x + params.stepSize * cos(st.headingRadians),
            y = st.y + params.stepSize * sin(st.headingRadians)
        )
    }
}

data class TurtleCommand(val name: String, val primCommands: List<PrimTurtleCommand>) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }
}

class Turtle (private val params: TurtleParams = TurtleParams()) {

    // Generates drawing commands for the SVG coordinate system
    fun interpret(commands: List<TurtleCommand>): List<DrawCmd> {
        var st = TurtleState()
        var primCommands = commands.flatMap { it.primCommands }
        var path = listOf<DrawCmd>(MoveTo(st.x, st.y))

        for (cmd in primCommands) {
            val newState = cmd.execute(params, st)
            if(cmd === Forward){
                path = if (st.drawing) {
                    path + LineTo(newState.x, newState.y)
                } else {
                    path + MoveTo(newState.x, newState.y)
                }
            }
            st = newState
        }

        return path
    }

    companion object {
        fun createForwardDrawingCmd(name: String = "F"): TurtleCommand
            = TurtleCommand(name, listOf(PenDown, Forward))

        fun createForwardNonDrawingCmd(name: String = "f"): TurtleCommand
            = TurtleCommand(name, listOf(PenUp, Forward))

        fun createLeftCmd(name: String = "+"): TurtleCommand
            = TurtleCommand(name, listOf(Left))

        fun createRightCmd(name: String = "-"): TurtleCommand
            = TurtleCommand(name, listOf(Right))

        // Can be used for node rewriting
        fun createDummyCmd(name: String): TurtleCommand
            = TurtleCommand(name, listOf())
    }
}
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
    fun execute (params: TurtleParams, sts: Stack<TurtleState>) : Stack<TurtleState>
}

object PenDown : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = true) }
    }
}

object PenUp : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = false) }
    }
}

object Left : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(headingDegrees = it.headingDegrees - params.angleIncrementDegrees)}
    }
}

object Right : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(headingDegrees = it.headingDegrees + params.angleIncrementDegrees)}
    }
}

object Forward : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(
            x = it.x + params.stepSize * cos(it.headingRadians),
            y = it.y + params.stepSize * sin(it.headingRadians)
        )}
    }
}

object PushState : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        val top = sts.peek()
        return if(top != null) sts.push(top) else sts
    }
}

object PopState : PrimTurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.pop()
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
        var initialState = TurtleState()
        var sts = listOf<TurtleState>(initialState)
        var primCommands = commands.flatMap { it.primCommands }
        var path = listOf<DrawCmd>(MoveTo(initialState.x, initialState.y))

        for (cmd in primCommands) {
            val oldState = sts.peek()
            sts = cmd.execute(params, sts)
            val newState = sts.peek()

            if(oldState != null && newState != null){
                if(cmd === Forward) {
                    path = if (oldState.drawing) {
                        path + LineTo(newState.x, newState.y)
                    } else {
                        path + MoveTo(newState.x, newState.y)
                    }
                }
                else if(cmd == PopState) {
                    if(oldState.x != newState.x || oldState.y != newState.y) {
                        path = path + MoveTo(newState.x, newState.y)
                    }
                }
            }
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

        fun createDummyCmd(name: String): TurtleCommand
            = TurtleCommand(name, listOf())

        fun createPushStateCmd(name: String): TurtleCommand
                = TurtleCommand(name, listOf(PushState))

        fun createPopStateCmd(name: String): TurtleCommand
                = TurtleCommand(name, listOf(PopState))
    }
}
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

sealed interface TurtleCommand {
    fun execute (params: TurtleParams, sts: Stack<TurtleState>) : Stack<TurtleState>
}

object PenDown : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = true) }
    }
}

object PenUp : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = false) }
    }
}

object Left : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(headingDegrees = it.headingDegrees - params.angleIncrementDegrees)}
    }
}

object Right : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(headingDegrees = it.headingDegrees + params.angleIncrementDegrees)}
    }
}

object Forward : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(
            x = it.x + params.stepSize * cos(it.headingRadians),
            y = it.y + params.stepSize * sin(it.headingRadians)
        )}
    }
}

object PushState : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        val top = sts.peek()
        return if(top != null) sts.push(top) else sts
    }
}

object PopState : TurtleCommand {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.pop()
    }
}



class Turtle (private val params: TurtleParams = TurtleParams()) {

    // Generates drawing commands for the SVG coordinate system
    fun interpret(commands: List<TurtleCommand>): List<DrawCmd> {
        var initialState = TurtleState()
        var sts = listOf<TurtleState>(initialState)
        var path = listOf<DrawCmd>(MoveTo(initialState.x, initialState.y))

        for (cmd in commands) {
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
}
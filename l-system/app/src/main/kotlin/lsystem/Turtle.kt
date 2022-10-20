package lsystem

import kotlin.math.cos
import kotlin.math.sin

data class TurtleState (
    val p: Point3D = Point3D(0.0, 0.0, 0.0),
    val H: Vector3D = Vector3D(0.0, 1.0, 0.0), // heading, faces up
    val L: Vector3D = Vector3D(1.0, 0.0, 0.0), // left
    val U: Vector3D = Vector3D(0.0, 0.0, 1.0), // up
    val drawing: Boolean = true
)

data class TurtleParams (
    val stepSize: Double = 1.0,
    val angleIncrementDegrees: Double = 90.0
) {
    val angleIncrementRadians = Math.toRadians(angleIncrementDegrees)
}

sealed class TurtleCommand {
    abstract fun execute (params: TurtleParams, sts: Stack<TurtleState>) : Stack<TurtleState>

    protected fun rotate(v: Vector3D, pv: Vector3D, angleRadian: Double)
        = v * cos(angleRadian) + pv * sin(angleRadian)

    override fun equals(other: Any?): Boolean {
        return this === other
    }
}

object PenDown : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = true) }
    }
}

object PenUp : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = false) }
    }
}

object TurnLeft : TurtleCommand() { // Yaw
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            it.copy(
                H = rotate(it.H, it.L, -params.angleIncrementRadians),
                L = rotate(it.L, -it.H, -params.angleIncrementRadians),
            )
        }
    }
}

object TurnRight : TurtleCommand() { // Yaw
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            it.copy(
                H = rotate(it.H, it.L, params.angleIncrementRadians),
                L = rotate(it.L, -it.H, params.angleIncrementRadians),
            )
        }
    }
}

object Forward : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(
            p = it.p + it.H * params.stepSize
        )}
    }
}

object PushState : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        val top = sts.peek()
        return if(top != null) sts.push(top) else sts
    }
}

object PopState : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.pop()
    }
}

class Turtle(private val params: TurtleParams = TurtleParams(), val canvas: Canvas = Canvas()) {

    private val initialState = TurtleState()
    private var sts = listOf<TurtleState>(initialState)

    init {
        canvas.moveTo(initialState.p)
    }

    fun execute(commands: List<TurtleCommand>){
        for(cmd in commands) {
            execute(cmd)
        }
    }

    fun execute(cmd: TurtleCommand) {
        val oldState = sts.peek()
        sts = cmd.execute(params, sts)
        val newState = sts.peek()

        if(oldState != null && newState != null){
            when(cmd) {
                Forward ->
                    if (oldState.drawing) {
                        canvas.lineTo(newState.p)
                    } else {
                        canvas.moveTo(newState.p)
                    }
                PopState ->
                    if(oldState.p != newState.p) {
                        canvas.moveTo(newState.p)
                    }
            }
        }
    }
}
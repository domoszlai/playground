package lsystem.turtle

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

import lsystem.*

data class TurtleState (
    val p: Point3D = Point3D(0.0, 0.0, 0.0),
    val H: Vector3D = Vector3D(0.0, 0.0, 1.0), // heading
    val L: Vector3D = Vector3D(1.0, 0.0, 0.0), // left
    val U: Vector3D = Vector3D(0.0, 1.0, 0.0), // up
    val drawing: Boolean = true,
    val lineWidth: Double
)

data class TurtleParams (
    val stepSize: Double = 1.0,
    val angleIncrementDegrees: Double = 90.0,
    val lineWidth: Double = 1.0 / 10.0
)

sealed class TurtleCommand {
    abstract fun execute (params: TurtleParams, sts: Stack<TurtleState>) : Stack<TurtleState>

    protected fun rotate(v: Vector3D, pv: Vector3D, angleRadian: Double)
        = v * cos(angleRadian) + pv * sin(angleRadian)

    override fun equals(other: Any?): Boolean {
        return this === other
    }
}

class PenDown : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = true) }
    }
}

class PenUp : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(drawing = false) }
    }
}

class SetLineWidth(private val lineWidth: Double) : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(lineWidth = lineWidth) }
    }
}

class TurnLeft(private val angleIncrementDegrees: Double? = null) : TurtleCommand() { // Yaw
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val angle = -Math.toRadians(angleIncrementDegrees ?: params.angleIncrementDegrees)
            it.copy(
                H = rotate(it.H, it.L, angle),
                L = rotate(it.L, -it.H, angle),
            )
        }
    }
}

class TurnRight(private val angleIncrementDegrees: Double? = null) : TurtleCommand() { // Yaw
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val angle = Math.toRadians(angleIncrementDegrees ?: params.angleIncrementDegrees)
            it.copy(
                H = rotate(it.H, it.L, angle),
                L = rotate(it.L, -it.H, angle),
            )
        }
    }
}

class TurnAround : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            it.copy(
                H = rotate(it.H, it.L, PI),
                L = rotate(it.L, -it.H, PI),
            )
        }
    }
}

class PitchDown(private val angleIncrementDegrees: Double? = null) : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val angle = -Math.toRadians(angleIncrementDegrees ?: params.angleIncrementDegrees)
            it.copy(
                H = rotate(it.H, it.U, angle),
                U = rotate(it.U, -it.H, angle),
            )
        }
    }
}

class PitchUp(private val angleIncrementDegrees: Double? = null) : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val angle = Math.toRadians(angleIncrementDegrees ?: params.angleIncrementDegrees)
            it.copy(
                H = rotate(it.H, it.U, angle),
                U = rotate(it.U, -it.H, angle),
            )
        }
    }
}

class RollLeft(private val angleIncrementDegrees: Double? = null) : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val angle = -Math.toRadians(angleIncrementDegrees ?: params.angleIncrementDegrees)
            it.copy(
                L = rotate(it.L, it.U, angle),
                U = rotate(it.U, -it.L, angle),
            )
        }
    }
}

class RollRight(private val angleIncrementDegrees: Double? = null) : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val angle = Math.toRadians(angleIncrementDegrees ?: params.angleIncrementDegrees)
            it.copy(
                L = rotate(it.L, it.U, angle),
                U = rotate(it.U, -it.L, angle),
            )
        }
    }
}

// Rolls the turtle so that L is brought to horizontal position
class RollToHorizontal() : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop {
            val V = Vector3D(0.0, 0.0, 1.0) // direction opposite to gravity
            //val n = V.cross(it.H).normalize()
            it.copy(
                L = V.cross(it.H).normalize(),
                U = it.H.cross(it.L),
            )
        }
    }
}

class Forward(private val stepSize: Double? = null) : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.updateTop { it.copy(
            p = it.p + it.H * (stepSize ?: params.stepSize)
        )}
    }
}

class PushState : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        val top = sts.peek()
        return if(top != null) sts.push(top) else sts
    }
}

class PopState : TurtleCommand() {
    override fun execute(params: TurtleParams, sts: Stack<TurtleState>): Stack<TurtleState> {
        return sts.pop()
    }
}

class Turtle(private val params: TurtleParams = TurtleParams(), val canvas: Canvas = Canvas()) {

    private val initialState = TurtleState(lineWidth = params.lineWidth)
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
                is Forward ->
                    if (oldState.drawing) {
                        canvas.lineTo(newState.p, oldState.lineWidth)
                    } else {
                        canvas.moveTo(newState.p)
                    }
                is PopState ->
                    if(oldState.p != newState.p) {
                        canvas.moveTo(newState.p)
                    }
            }
        }
    }
}
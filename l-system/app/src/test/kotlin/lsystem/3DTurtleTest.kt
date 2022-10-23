package lsystem

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class `3DTurtleTest` {

    val F = ForwardDrawing()
    val `+` = TurnLeft()
    val `-` = TurnRight()
    val `|` = TurnAround()
    val `&` = PitchDown()
    val `^` = PitchUp()
    val Rl = RollLeft()
    val Rr = RollRight()
    val `$` = RollToHorizontal()
    val `(` = PushState()
    val `)` = PopState()

    @Test
    fun basicMovement() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        val obj = generateOBJ(r, listOf(F, F, F, `-`, F, F, `^`, F, `(`, `+`, F, `)`, Rr, `+`, F, `&`, F), 1)

        File("/users/dlacko/Desktop/gen.obj").writeText(obj)
        //assertEquals(getResourceAsText("/turtle/3d/basicMovement.obj"), obj)
    }

    @Test
    fun rollToHorizontal() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        val basecase = generateOBJ(r, listOf(F, F, `&`, F, Rr, `+`, F), 1)
        val noturn = generateOBJ(r, listOf(F, F, `&`, F, Rr, `$`, F), 1)
        val newleft = generateOBJ(r, listOf(F, F, `&`, F, Rr, `$`, `+`, F), 1)

        assertEquals(getResourceAsText("/turtle/3d/rth_basecase.obj"), basecase)
        assertEquals(getResourceAsText("/turtle/3d/rth_noturn.obj"), noturn)
        assertEquals(getResourceAsText("/turtle/3d/rth_newleft.obj"), newleft)
    }

    @Test
    fun hilbertCurve() {

        class ANode : TurtleRewriterNode()
        class BNode : TurtleRewriterNode()
        class CNode : TurtleRewriterNode()
        class DNode : TurtleRewriterNode()

        val A = ANode()
        val B = BNode()
        val C = CNode()
        val D = DNode()

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(A.symbol to listOf(B, `-`, F, `+`, C, F, C, `+`, F, `-`, D, `&`, F, `^`, D, `-`, F, `+`, `&`, `&`, C, F, C, `+`, F, `+`, B, Rr, Rr ))
        r.addRule(B.symbol to listOf(A, `&`, F, `^`, C, F, B, `^`, F, `^`, D, `^`, `^`, `-`, F, `-`, D, `^`, `|`, F, `^`, B, `|`, F, C, `^`, F, `^`, A, Rr, Rr ))
        r.addRule(C.symbol to listOf(`|`, D, `^`, `|`, F, `^`, B, `-`, F, `+`, C, `^`, F, `^`, A, `&`, `&`, F, A, `&`, F, `^`, C, `+`, F, `+`, B, `^`, F, `^`, D, Rr, Rr))
        r.addRule(D.symbol to listOf(`|`, C, F, B, `-`, F, `+`, B, `|`, F, A, `&`, F, `^`, A, `&`, `&`, F, B, `-`, F, `+`, B, `|`, F, C, Rr, Rr))
        val obj = generateOBJ(r, listOf(A), 3)

        File("/users/dlacko/Desktop/gen.obj").writeText(obj)

        assertEquals(getResourceAsText("/turtle/3d/hilbertCurve.obj"), obj)
    }

}
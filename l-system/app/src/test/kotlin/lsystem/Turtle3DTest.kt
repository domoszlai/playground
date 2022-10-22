package lsystem

import org.junit.Test
import kotlin.test.assertEquals

class Turtle3DTest {

    class ANode : TurtleRewriterNode()
    class BNode : TurtleRewriterNode()
    class CNode : TurtleRewriterNode()
    class DNode : TurtleRewriterNode()

    val F = ForwardDrawing()
    val A = ANode()
    val B = BNode()
    val C = CNode()
    val D = DNode()
    val `+` = TurnLeft()
    val `-` = TurnRight()
    val `|` = TurnAround()
    val `&` = PitchDown()
    val `^` = PitchUp()
    val `Rf` = RollLeft()
    val `Rr` = RollRight()

    @Test
    fun hilbertCurve() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(A.symbol to listOf(B, `-`, F, `+`, C, F, C, `+`, F, `-`, D, `&`, F, `^`, D, `-`, F, `+`, `&`, `&`, C, F, C, `+`, F, `+`, B, Rr, Rr ))
        r.addRule(B.symbol to listOf(A, `&`, F, `^`, C, F, B, `^`, F, `^`, D, `^`, `^`, `-`, F, `-`, D, `^`, `|`, F, `^`, B, `|`, F, C, `^`, F, `^`, A, Rr, Rr ))
        r.addRule(C.symbol to listOf(`|`, D, `^`, `|`, F, `^`, B, `-`, F, `+`, C, `^`, F, `^`, A, `&`, `&`, F, A, `&`, F, `^`, C, `+`, F, `+`, B, `^`, F, `^`, D, Rr, Rr))
        r.addRule(D.symbol to listOf(`|`, C, F, B, `-`, F, `+`, B, `|`, F, A, `&`, F, `^`, A, `&`, `&`, F, B, `-`, F, `+`, B, `|`, F, C, Rr, Rr))
        val obj = generateOBJ(r, listOf(A), 3)

        assertEquals(getResourceAsText("/3d/hilbert.obj"), obj)
    }
}
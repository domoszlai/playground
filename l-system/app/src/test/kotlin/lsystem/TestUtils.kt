package lsystem

import lsystem.turtle.Turtle
import lsystem.turtle.TurtleParams
import lsystem.turtle.output.toObj
import lsystem.turtle.output.toSVG
import kotlin.reflect.KClass

val<T: Any> T.symbol: KClass<T>
    get() = javaClass.kotlin

fun generateSVG(
    rewriter: ParametricRewriter<TurtleRewriterNode>,
    axiom: List<TurtleRewriterNode>,
    n: Int,
    params: TurtleParams = TurtleParams()
) : String {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    return turtle.canvas.toSVG()
}

fun generateOBJ(
    rewriter: ParametricRewriter<TurtleRewriterNode>,
    axiom: List<TurtleRewriterNode>,
    n: Int,
    params: TurtleParams = TurtleParams()
) : String {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    return turtle.canvas.toObj()
}

fun getResourceAsText(path: String): String? =
    object {}.javaClass.getResource(path)?.readText()

fun String.deleteWhitespace() : String = replace("\\s".toRegex(), "")


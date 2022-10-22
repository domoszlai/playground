package lsystem

import lsystem.turtle.Turtle
import lsystem.turtle.TurtleParams
import lsystem.turtle.output.toObj
import lsystem.turtle.output.toSVG
import java.io.File

fun generateSVG(rewriter: Rewriter<TurtleRewriterNode>, axiom: List<TurtleRewriterNode>, n: Int, params: TurtleParams = TurtleParams()) {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    val svg = turtle.canvas.toSVG()
    File("c:\\users\\dlacko\\desktop\\gen.svg").writeText(svg)
}

fun generateObj(rewriter: Rewriter<TurtleRewriterNode>, axiom: List<TurtleRewriterNode>, n: Int, params: TurtleParams = TurtleParams()) {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    val obj = turtle.canvas.toObj()
    File("c:\\users\\dlacko\\desktop\\gen.obj").writeText(obj)
}

fun main() {

    // Edge rewriting
    val F = TurtleRewriterNode.createForwardDrawingNode()
    val Fl = TurtleRewriterNode.createForwardDrawingNode()
    val Fr = TurtleRewriterNode.createForwardDrawingNode()
    val f = TurtleRewriterNode.createForwardNonDrawingNode()
    val `+` = TurtleRewriterNode.createTurnLeftNode()
    val `-` = TurtleRewriterNode.createTurnRightNode()
    val `|` = TurtleRewriterNode.createTurnAroundNode()
    val `&` = TurtleRewriterNode.createPitchDownNode()
    val `^` = TurtleRewriterNode.createPitchUpNode()
    val `Rf` = TurtleRewriterNode.createRollLeftNode()
    val `Rr` = TurtleRewriterNode.createRollRightNode()
    val `(` = TurtleRewriterNode.createPushStateNode()
    val `)` = TurtleRewriterNode.createPopStateNode()
    // Node rewriting
    val L = TurtleRewriterNode.createCustomNode("L")
    val R = TurtleRewriterNode.createCustomNode("R")
    val X = TurtleRewriterNode.createCustomNode("X")
    val A = TurtleRewriterNode.createCustomNode("A")
    val B = TurtleRewriterNode.createCustomNode("B")
    val C = TurtleRewriterNode.createCustomNode("C")
    val D = TurtleRewriterNode.createCustomNode("D")
    val S = TurtleRewriterNode.createCustomNode("S")


    val hilbertCurve3D = Rewriter<TurtleRewriterNode>()
    hilbertCurve3D.addRule(A to listOf(B, `-`, F, `+`, C, F, C, `+`, F, `-`, D, `&`, F, `^`, D, `-`, F, `+`, `&`, `&`, C, F, C, `+`, F, `+`, B, Rr, Rr ))
    hilbertCurve3D.addRule(B to listOf(A, `&`, F, `^`, C, F, B, `^`, F, `^`, D, `^`, `^`, `-`, F, `-`, D, `^`, `|`, F, `^`, B, `|`, F, C, `^`, F, `^`, A, Rr, Rr ))
    hilbertCurve3D.addRule(C to listOf(`|`, D, `^`, `|`, F, `^`, B, `-`, F, `+`, C, `^`, F, `^`, A, `&`, `&`, F, A, `&`, F, `^`, C, `+`, F, `+`, B, `^`, F, `^`, D, Rr, Rr))
    hilbertCurve3D.addRule(D to listOf(`|`, C, F, B, `-`, F, `+`, B, `|`, F, A, `&`, F, `^`, A, `&`, `&`, F, B, `-`, F, `+`, B, `|`, F, C, Rr, Rr))
    //generateObj(hilbertCurve3D, listOf(A), 3)

    val test = Rewriter<TurtleRewriterNode>()
    //generateObj(test, listOf(F,F,F,`-`,F,F,`-`,F,`-`,F,`+`,F,`+`,F,F,`-`,F,`-`,F,F,F), 1)
    //generateObj(test, listOf(F,F,F,`&`,F), 1)
}

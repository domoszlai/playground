package lsystem

fun Turtle.execute(
    rewriter: Rewriter<TurtleRewriterNode>,
    axiom: List<TurtleRewriterNode>,
    n: Int
){
    val nodes = rewriter.rewrite(axiom, n)
    var turtleCommands = nodes.flatMap { it.turtleCommands }
    this.execute(turtleCommands)
}

class TurtleRewriterNode(
    private val name: String,
    val turtleCommands: List<TurtleCommand>
) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    companion object {
        fun createForwardDrawingNode(name: String = "F"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PenDown, Forward))

        fun createForwardNonDrawingNode(name: String = "f"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PenUp, Forward))

        fun createLeftNode(name: String = "+"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(Left))

        fun createRightNode(name: String = "-"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(Right))

        fun createCustomNode(name: String): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf())

        fun createPushStateNode(name: String = "["): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PushState))

        fun createPopStateNode(name: String = "]"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PopState))
    }
}
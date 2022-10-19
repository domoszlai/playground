package lsystem

object TurtleRewriter {
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

    fun createPushStateNode(name: String): TurtleRewriterNode =
        TurtleRewriterNode(name, listOf(PushState))

    fun createPopStateNode(name: String): TurtleRewriterNode =
        TurtleRewriterNode(name, listOf(PopState))

    fun toTurtleCommands(nodes: List<TurtleRewriterNode>) : List<TurtleCommand> {
        return nodes.flatMap { it.turtleCommands }
    }
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
}
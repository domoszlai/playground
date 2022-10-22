package lsystem

import kotlin.test.Test
import kotlin.test.assertContentEquals

class RewriterTest {

    @Test fun basicNonParametric() {

        data class F(val dummy: Unit = Unit) : TurtleRewriterNode()
        data class G(val dummy: Unit = Unit) : TurtleRewriterNode()
        data class H(val dummy: Unit = Unit) : TurtleRewriterNode()

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F::class, listOf(G()))
        r.addRule(G::class, listOf(H()))

        assertContentEquals(listOf(G()),  r.rewrite(listOf(F()), 1))
        assertContentEquals(listOf(H()),  r.rewrite(listOf(F()), 2))
    }
}

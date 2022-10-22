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

    @Test fun basicParametric() {

        data class A(val x: Int, val y: Int) : TurtleRewriterNode()
        data class B(val x: Int) : TurtleRewriterNode()
        data class C(val dummy: Unit = Unit) : TurtleRewriterNode()

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(A::class, {it.y <= 3}, {listOf(A(it.x*2, it.x+it.y))})
        r.addRule(A::class, {it.y > 3}, {listOf(B(it.x), A(it.x/it.y, 0))})
        r.addRule(B::class, {it.x < 1}, {listOf(C())})
        r.addRule(B::class, {it.x >= 1}, {listOf(B(it.x-1))})

        val axiom = listOf(B(2), A(4,4))

        assertContentEquals(listOf(B(1), B(4), A(1,0)), r.rewrite(axiom, 1))
        assertContentEquals(listOf(B(0), B(3), A(2,1)), r.rewrite(axiom, 2))
        assertContentEquals(listOf(C(), B(2), A(4,3)), r.rewrite(axiom, 3))
        assertContentEquals(listOf(C(), B(1), A(8,7)), r.rewrite(axiom, 4))
    }
}

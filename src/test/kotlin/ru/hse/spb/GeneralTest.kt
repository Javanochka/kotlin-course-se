package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class GeneralTest {

    private val out = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(out))
    }

    @Test
    fun testArithmeticWithoutFunctions() {
        ru.hse.spb.run("""
            var a = 4
            var b = a * 2 + 3
            var c = (a * b) / (a + b) - b % a
            println(a, b, c)
        """.trimIndent())
        assertEquals("4 11 -1", out.toString().trim())
    }

    @Test
    fun testArithmeticWithFunction() {
        ru.hse.spb.run("""
            fun square(a) {
                return a * a
            }
            var a = 4
            var b = a * 2 + 3
            var c = (square(b + 1)) / (a + b) - b % a
            println(a, b, c)
        """.trimIndent())
        assertEquals("4 11 6", out.toString().trim())
    }

    @Test
    fun testFibonacciFunction() {
        ru.hse.spb.run("""
            fun fib(n) {
                if (n == 1 || n == 2) {
                    return 1
                }
                return fib(n - 1) + fib(n - 2)
            }
            println(fib(4), fib(6))
        """.trimIndent())
        assertEquals("3 8", out.toString().trim())
    }

    @Test
    fun testWhileWithoutFunction() {
        ru.hse.spb.run("""
            var n = 0
            var deg = 1
            while(n < 10) {
                deg = deg * 2
                n = n + 1
            }
            println(n, deg)
        """.trimIndent())
        assertEquals("10 1024", out.toString().trim())
    }

    @Test(expected = InterpreterException::class)
    fun testUsageOfNonDeclaredVariable() {
        ru.hse.spb.run("""
            println(n, deg)
        """.trimIndent())
    }
}
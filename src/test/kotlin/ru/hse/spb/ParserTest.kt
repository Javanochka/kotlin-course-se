package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.hse.spb.parser.FunLangLexer
import ru.hse.spb.parser.FunLangParser

class ParserTest {
    @Test
    fun testExpression() {
        val expLexer = FunLangLexer(CharStreams.fromString("a * (b + 203) - a"))
        val parser = FunLangParser(BufferedTokenStream(expLexer))
        val file = parser.file()
        assertEquals("(file (block (statement " +
                "(expression (expression (expression a) * (expression " +
                "( (expression (expression b) + (expression 203)) )))" +
                " - (expression a)))) <EOF>)", file.toStringTree(parser))
    }
    @Test
    fun testVariableDeclaration() {
        val expLexer = FunLangLexer(CharStreams.fromString("var a = 4"))
        val parser = FunLangParser(BufferedTokenStream(expLexer))
        val file = parser.file()
        assertEquals("(file (block (statement " +
                "(variable var a = (expression 4)))) <EOF>)", file.toStringTree(parser))
    }

    @Test
    fun testBranching() {
        val expLexer = FunLangLexer(CharStreams.fromString("""
            if (a == b) {
                println(2)
            } else {
                println(a, b)
            }""".trimIndent()))
        val parser = FunLangParser(BufferedTokenStream(expLexer))
        val file = parser.file()
        assertEquals("(file (block (statement (ifStatement if " +
                "( (expression (expression a) == (expression b)) ) " +
                "(blockWithBraces { " +
                "(block (statement (expression " +
                "(functionCall println ( (arguments (expression 2)) ))))) }" +
                ") else " +
                "(blockWithBraces { (block (statement (expression " +
                "(functionCall " +
                "println ( (arguments (expression a) , (expression b)) ))))) })))) <EOF>)", file.toStringTree(parser))
    }

    @Test
    fun testFunctionDeclaration() {
        val expLexer = FunLangLexer(CharStreams.fromString("""
            fun meow(a, b) {
                return a + b
            }""".trimIndent()))
        val parser = FunLangParser(BufferedTokenStream(expLexer))
        val file = parser.file()
        assertEquals("(file (block (statement " +
                "(function fun meow ( (parameterNames a , b) ) " +
                "(blockWithBraces { (block (statement " +
                "(returnStatement return " +
                "(expression (expression a) + (expression b))))) }))))" +
                " <EOF>)", file.toStringTree(parser))
    }

    @Test
    fun testWhile() {
        val expLexer = FunLangLexer(CharStreams.fromString("""
            while (a != b) {
                a = a + 1
            }""".trimIndent()))
        val parser = FunLangParser(BufferedTokenStream(expLexer))
        val file = parser.file()
        assertEquals("(file (block (statement " +
                "(whileLoop while ( (expression (expression a) != (expression b)) )" +
                " (blockWithBraces { " +
                "(block (statement " +
                "(assignment a = " +
                "(expression (expression a) + (expression 1))))) }))))" +
                " <EOF>)", file.toStringTree(parser))
    }
}
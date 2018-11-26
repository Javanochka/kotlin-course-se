package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.FunLangLexer
import ru.hse.spb.parser.FunLangParser
import java.io.File

fun run(text: String): Int {
    val lexer = FunLangLexer(CharStreams.fromString(text))
    val parser = FunLangParser(BufferedTokenStream(lexer))
    val file = parser.file()
    val visitor = FunLangVisitor()
    return visitor.visit(file)
}
fun process(text: String) {
    try {
        run(text)
    } catch (e: InterpreterException) {
        println(e.message)
    }
}
fun main(args: Array<String>) {
    process(File(args[0]).readText())
}
package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.FunLangLexer
import ru.hse.spb.parser.FunLangParser
import java.io.File

fun run(text : String) : Int {
    val lexer = FunLangLexer(CharStreams.fromString(text))
    val parser = FunLangParser(BufferedTokenStream(lexer))
    val file = parser.file()
    val visitor = FunLangVisitor()
    return visitor.visit(file)
}
fun interpretate(text : String) {
    val lexer = FunLangLexer(CharStreams.fromString(text))
    val parser = FunLangParser(BufferedTokenStream(lexer))
    val file = parser.file()
    val visitor = FunLangVisitor()
    try {
        visitor.visit(file)
    } catch (e : InterpreterException) {
        println(e.message)
    }
}
fun main(args: Array<String>) {
    interpretate(File(args[0]).readText())
}
package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TexDSLTest {

    @Test
    fun testCreateDocument() {
        val result = document {
            documentClass("beamer", "11pt")
            usePackage("inputenc", "utf8")
            usePackage("amsmath")
        }.toString().trimIndent()
        val expected = """
            \documentclass[11pt]{beamer}
            \usepackage[utf8]{inputenc}
            \usepackage{amsmath}
            \begin{document}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun testCreateDocumentManyParameters() {
        val result = document {
            documentClass("beamer", "11pt")
            usePackage("inputenc", "utf8")
            usePackage("geometry", "left" to "15mm", "top" to "2cm", "right" to "15mm", "bottom" to "2cm", "nohead", "footskip" to "1cm")
        }.toString().trimIndent()
        val expected = """
            \documentclass[11pt]{beamer}
            \usepackage[utf8]{inputenc}
            \usepackage[left=15mm, top=2cm, right=15mm, bottom=2cm, nohead, footskip=1cm]{geometry}
            \begin{document}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun testSimpleFrame() {
        val result = document {
            documentClass("beamer", "11pt")
            usePackage("inputenc", "utf8")
            usePackage("amsmath")
            frame("Hello", "arg1" to "arg2") {
                +"Hello world!"
            }
        }.toString().trimIndent()
        val expected = """
            \documentclass[11pt]{beamer}
            \usepackage[utf8]{inputenc}
            \usepackage{amsmath}
            \begin{document}
                \begin{frame}[arg1=arg2]
                    \frametitle{Hello}
                    Hello world!
                \end{frame}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun testEnumerate() {
        val result = document {
            enumerate {
                item {
                    +"meow"
                }
                item {
                    +"hello"
                }
            }
        }.toString().trimIndent()
        val expected = """
            \begin{document}
                \begin{enumerate}
                    \item
                        meow
                    \item
                        hello
                \end{enumerate}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun testItemize() {
        val result = document {
            itemize {
                item {
                    +"meow"
                }
                item {
                    +"hello"
                }
            }
        }.toString().trimIndent()
        val expected = """
            \begin{document}
                \begin{itemize}
                    \item
                        meow
                    \item
                        hello
                \end{itemize}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun testAlignment() {
        val result = document {
            alignment(Alignment.AlignType.CENTER) {
                math { +"a^2 + b^2 = c^2" }
            }
        }.toString().trimIndent()
        val expected = """
            \begin{document}
                \begin{center}
                    \begin{math}
                        a^2 + b^2 = c^2
                    \end{math}
                \end{center}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }

    @Test
    fun testCustomTag() {
        val result = document {
            customTag("pyglist", "language" to "kotlin") {
                +"val a = 1"
            }
        }.toString().trimIndent()
        val expected = """
            \begin{document}
                \begin{pyglist}[language=kotlin]
                    val a = 1
                \end{pyglist}
            \end{document}
        """.trimIndent()
        assertEquals(expected, result)
    }
}
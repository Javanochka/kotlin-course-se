package ru.hse.spb

import java.io.OutputStream

abstract class Element {
    abstract fun render(builder: StringBuilder, indent: String)

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        render(stringBuilder, "")
        return stringBuilder.toString()
    }
    fun toOutputStream(out: OutputStream) {
        val writer = out.writer()
        writer.use {
            it.append(toString())
        }
    }
}

class TextElement(val text: String) : Element() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}

infix fun String.to(a: String): String {
    return "$this=$a"
}

private const val TEX_INDENT = "    "

fun printParameters(list: List<String>): String {
    return if (list.isEmpty()) "" else "[${list.joinToString(", ")}]"
}

@DslMarker
annotation class TexTagMarker

@TexTagMarker
open class SingleTexTag(val name: String, val argument: String, vararg val params: String): Element() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name${printParameters(params.toList())}{$argument}\n")
    }
}

class DocumentClass(argument: String, vararg params: String): SingleTexTag("documentclass", argument, *params)

class UsePackage(argument: String, vararg params: String): SingleTexTag("usepackage", argument, *params)


@TexTagMarker
open class CustomTag(val name: String, vararg val params: String): Element() {
    protected val children = arrayListOf<Element>()

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${printParameters(params.toList())}\n")
        children.forEach { it -> it.render(builder, indent + TEX_INDENT) }
        builder.append("$indent\\end{$name}\n")
    }

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    fun math(init: Math.() -> Unit) = initTag(Math(), init)
    fun frame(frameTitle: String, vararg params: String, init: Frame.() -> Unit) = initTag(Frame(frameTitle, *params), init)
    fun itemize(vararg params: String, init: Itemize.() -> Unit) = initTag(Itemize(*params), init)
    fun enumerate(vararg params: String, init: Enumerate.() -> Unit) = initTag(Enumerate(*params), init)
    fun customTag(name: String, vararg params: String, init: CustomTag.() -> Unit) = initTag(CustomTag(name, *params), init)
    fun alignment(alignType: Alignment.AlignType, init: Alignment.() -> Unit) = initTag(Alignment(alignType), init)
}

class Document: CustomTag("document") {
    private val headerChildren = arrayListOf<Element>()

    override fun render(builder: StringBuilder, indent: String) {
        headerChildren.forEach { it -> it.render(builder, indent) }
        super.render(builder, indent)
    }

    fun documentClass(argument: String, vararg params: String) =
            headerChildren.add(DocumentClass(argument, *params))
    fun usePackage(argument: String, vararg params: String) =
            headerChildren.add(UsePackage(argument, *params))
}

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

class Frame(private val frameTitle: String, vararg params: String): CustomTag("frame", *params) {
    override fun render(builder: StringBuilder, indent: String) {
        children.add(0, SingleTexTag("frametitle", frameTitle))
        super.render(builder, indent)
    }
}

class Math: CustomTag("math")

class Item(vararg params: String): CustomTag("item", *params) {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\item${printParameters(params.toList())}\n")
        children.forEach { it -> it.render(builder, indent + TEX_INDENT) }
    }
}

open class TagWithItems(name: String, vararg params: String): CustomTag(name, *params) {
    fun item(vararg params: String, init: Item.() -> Unit) = initTag(Item(*params), init)
}

class Itemize(vararg params: String): TagWithItems("itemize", *params)
class Enumerate(vararg params: String): TagWithItems("enumerate", *params)

class Alignment(type: AlignType): CustomTag(type.alignType) {
    enum class AlignType(val alignType: String) {
        CENTER("center"),
        LEFT("flushleft"),
        RIGHT("flushright")
    }
}
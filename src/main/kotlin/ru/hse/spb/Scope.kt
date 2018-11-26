package ru.hse.spb

import ru.hse.spb.parser.FunLangParser

class Scope(val parent: Scope?) {
    val functionDict: ScopeDict<FunLangParser.FunctionContext> = ScopeDict(parent?.functionDict)
    val variableDict: ScopeDict<Int> = ScopeDict(parent?.variableDict)

    class ScopeDict<T>(private val parentDict: ScopeDict<T>?) {
        private val dict = HashMap<String, T>()

        fun get(name: String): T {
            return dict[name] ?: parentDict?.get(name) ?: throw InterpreterException("Element \'$name\' not found.")
        }

        fun add(name: String, value: T) {
            if (name in dict) {
                throw InterpreterException("Element \'$name\' is already declared in current scope.")
            }
            dict[name] = value
        }

        fun set(name: String, value: T) {
            if (name !in dict) {
                throw InterpreterException("Element \'$name\' not found.")
            }
            dict[name] = value
        }
    }
}
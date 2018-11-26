package ru.hse.spb

import ru.hse.spb.parser.FunLangBaseVisitor
import ru.hse.spb.parser.FunLangParser

class FunLangVisitor: FunLangBaseVisitor<Int>() {

    private var scope = Scope(null)
    private var returnValue: Int? = null
    private var toReturn = false

    override fun visitFile(ctx: FunLangParser.FileContext): Int {
        ctx.block().accept(this)
        return returnValue ?: 0
    }

    override fun visitBlock(ctx: FunLangParser.BlockContext): Int? {
        for (statement in ctx.statement()) {
            statement.accept(this)
            if (toReturn) {
                return null
            }
        }
        return null
    }

    override fun visitBlockWithBraces(ctx: FunLangParser.BlockWithBracesContext): Int? {
        return ctx.body.accept(this)
    }

    override fun visitStatement(ctx: FunLangParser.StatementContext): Int? {
        return ctx.getChild(0).accept(this)
    }

    override fun visitFunction(ctx: FunLangParser.FunctionContext): Int? {
        scope.functionDict.add(ctx.IDENTIFIER().text, ctx)
        return null
    }

    override fun visitVariable(ctx: FunLangParser.VariableContext): Int? {
        scope.variableDict.add(ctx.IDENTIFIER().text, ctx.expression().accept(this))
        return null
    }

    override fun visitExpression(ctx: FunLangParser.ExpressionContext): Int {
        return when {
            ctx.IDENTIFIER() != null -> scope.variableDict.get(ctx.IDENTIFIER().text)
            ctx.LITERAL() != null -> Integer.parseInt(ctx.LITERAL().text)
            ctx.inBraces != null -> ctx.inBraces.accept(this)
            ctx.left == null -> ctx.getChild(0).accept(this)
            ctx.operator.text != null -> {
                val leftValue = ctx.left.accept(this)
                val rightValue = ctx.right.accept(this)
                when (ctx.operator.text) {
                    "*" -> leftValue * rightValue
                    "/" -> leftValue / rightValue
                    "%" -> leftValue % rightValue
                    "+" -> leftValue + rightValue
                    "-" -> leftValue - rightValue
                    ">" -> booleanToInt(leftValue > rightValue)
                    "<" -> booleanToInt(leftValue < rightValue)
                    ">=" -> booleanToInt(leftValue >= rightValue)
                    "<=" -> booleanToInt(leftValue <= rightValue)
                    "==" -> booleanToInt(leftValue == rightValue)
                    "!=" -> booleanToInt(leftValue != rightValue)
                    "&&" -> booleanToInt(leftValue > 0 && rightValue > 0)
                    "||" -> booleanToInt(leftValue > 0 || rightValue > 0)
                    else -> throw InterpreterException("Didn't manage to parse expression")
                }
            }
            else -> throw InterpreterException("Didn't manage to parse expression")
        }

    }

    override fun visitFunctionCall(ctx: FunLangParser.FunctionCallContext): Int {
        val arguments = ctx.arguments().expression().mapNotNull { expression ->  expression.accept(this)}
        val functionName = ctx.IDENTIFIER().toString()
        if (functionName == "println") {
            callPrintln(arguments)
            return 0
        }
        val function = scope.functionDict.get(functionName)
        val parameterNames = function.parameterNames().IDENTIFIER().mapNotNull { name -> name.toString() }
        if (arguments.size != parameterNames.size) {
            throw InterpreterException("Wrong amount of arguments in function call")
        }
        scope = Scope(scope)
        for (i in 0 until arguments.size) {
            scope.variableDict.add(parameterNames[i], arguments[i])
        }
        function.blockWithBraces().body.accept(this)
        scope = scope.parent ?: Scope(null)
        val result = returnValue
        returnValue = null
        toReturn = false
        return result ?: 0
    }

    override fun visitWhileLoop(ctx: FunLangParser.WhileLoopContext): Int? {
        var condition = ctx.condition.accept(this)
        while (condition != 0) {
            ctx.blockWithBraces().body.accept(this)
            if (toReturn) {
                return null
            }
            condition = ctx.condition.accept(this)
        }
        return null
    }

    override fun visitIfStatement(ctx: FunLangParser.IfStatementContext): Int? {
        val condition = ctx.condition.accept(this)
        if (condition != 0) {
            ctx.ifBody.body.accept(this)
        } else if (ctx.elseBody != null){
            ctx.elseBody.body.accept(this)
        }
        return null
    }

    override fun visitAssignment(ctx: FunLangParser.AssignmentContext): Int? {
        scope.variableDict.set(ctx.IDENTIFIER().text, ctx.expression().accept(this))
        return null
    }

    override fun visitReturnStatement(ctx: FunLangParser.ReturnStatementContext): Int? {
        returnValue = ctx.expression().accept(this)
        toReturn = true
        return null
    }

    private fun callPrintln(arguments : List<Int>) {
        println(arguments.joinToString(" "))
    }

    private fun booleanToInt(b : Boolean) : Int {
        return if (b) 1 else 0
    }
}


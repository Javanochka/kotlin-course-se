package ru.hse.spb

import java.util.*

class Input(val numberOfRooms: Int, val cost: IntArray, val nextRoom: IntArray)

fun readInput(scanner: Scanner): Input {
    val numberOfRooms = scanner.nextInt()
    val cost = IntArray(numberOfRooms, {scanner.nextInt()})
    val nextRoom = IntArray(numberOfRooms, {scanner.nextInt() - 1})
    return Input(numberOfRooms, cost, nextRoom)
}

class Solution(private val input: Input) {

    private enum class Color {
        WHITE, GREY, BLACK
    }

    private val colors = Array(input.numberOfRooms, { Color.WHITE })
    private val stack = mutableListOf<Int>()

    private fun calculateCostOfReachableTraps(currentRoom: Int): Int {
        colors[currentRoom] = Color.GREY
        stack.add(currentRoom)
        val followingRoom = input.nextRoom[currentRoom]
        val answer = when (colors[followingRoom]) {
            Color.GREY -> {
                var i = stack.size - 1

                var bestTrap = followingRoom
                while (stack[i] != followingRoom) {
                    if (input.cost[bestTrap] > input.cost[stack[i]]) {
                        bestTrap = stack[i]
                    }
                    i--
                }
                input.cost[bestTrap]
            }
            Color.WHITE -> calculateCostOfReachableTraps(followingRoom)
            Color.BLACK -> 0
        }
        colors[currentRoom] = Color.BLACK
        stack.removeAt(stack.size - 1)
        return answer
    }

    private var gotAnswer = false

    fun getAnswer(): Int {
        if (gotAnswer) {
            throw GetAnswerCalledAgainException()
        }
        gotAnswer = true
        var answer = 0

        for (i in 0 until input.numberOfRooms) {
            if (colors[i] == Color.WHITE) {
                answer += calculateCostOfReachableTraps(i)
            }
        }
        return answer
    }

    class GetAnswerCalledAgainException : Exception("Function getAnswer should be called once")
}

fun main(args: Array<String>) {
    val input = readInput(Scanner(System.`in`))
    println(Solution(input).getAnswer())
}
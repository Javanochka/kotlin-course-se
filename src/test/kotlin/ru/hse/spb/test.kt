package ru.hse.spb

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TestSource {
    @Test
    fun testReadInputSample1() {
        val sample = "5\n" +
                "1 2 3 2 10\n" +
                "1 3 4 3 3"
        val input = readInput(Scanner(sample))
        assertEquals(5, input.numberOfRooms)
        assertArrayEquals(intArrayOf(1, 2, 3, 2, 10), input.cost)
        assertArrayEquals(intArrayOf(0, 2, 3, 2, 2), input.nextRoom)
    }

    @Test
    fun testReadInputSample2() {
        val sample = "4\n" +
                "1 10 2 10\n" +
                "2 4 2 2\n"
        val input = readInput(Scanner(sample))
        assertEquals(4, input.numberOfRooms)
        assertArrayEquals(intArrayOf(1, 10, 2, 10), input.cost)
        assertArrayEquals(intArrayOf(1, 3, 1, 1), input.nextRoom)
    }

    @Test
    fun testReadInputSample3() {
        val sample = "7\n" +
                "1 1 1 1 1 1 1\n" +
                "2 2 2 3 6 7 6"
        val input = readInput(Scanner(sample))
        assertEquals(7, input.numberOfRooms)
        assertArrayEquals(intArrayOf(1, 1, 1, 1, 1, 1, 1), input.cost)
        assertArrayEquals(intArrayOf(1, 1, 1, 2, 5, 6, 5), input.nextRoom)
    }

    @Test
    fun testGetAnswerSample1() {
        val input = Input(5,
                intArrayOf(1, 2, 3, 2, 10),
                intArrayOf(0, 2, 3, 2, 2))
        assertEquals(3, Solution(input).getAnswer())
    }

    @Test
    fun testGetAnswerSample2() {
        val input = Input(4,
                intArrayOf(1, 10, 2, 10),
                intArrayOf(1, 3, 1, 1))
        assertEquals(10, Solution(input).getAnswer())
    }

    @Test
    fun testGetAnswerSample3() {
        val input = Input(7,
                intArrayOf(1, 1, 1, 1, 1, 1, 1),
                intArrayOf(1, 1, 1, 2, 5, 6, 5))
        assertEquals(2, Solution(input).getAnswer())
    }

    @Test(expected = Solution.GetAnswerCalledAgainException::class)
    fun testGetAnswerCalledTwice() {
        val input = Input(1,
                intArrayOf(1),
                intArrayOf(0))
        val solution = Solution(input)
        assertEquals(1, solution.getAnswer())
        solution.getAnswer()
    }
}
package minesweeper

import java.util.*
import kotlin.system.exitProcess

val s = Scanner(System.`in`)
const val dim = 9

enum class Directions {
    N, NE, E, SE, S, SW, W, MW
}

fun main() {
    println("The dimension of the board is 9.")
    print("How many mines do you want on the field? ")
    val bombs = s.nextInt()

    val minesweeper = Game(dim)
    minesweeper.printBoard()

    loop@do {
        println("Set/unset mines marks(mine) or claim(free) a cell(row, column):\n" +
                "i.e. > 1 3 free  OR  2 5 mine")
        val row = s.nextInt() - 1
        val column = s.nextInt() - 1
        val option = s.next()
        if (!minesweeper.hasInitialized) {
            minesweeper.initialize(bombs, row, column)
            minesweeper.hasInitialized = true
        }
        val cell = minesweeper.board[row][column].value
        val marked = minesweeper.board[row][column].marked
        when (option) {
            "free" -> { // The player makes the assumption that the cell is free
                when (cell) {
                    0 -> minesweeper.revealCells(row, column) // Reveal all surrounding cells that are not bombs
                    in 1..8 -> minesweeper.revealCell(row, column) // Free only that cell
                    else -> { // Trigger game lost, also print all bombs on current board
                        minesweeper.lostGame()
                        println("You stepped on a mine and failed!")
                        exitProcess(1)
                    }
                }
            }
            "mine" -> { // The player marks a cell
                minesweeper.board[row][column].marked = marked != true
            }
            else -> {
                println("Invalid option.")
                continue@loop
            }
        }
        minesweeper.printBoard()
    } while (!minesweeper.checkWin())
    println("Congratulations! You found all the mines!")
}

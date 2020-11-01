package minesweeper

import java.util.*
import kotlin.system.exitProcess

val s = Scanner(System.`in`)

enum class Directions {
    N, NE, E, SE, S, SW, W, MW
}

fun main() {
    print("How many mines do you want on the field? ")
    val bombs = s.nextInt()

    val minesweeper = Game(9)
    minesweeper.printBoard()

    loop@do {
        println("Set/unset mines marks or claim a cell as free: ")
        val y = s.nextInt() - 1
        val x = s.nextInt() - 1
        val option = s.next()
        if (!minesweeper.hasInitialized) {
            minesweeper.initialize(bombs, x, y)
            minesweeper.hasInitialized = true
        }
        val cell = minesweeper.board[x][y].value
        val marked = minesweeper.board[x][y].marked
        when (option) {
            "free" -> { // The player makes the assumption that the cell is free
                when (cell) {
                    0 -> minesweeper.revealCells(x, y) // Reveal all surrounding cells that are not bombs
                    in 1..8 -> minesweeper.revealCell(x, y) // Free only that cell
                    else -> { // Trigger game lost, also print all bombs on current board
                        minesweeper.lostGame()
                        println("You stepped on a mine and failed!")
                        exitProcess(1)
                    }
                }
            }
            "mine" -> { // The player marks a cell
                minesweeper.board[x][y].marked = marked != true
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

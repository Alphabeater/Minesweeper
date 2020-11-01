package minesweeper

/** Game class
 * Board cells values can be:
 * 0: Empty
 * 1-8: Number of bombs near cell
 * 9: Bomb
 *
 * Aside from that, the cell can be marked (shown as an asterisk)
 * or being visible (actually printing out its value).
 */

class Game(private val dim: Int) {
    var board = Array(dim) { Array(dim) { Cell(0, false, false) } }
    private var bombs = 0

    class Cell(var value: Int, var marked: Boolean, var visible: Boolean)

    var hasInitialized = false

    fun initialize(bombs: Int, row: Int, column: Int) {
        this.bombs = bombs
        fillBoard(row, column)
        checkBoard()
    }

    private fun fillBoard(row: Int, column: Int) {
        var bombsDeployed = bombs
        while (bombsDeployed > 0) {
            val r = (0..8).random()
            val c = (0..8).random()
            if (r == row && c == column) continue
            if (board[r][c].value == 0) {
                board[r][c].value = 9
                bombsDeployed--
            } else continue
        }
    }

    private fun checkBoard() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].value == 9) {
                    tryAdd(i + 1, j) // S
                    tryAdd(i, j + 1) // E
                    tryAdd(i + 1, j + 1) // SE
                    tryAdd(i - 1, j) // N
                    tryAdd(i, j - 1) // W
                    tryAdd(i - 1, j - 1) // NW
                    tryAdd(i + 1, j - 1) // SW
                    tryAdd(i - 1, j + 1) // NE
                }
            }
        }
    }

    private fun tryAdd(i: Int, j: Int) {
        try {
            if (board[i][j].value != 9) board[i][j].value += 1
        } catch (e: Exception) {
            return
        }
    }

    fun printBoard() {
        println("\n │1 2 3 4 5 6 7 8 9 │\n—│——————————————————│")
        for (i in board.indices) {
            print("${i + 1}│")
            for (j in board[i].indices) {
                print(
                    "${if (board[i][j].marked) '*'
                        else if (!board[i][j].visible) '.'
                        else {
                            when (board[i][j].value) {
                                0 -> '/'
                                9 -> 'X'
                                else -> board[i][j].value
                            }
                        }}" + ' '
                )
            }
            println("│")
        }
        println("—│——————————————————│")
    }

    fun revealCell(row: Int, column: Int) {
        board[row][column].visible = true
    }

    fun revealCells(row: Int, column: Int) {
        board[row][column].visible = true
        recursionReveal(row - 1, column)
        recursionReveal(row, column + 1)
        recursionReveal(row + 1, column)
        recursionReveal(row, column - 1)
        recursionReveal(row - 1, column + 1)
        recursionReveal(row + 1, column + 1)
        recursionReveal(row + 1, column - 1)
        recursionReveal(row - 1, column - 1)
    }

    private fun recursionReveal(row: Int, column: Int) {
        try {
            if (board[row][column].value == 9 || board[row][column].visible) return
            else if (board[row][column].marked && viewNeighbors(row, column)) board[row][column].marked = false
            else board[row][column].visible = true
        } catch (e: Exception) {
            return
        }
        if (board[row][column].value == 0)
            for (value in Directions.values()) {
                recursionReveal(row - 1, column)
                recursionReveal(row, column + 1)
                recursionReveal(row + 1, column)
                recursionReveal(row, column - 1)
                recursionReveal(row - 1, column + 1)
                recursionReveal(row + 1, column + 1)
                recursionReveal(row + 1, column - 1)
                recursionReveal(row - 1, column - 1)
            }
    }

    private fun viewNeighbors(row: Int, column: Int): Boolean {
        return viewNeighbor(row + 1, column) ||
            viewNeighbor(row, column + 1) ||
            viewNeighbor(row + 1, column + 1) ||
            viewNeighbor(row - 1, column) ||
            viewNeighbor(row, column - 1) ||
            viewNeighbor(row - 1, column - 1) ||
            viewNeighbor(row + 1, column - 1) ||
            viewNeighbor(row - 1, column + 1)
    }

    private fun viewNeighbor(row: Int, column: Int): Boolean {
        return try {
            board[row][column].value == 0
        } catch (e: Exception) {
            false
        }
    }

    fun lostGame() {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].value == 9) board[i][j].visible = true
            }
        }
        printBoard()
    }

    fun checkWin(): Boolean {
        for (i in board.indices) {
            for (j in board[i].indices) {
                if (board[i][j].value == 9 && !board[i][j].marked) {
                    return false
                } else if (board[i][j].value in 1..8 && !board[i][j].visible){
                    return false
                }
            }
        }
        return true
    }
}
package com.example.tictaktoe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var player = Utils.X_TURN
    var winner: Char = Utils.NO_WINNER
    private var boardList = MutableList(9){Utils.FREE_CELL_VALUE}

    private var boardListLiveData = MutableLiveData<List<Int>>()
    private var isGameOverMutable = MutableLiveData<Boolean>()

    fun getBoardList():List<Int> {
        boardListLiveData.value = boardList
        return boardListLiveData.value!!
    }

    fun boardList(): LiveData<List<Int>> {
        return boardListLiveData
    }

    fun setBoardCell(position: Int) {

        if (checkIfAlreadyFilled(position)) {return } // Already filled by x / o OR game over
        if (player == Utils.X_TURN) {
            boardList[position] = Utils.X_VALUE
            player = Utils.O_TURN
        }else {
            boardList[position] = Utils.O_VALUE
            player = Utils.X_TURN;
        }
        boardListLiveData.value = boardList
        if(checkIsBoardFilled()) isGameOverMutable.value = true
        else {
            if (checkWin()) {
                // we toggle the player cuz when setting value on board we set next player turn
                winner = if (player == Utils.X_TURN) {
                    Utils.O_TURN
                } else {
                    Utils.X_TURN
                }
                isGameOverMutable.value = true
            }
        }
    }

    fun isGameOver():LiveData<Boolean> {
        return isGameOverMutable
    }

    fun resetGame() {
        winner= Utils.NO_WINNER
        player = Utils.X_TURN
        boardList = MutableList(9){Utils.FREE_CELL_VALUE}
        boardListLiveData.value = boardList
        isGameOverMutable.value = false
    }
    private fun checkIfAlreadyFilled(position: Int): Boolean {
        return boardList[position] != 0
    }

    private fun checkIsBoardFilled():Boolean {
        boardList.forEach { value -> if(value == 0) return false  }
        return true
    }

    private fun checkWin() : Boolean{
        return checkRow() ||  checkCol() || checkDiagonal()
    }

    private fun checkRow(): Boolean {
        return checkNoFreeCellInList(0,1,2) && boardList[0] == boardList[1] && boardList[1] == boardList[2] || // 1th row
                checkNoFreeCellInList(3,4,5) && boardList[3] == boardList[4] && boardList[4] == boardList[5] || // 2nd row
                checkNoFreeCellInList(6,7,8) && boardList[6] == boardList[7] && boardList[7] == boardList[8]    // 3rd row
    }

    private fun checkCol(): Boolean {
        return checkNoFreeCellInList(0,3,6) && boardList[0] == boardList[3] && boardList[3] == boardList[6] || // 1st col
                checkNoFreeCellInList(1,4,7) && boardList[1] == boardList[4] && boardList[4] == boardList[7] || // 2nd col
                checkNoFreeCellInList(2,5,8) && boardList[2] == boardList[5] && boardList[5] == boardList[8]    // 3rd col
    }

    private fun checkDiagonal(): Boolean {
        return checkNoFreeCellInList(0,4,8) && boardList[0] == boardList[4] && boardList[4] == boardList[8] ||  // left top to right bottom
                checkNoFreeCellInList(2,4,6) && boardList[2] == boardList[4] && boardList[4] == boardList[6]   // right top to left bottom
    }

    // Checks whether that following position is filled with x or o and returns true if filled else false
    private fun checkNoFreeCellInList(p1: Int, p2: Int, p3: Int): Boolean {
        return boardList[p1] != Utils.FREE_CELL_VALUE &&
                boardList[p2] != Utils.FREE_CELL_VALUE &&
                boardList[p3] != Utils.FREE_CELL_VALUE
    }

}
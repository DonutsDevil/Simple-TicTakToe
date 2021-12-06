package com.example.tictaktoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), BoardAdapter.OnBoardClickListener, View.OnClickListener {
    //UI
    private lateinit var mRvBoard: RecyclerView
    private lateinit var mBoardAdapter: BoardAdapter
    private lateinit var mViewModel: MainViewModel
    private lateinit var mRestartBtn: Button
    private lateinit var mGameStatusTv: TextView

    //VAR
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews()
        initBoard()
        mViewModel.boardList().observe(this, Observer {
            mBoardAdapter.setNewBoardList(it)
        })

        mViewModel.isGameOver().observe(this, Observer { isGameOver ->
            this.isGameOver = isGameOver
            if (isGameOver) {
                if (mViewModel.winner != Utils.NO_WINNER) {
                    mGameStatusTv.text = "${mViewModel.winner} is the winner, click on restart to play again"
                }else {
                    mGameStatusTv.text = getString(R.string.gameOver)
                }
                Toast.makeText(this,"Game over",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initViews() {
        mRestartBtn = findViewById(R.id.btn_Restart)
        mGameStatusTv = findViewById(R.id.tv_gameStatus)
        mRvBoard = findViewById(R.id.rv_board)
        mRestartBtn.setOnClickListener(this)
    }

    private fun initBoard() {
        val gridLayout = GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false)
        mRvBoard.layoutManager = gridLayout
        mBoardAdapter = BoardAdapter(mViewModel.getBoardList(),this,this)
        mRvBoard.adapter = mBoardAdapter
    }

    override fun onBoardCellClicked(position: Int) {
        if (isGameOver.not()) mViewModel.setBoardCell(position)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_Restart) {
            mGameStatusTv.text = null
            mViewModel.resetGame()
        }
    }
}
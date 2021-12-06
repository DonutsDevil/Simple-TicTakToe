package com.example.tictaktoe

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView

class BoardAdapter(private var mList: List<Int>, private val mContext: Context, val mListener: OnBoardClickListener):
    RecyclerView.Adapter<BoardAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view:View = LayoutInflater.from(mContext).inflate(R.layout.rv_list_item_board_cell,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setBoardCellImage(getIcon(mList.get(position)))
        holder.boardCell.setOnClickListener(
            View.OnClickListener { mListener.onBoardCellClicked(position) }
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setNewBoardList(boardList: List<Int>) {
        mList = boardList
        notifyDataSetChanged()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val boardCell:ImageView = itemView.findViewById(R.id.board_cell)
        fun setBoardCellImage(cellImage: Drawable) {
            boardCell.setImageDrawable(cellImage)
        }
    }

    interface OnBoardClickListener {
        fun onBoardCellClicked(position: Int)
    }

    private fun getIcon(value: Int) : Drawable {
       return when(value) {
           Utils.O_VALUE -> AppCompatResources.getDrawable(mContext, R.drawable.ic_o)!!
           Utils.X_VALUE -> AppCompatResources.getDrawable(mContext, R.drawable.ic_x)!!
           else -> AppCompatResources.getDrawable(mContext, R.drawable.ic_none)!!
        }
    }
}
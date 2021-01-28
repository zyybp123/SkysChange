package com.bpzzr.skyschange

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bpzzr.commonlibrary.file.BaseFileEntity
import java.util.ArrayList

class SimpleTextAdapter<T>(var mDataList: ArrayList<T>?) : RecyclerView.Adapter<SimpleTextAdapter.TextHolder>() {

    class TextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val tv = TextView(parent.context)
        tv.setTextColor(Color.BLACK)
        return TextHolder(tv)
    }

    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        if (holder.itemView is TextView) {
            holder.itemView.text = mDataList?.get(position).toString()
        }
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }
}
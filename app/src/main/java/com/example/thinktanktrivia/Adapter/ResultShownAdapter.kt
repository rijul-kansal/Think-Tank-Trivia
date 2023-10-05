package com.example.thinktanktrivia.Adapter

import com.example.thinktanktrivia.Model.Res


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.thinktanktrivia.databinding.DisplayResRvBinding

class ResultShownAdapter(
    private val items: ArrayList<Res>
) :
    RecyclerView.Adapter<ResultShownAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
           DisplayResRvBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.pos.text=(position+1).toString()+')'
        holder.cat.text=item.category
        holder.corr.text="Correct Q. "+item.corrQ.toString()
        holder.total.text="Total Q. "+item.totalQ.toString()
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item )
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    fun setOnClickListener(onClickListener: ResultShownAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, model: Res)
    }
    class ViewHolder(binding: DisplayResRvBinding) : RecyclerView.ViewHolder(binding.root) {
        var pos=binding.posRv
        var cat=binding.catRV
        var corr=binding.corrRV
        val total=binding.totalRV
    }
}
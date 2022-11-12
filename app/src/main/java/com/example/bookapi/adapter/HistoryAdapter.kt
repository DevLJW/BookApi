package com.example.bookapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter


import androidx.recyclerview.widget.RecyclerView
import com.example.bookapi.Model.History
import com.example.bookapi.databinding.ItemHistoryBinding

class HistoryAdapter(val historyDeleteClickListener : (String) -> Unit) : ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil) {


    //.ViewHolder == 미리만들어진 뷰 (홀딩해놈)(2)
    inner class HistoryItemViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) { // 생성자 실행 itemView 상속받기 View 객체타입

        fun bind(historyModel: History) {

            binding.usersearchtext.text = historyModel.keyword
            binding.deletebtn.setOnClickListener {

                historyDeleteClickListener(historyModel.keyword.orEmpty())
            }


        }

    }


    //미리 만들어진 뷰 홀더가 없을경우에, 새롭게 생성(1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {

        return HistoryItemViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }

    //데이터 바인드 함수(3)
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {

        holder.bind(currentList[position]) //?


    }



    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<History>() {

            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {

                return oldItem == newItem


            }


            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean { //콘텐츠가 같냐
                return oldItem.keyword == newItem.keyword
            }

        }


    }
}



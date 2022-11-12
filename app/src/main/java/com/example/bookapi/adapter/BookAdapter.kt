package com.example.bookapi.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookapi.Model.Book
import com.example.bookapi.databinding.ItemBookBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_book.view.*

class BookAdapter : ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) { //상속받을 어댑터 클래스의 제네릭 타입, VH를 구현 그 후, 생성자 diffUtil 구현



    //.ViewHolder == 미리만들어진 뷰 (홀딩해놈)(2)
    inner class BookItemViewHolder(private val binding: ItemBookBinding): RecyclerView.ViewHolder(binding.root){ // 생성자 실행 itemView 상속받기 View 객체타입

        fun bind(bookModel: Book){

            binding.title.text = bookModel.title //bookmodel의 title값 가져오기
            binding.content.text = bookModel.description
            Glide.with(binding.MainImage.context).load(bookModel.coverSmallUrl).into(binding.MainImage)


        }

    }




    //미리 만들어진 뷰 홀더가 없을경우에, 새롭게 생성(1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))




    }

    //데이터 바인드 함수(3)
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {

        holder.bind(currentList[position]) //?


    }


    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Book>(){

            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {

                        return oldItem == newItem


            }


            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean { //콘텐츠가 같냐
                 return oldItem.id == newItem.id
            }

        }


    }




}
package com.app.dolt.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.dolt.R
import com.app.dolt.databinding.ItemSearchViewBinding

class SearchView(
    context: Context
) : LinearLayout(context) {

    lateinit var binding: ItemSearchViewBinding

    private var onClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_search_view, this, true)
        binding = ItemSearchViewBinding.inflate(LayoutInflater.from(context), this, true)
        Log.i("HEIGHT::::::::::::",binding.main.height.toString())

        setOnClickListener {
            onClickListener?.invoke()
        }

    }

    /*fun setOnPostClickListener(listener: () -> Unit) {
        this.onClickListener = listener
    }

    fun setName(name: String) {
        binding.nameText.text = name
    }

    fun setDetail(detail: String) {
        binding.detailText.text = detail
    }*/
}

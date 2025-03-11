package com.app.dolt.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.dolt.R
import com.app.dolt.databinding.ItemPostViewBinding

class PostView(
    context: Context
) : LinearLayout(context) {

    lateinit var binding: ItemPostViewBinding

    private var onClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_post_view, this, true)
        binding = ItemPostViewBinding.inflate(LayoutInflater.from(context), this, true)

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

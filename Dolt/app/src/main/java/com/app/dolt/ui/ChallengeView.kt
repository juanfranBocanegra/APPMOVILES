package com.app.dolt.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.app.dolt.R
import com.app.dolt.databinding.ActivityMainBinding
import com.app.dolt.databinding.ItemChallengeViewBinding

class ChallengeView(
    context: Context
) : LinearLayout(context) {

    lateinit var binding: ItemChallengeViewBinding

    private var onClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_challenge_view, this, true)
        binding = ItemChallengeViewBinding.inflate(LayoutInflater.from(context), this, true)

        setOnClickListener {
            onClickListener?.invoke()
        }

    }

    fun setOnChallengeClickListener(listener: () -> Unit) {
        this.onClickListener = listener
    }

    fun setName(name: String) {
        binding.nameText.text = name
    }

    fun setDetail(detail: String) {
        binding.detailText.text = detail
    }
}

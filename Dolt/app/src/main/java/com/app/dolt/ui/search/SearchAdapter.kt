package com.app.dolt.ui.search

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.dolt.databinding.ItemSearchViewBinding
import com.app.dolt.model.UserSimple
import java.util.Locale

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val items = mutableListOf<UserSimple>()
    private val width = 200

    // Listener para manejar clics en los elementos
    private var onItemClickListener: ((position: Int) -> Unit)? = null

    // Método para establecer el listener desde fuera
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        this.onItemClickListener = listener
    }

    init {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSearch(newUsers: List<UserSimple>) {
        items.clear()
        items.addAll(newUsers)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    class SearchViewHolder(val binding: ItemSearchViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val size = getItemCount()
        val user = items[position]


            holder.binding.name.text = user.name
            val spannableString = SpannableString("@"+user.username)
            spannableString.setSpan(UnderlineSpan(), 0, user.username.length+1, 0)
            holder.binding.user.text = spannableString


        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }

    }

    override fun getItemCount() = items.size
}
package com.app.dolt.ui.post

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.dolt.databinding.ItemPostViewBinding
import com.app.dolt.model.Post
import java.util.Locale

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val items = mutableListOf<Post>()
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
    fun updatePosts(newPosts: List<Post>) {
        items.clear()
        items.addAll(newPosts)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    class PostViewHolder(val binding: ItemPostViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentLanguage = Locale.getDefault().language
        val post = items[position]
        if (currentLanguage == "es") {

                post.challenge = post.challenge_es

        }

            holder.binding.postUserName.text = post.name_user
            val spannableString = SpannableString("@"+post.user)
            spannableString.setSpan(UnderlineSpan(), 0, post.user.length+1, 0)
            holder.binding.postUser.text = spannableString
            holder.binding.PostText.text = post.text
            holder.binding.PostChallenge.text = post.challenge
            holder.binding.PostDate.text = post.date




        holder.binding.postUser.setOnClickListener {
            onItemClickListener?.invoke(position)
        }

        holder.binding.postUserName.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount() = items.size
}
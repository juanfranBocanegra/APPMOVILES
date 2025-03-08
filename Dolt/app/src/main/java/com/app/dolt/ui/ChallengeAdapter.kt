package com.app.dolt.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.dolt.databinding.ItemChallengeViewBinding
import com.app.dolt.model.Challenge

class ChallengeAdapter : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private val items = mutableListOf<Challenge>()
    private val width = 200

    // Listener para manejar clics en los elementos
    private var onItemClickListener: ((position: Int) -> Unit)? = null

    // Método para establecer el listener desde fuera
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        this.onItemClickListener = listener
    }

    init {

    }

    fun updateChallenges(newChallenges: List<Challenge>) {
        items.clear()
        items.addAll(newChallenges)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    class ChallengeViewHolder(val binding: ItemChallengeViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChallengeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = items[position]
        holder.binding.nameText.text = challenge.name
        holder.binding.detailText.text = challenge.detail


        // Configurar el listener para cada elemento
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    override fun getItemCount() = items.size
}
package com.app.dolt.ui.challenge

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.dolt.databinding.ItemChallengeViewBinding
import com.app.dolt.model.Challenge
import java.util.Locale

/**
 * Adaptador para mostrar la lista de desafíos en un [RecyclerView].
 */
class ChallengeAdapter : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private val items = mutableListOf<Challenge>()
    private val width = 200

    // Listener para manejar clics en los elementos
    private var onItemClickListener: ((position: Int) -> Unit)? = null

    /**
     * Establece un listener para manejar el clic sobre un elemento de la la lista.
     * 
     * @param listener : Función que se ejecuta cuando se hace clic en un elemento. 
     */
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        this.onItemClickListener = listener
    }

    /**
     * Actualiza la lista de desafíos.
     * 
     * @param newChallenges : Nueva lista de desafíos.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateChallenges(newChallenges: List<Challenge>) {
        items.clear()
        items.addAll(newChallenges)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    /**
     * ViewHolder que representa cada elemento de la lista de desafíos.
     * 
     * @property binding : Enlace a la vista del elemento.
     */
    class ChallengeViewHolder(val binding: ItemChallengeViewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Crea y devuelve un nuevo [ChallengeViewHolder].
     * 
     * @param parent : Vista padre.
     * @param viewType : Tipo de vista (no utilizada en este caso).
     * @return Nueva instancia de [ChallengeViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val binding = ItemChallengeViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChallengeViewHolder(binding)
    }

    /**
     * Asocia los datos de un desafío a la vista correspondiente.
     * Además, adapta el idioma según la configuración del dispositivo.
     * 
     * @param holder : ViewHolder que representa el elemento.
     * @param position : Posición del elemento en la vista.
     */
    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val currentLanguage = Locale.getDefault().language
        val challenge = items[position]

        // Ajusta el idioma del desafío según la configuración del dispositivo
        if (currentLanguage == "es") {
            challenge.name = challenge.name_es
            challenge.detail = challenge.detail_es
        }

        holder.binding.nameText.text = challenge.name
        holder.binding.detailText.text = challenge.detail

        // Configurar el listener para el clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    /**
     * Devuleve el número total de elementos en la lista.
     * 
     * @return Número de elementos.
     */
    override fun getItemCount() = items.size
}
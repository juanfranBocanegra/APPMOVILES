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
import com.bumptech.glide.Glide
import java.util.Locale


/**
 * Adaptador para mostrar la lista de resultados de búsqueda de usuarios en un [RecyclerView].
 */
class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val items = mutableListOf<UserSimple>()
    private val width = 200

    // Listener para manejar clics en los elementos
    private var onItemClickListener: ((position: Int) -> Unit)? = null

    /**
     * Establece un listener para manejar el clic sobre un elemento de la lista.
     *
     * @param listener : Función que se ejecuta al hacer clic en un elemento.
     */
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        this.onItemClickListener = listener
    }

    /**
     * Actualiza la lista de usuarios con nuevos resultados de búsqueda.
     *
     * @param newUsers : Nueva lista de usuarios.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateSearch(newUsers: List<UserSimple>) {
        items.clear()
        items.addAll(newUsers)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    /**
     * ViewHolder que representa cada elemento de la lista de búsqueda.
     *
     * @property binding : Enlace a la vista del elemento.
     */
    class SearchViewHolder(val binding: ItemSearchViewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Crea y devuelve un nuevo [SearchViewHolder].
     *
     * @param parent : Vista padre.
     * @param viewType : Tipo de vista (no utilizado en este caso).
     * @return Nueva instancia de [SearchViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    /**
     * Asocia los datos de un usuario a la vista correspondiente.
     *
     * @param holder : ViewHolder que representa el elemento.
     * @param position : Posición del elemento en la lista.
     */
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val user = items[position]

        // Cargar la imagen
        holder.binding.profileImage.apply {
            post {
            val size = width
            layoutParams.height = size
            requestLayout()

            Glide.with(context)
                .load(user.getProfileImageUrl())
                .override(size, size)
                .centerCrop()
                .into(this)
            }
        }

        // Mostrar nombre y usuario
        holder.binding.name.text = user.name
        val spannableString = SpannableString("@"+user.username)
        spannableString.setSpan(UnderlineSpan(), 0, user.username.length+1, 0)
        holder.binding.user.text = spannableString

        // Listener de clic en el elemento
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    /**
     * Devuelve el número total de elementos en la lista.
     *
     * @return Número de elementos.
     */
    override fun getItemCount() = items.size
}
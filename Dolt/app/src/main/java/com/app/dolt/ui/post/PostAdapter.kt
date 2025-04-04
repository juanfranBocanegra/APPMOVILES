package com.app.dolt.ui.post

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.dolt.databinding.ItemPostViewBinding
import com.app.dolt.model.Post
import com.app.dolt.utils.DateFormatter
import com.bumptech.glide.Glide
import java.util.Locale


/**
 * Adaptador para mostrar la lista de publicaciones en un [RecyclerView].
 */
class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val items = mutableListOf<Post>()
    private val width = 200

    // Listener para manejar clics en los elementos
    private var onItemClickListener: ((position: Int) -> Unit)? = null

    /**
     * Establece un listener para manejar el clic sobre un elemento de la lista.
     *
     * @param listener : Función que se ejecuta cuando se hace clic en un elemento.
     */
    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        this.onItemClickListener = listener
    }

    /**
     * Actualiza la lista de publicaciones con nuevos datos.
     *
     * @param newPosts : Nueva lista de publicaciones.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updatePosts(newPosts: List<Post>) {
        items.clear()
        items.addAll(newPosts)
        notifyDataSetChanged() // Notifica al RecyclerView que los datos han cambiado
    }

    /**
     * ViewHolder que representa cada elemento de la lista de publicaciones.
     *
     * @property binding : Enlace a la vista del elemento.
     */
    class PostViewHolder(val binding: ItemPostViewBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Crea y devuelve un nuevo [PostViewHolder].
     *
     * @param parent : Vista padre.
     * @param viewType : Tipo de vista (no utilizado en este caso).
     * @return Nueva instancia de [PostViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    /**
     * Asocia los datos de una publicación a la vista correspondiente.
     * Además, adapta el idioma y carga la imagen de perfil.
     *
     * @param holder :ViewHolder que representa el elemento.
     * @param position : Posición del elemento en la lista.
     */
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentLanguage = Locale.getDefault().language
        val post = items[position]

        // Ajusta el idioma del desafío según la configuración del dispositivo
        if (currentLanguage == "es") {
                post.challenge = post.challenge_es
        }

        // Carga la imagen de perfil
        holder.binding.profileImage.apply {
            post { 
                val size = width 
                layoutParams.height = size
                requestLayout()

                Glide.with(context)
                    .load(post.getProfileImageUrl())
                    .override(size, size)
                    .centerCrop()
                    .into(this)
            }
        }

        // Asigna los datos al layout
        holder.binding.postUserName.text = post.name_user
        val spannableString = SpannableString("@"+post.user)
        spannableString.setSpan(UnderlineSpan(), 0, post.user.length+1, 0)
        holder.binding.postUser.text = spannableString
        holder.binding.PostText.text = post.text
        holder.binding.PostChallenge.text = post.challenge
        val formattedDate = DateFormatter.formatApiDate(post.date)
        holder.binding.PostDate.text = formattedDate

        // Configura los listeners para los elementos clicables
        holder.binding.profileImage.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
        holder.binding.postUser.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
        holder.binding.postUserName.setOnClickListener {
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
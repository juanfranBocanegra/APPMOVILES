package com.app.dolt.ui.challenge.vote

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.dolt.R
import com.app.dolt.databinding.ItemPostViewBinding
import com.app.dolt.databinding.ItemVoteViewBinding

/**
 * Vista personalizada que representa una publicación.
 * Utiliza un layout específico y permite configurar un listener de clic.
 *
 * @constructor Crea una nueva instancia de [PostView].
 * @param context : Contexto de la aplicación.
 */
class VoteView(
    context: Context
) : LinearLayout(context) {

    lateinit var binding: ItemVoteViewBinding

    private var onClickListener: (() -> Unit)? = null

    init {
        // Infla el layout asociaco y vincula el binding
        LayoutInflater.from(context).inflate(R.layout.item_vote_view, this, true)
        binding = ItemVoteViewBinding.inflate(LayoutInflater.from(context), this, true)

        // Configura el listener para el clic en la vista
        setOnClickListener {
            onClickListener?.invoke()
        }
    }

    /**
     * Establece un listener para manejar el clic sobre la vista de la publicación.
     *
     * @param listener : Función que se ejecuta al hacer clic.
     */
    // fun setOnPostClickListener(listener: () -> Unit) {
    //     this.onClickListener = listener
    // }

    /**
     * Establece el nombre del autor de la publicación en la vista.
     *
     * @param name : Nombre del autor.
     */
    // fun setName(name: String) {
    //     binding.nameText.text = name
    // }

    /**
     * Establece el detalle de la publicación en la vista.
     *
     * @param detail : Detalle de la publicación.
     */
    // fun setDetail(detail: String) {
    //     binding.detailText.text = detail
    // }
}

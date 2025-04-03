package com.app.dolt.ui.challenge

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.dolt.R
import com.app.dolt.databinding.ItemChallengeViewBinding


/**
 * Vista personalizada que representa un desafío.
 * Utiliza un layout específico y permite configurar nombre, detalle y listener de clic.
 *
 * @constructor Crea una nueva instancia de [ChallengeView].
 * @param context : Contexto de la aplicación.
 */
class ChallengeView(
    context: Context
) : LinearLayout(context) {

    lateinit var binding: ItemChallengeViewBinding

    private var onClickListener: (() -> Unit)? = null

    init {
        // infla el layout asociado y vincula el binding 
        LayoutInflater.from(context).inflate(R.layout.item_challenge_view, this, true)
        binding = ItemChallengeViewBinding.inflate(LayoutInflater.from(context), this, true)

        // Configura el listener de clic para la vista completa
        setOnClickListener {
            onClickListener?.invoke()
        }
    }

    /**
     * Establece un listener para manejar el clic sobre la vista del desafío.
     *
     * @param listener : Función que se ejecuta al hacer clic.
     */
    fun setOnChallengeClickListener(listener: () -> Unit) {
        this.onClickListener = listener
    }

    /**
     * Establece el nombre del desafío en la vista.
     *
     * @param name : Nombre del desafío.
     */
    fun setName(name: String) {
        binding.nameText.text = name
    }

    /**
     * Establece el detalle del desafío en la vista.
     *
     * @param detail : Detalle del desafío.
     */
    fun setDetail(detail: String) {
        binding.detailText.text = detail
    }
}

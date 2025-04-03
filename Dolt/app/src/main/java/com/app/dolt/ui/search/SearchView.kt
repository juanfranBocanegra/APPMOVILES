package com.app.dolt.ui.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.dolt.R
import com.app.dolt.databinding.ItemSearchViewBinding


/**
 * Vista personalizada que representa un elemento de búsqueda de usuario.
 * Utiliza un layout específico y permite configurar un listener de clic.
 *
 * @constructor Crea una nueva instancia de [SearchView].
 * @param context : Contexto de la aplicación.
 */
class SearchView(
    context: Context
) : LinearLayout(context) {

    lateinit var binding: ItemSearchViewBinding

    private var onClickListener: (() -> Unit)? = null

    init {
        // Infla el layout asociado y vincula el binding
        LayoutInflater.from(context).inflate(R.layout.item_search_view, this, true)
        binding = ItemSearchViewBinding.inflate(LayoutInflater.from(context), this, true)
        Log.i("HEIGHT::::::::::::",binding.main.height.toString())

        // Configura el listener para el clic en la vista
        setOnClickListener {
            onClickListener?.invoke()
        }

    }

    /**
     * Establece un listener para manejar el clic sobre la vista de búsqueda.
     *
     * @param listener : Función que se ejecuta al hacer clic.
     */
    // fun setOnPostClickListener(listener: () -> Unit) {
    //     this.onClickListener = listener
    // }

    /**
     * Establece el nombre del usuario en la vista.
     *
     * @param name : Nombre del usuario.
     */
    // fun setName(name: String) {
    //     binding.nameText.text = name
    // }

    /**
     * Establece el detalle del usuario en la vista.
     *
     * @param detail : Detalle del usuario.
     */
    // fun setDetail(detail: String) {
    //     binding.detailText.text = detail
    // }
}

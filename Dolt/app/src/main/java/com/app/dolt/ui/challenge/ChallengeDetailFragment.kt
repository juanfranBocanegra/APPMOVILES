package com.app.dolt.ui.challenge

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.dolt.R
import com.app.dolt.databinding.FragmentChallengeDetailBinding
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.PostRequest
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Fragmento que muestra los detalles de un desafío en un diálogo.
 * Permite al usuario ver la información del desafío y publicar un post si está disponible.
 */
class ChallengeDetailFragment : DialogFragment() {

    private var _binding: FragmentChallengeDetailBinding? = null
    private val binding get() = _binding!!

    /**
     * Establece el estilo del desafío.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    /**
     * Configura las dimensiones y animaciones del diálogo al iniciarse.
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            // Ajusta estas dimensiones según necesites
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

            // Opcional: animación al aparecer/desaparecer
            setWindowAnimations(R.style.DialogAnimation)
        }
    }

    /**
     * Infla la vista del fragmento.
     *
     * @return Vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    /**
     * Configura la vista una vez creada, incluyendo la carga de datos del desafío
     * y la gestión de eventos como publicar un post o cerrar el diálogo.
     *
     * @param view Vista creada.
     * @param savedInstanceState Estado anterior guardado (no utilizado).
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val challengeName = arguments?.getString("challenge_name") ?: "NULL"
        val challengeDetail = arguments?.getString("challenge_detail") ?: "NULL"
        val challengeId = arguments?.getString("challenge_id") ?: "NULL"
        val challengeAvailable : Boolean = (arguments?.getString("challenge_available") == "true")

        if (challengeAvailable){
            binding.editText.visibility = View.VISIBLE
            binding.postButton.visibility = View.VISIBLE
        }

        binding.nameTextOverlay.text = challengeName
        binding.detailTextOverlay.text = challengeDetail

        binding.closeOverlay.setOnClickListener {
            dismiss()  // Cierra el fragmento
        }

        binding.postButton.setOnClickListener {
            lifecycleScope.launch {
                RetrofitClient.apiService.post(PostRequest(challengeId,binding.editText.getText().toString()))
                dismiss()
            }
        }


    }

    /**
     * Limpia el binding al destruir la vista para evitar fugas de memoria.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Crea una nueva instancia del fragmento con los datos del desafío.
         *
         * @param name Nombre del desafío.
         * @param detail Detalle del desafío.
         * @param id Identificador del desafío.
         * @param available Indica si el desafío está disponible.
         * @return Nueva instancia de [ChallengeDetailFragment].
         */
        fun newInstance(name: String, detail: String, id: String, available: String): ChallengeDetailFragment {
            val fragment = ChallengeDetailFragment()
            val args = Bundle().apply {
                putString("challenge_name", name)
                putString("challenge_detail", detail)
                putString("challenge_id", id)
                putString("challenge_available", available.toString())
            }
            fragment.arguments = args
            return fragment
        }
    }
}

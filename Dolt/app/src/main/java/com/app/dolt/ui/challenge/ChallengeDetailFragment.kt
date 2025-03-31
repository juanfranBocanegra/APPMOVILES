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
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.PostRequest
import kotlinx.coroutines.launch

class ChallengeDetailFragment : DialogFragment() {
    private var _binding: FragmentChallengeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
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

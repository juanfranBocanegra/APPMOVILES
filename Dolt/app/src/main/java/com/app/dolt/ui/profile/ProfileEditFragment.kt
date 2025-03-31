import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.dolt.R
import com.app.dolt.databinding.FragmentProfileEditBinding
import com.app.dolt.model.ProfileRequest
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import androidx.lifecycle.lifecycleScope

class ProfileEditFragment : Fragment() {
    private var _binding: FragmentProfileEditBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    // Registro para el selector de imágenes
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.profileImage)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar datos actuales del perfil
        loadCurrentProfile()

        // Botón para seleccionar imagen
        binding.changeImageButton.setOnClickListener {
            openImagePicker()
        }

        // Botón para guardar cambios
        binding.saveButton.setOnClickListener {
            updateProfile()
        }
    }

    private fun loadCurrentProfile() {
        // Aquí cargas los datos actuales del usuario (puedes pasarlos por Bundle o desde API)
        val currentName = "Nombre actual" // Reemplaza con datos reales
        val currentImageUrl = "URL_imagen" // Reemplaza con datos reales

        binding.nameEditText.setText(currentName)
        Glide.with(this)
            .load(currentImageUrl)
            .circleCrop()
            .into(binding.profileImage)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun updateProfile() {
        val newName = binding.nameEditText.text.toString().trim()

        if (newName.isEmpty()) {
            Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Subir imagen si se seleccionó una nueva
                selectedImageUri?.let { uri ->
                    val file = File(uri.path) // Necesitas convertir Uri a File (ver nota abajo)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)

                    // Llamada a la API para actualizar imagen
                    //RetrofitClient.apiService.updateProfileImage(imagePart)
                }

                // Actualizar nombre
                //val updateRequest = ProfileRequest(newName)
                //RetrofitClient.apiService.updateProfile(updateRequest)

                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                // Cerrar fragmento o actualizar vista
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
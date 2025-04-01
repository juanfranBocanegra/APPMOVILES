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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.UserProfile
import com.app.dolt.ui.profile.ProfileActivity
import timber.log.Timber

class ProfileEditFragment(private var userProfile: MutableLiveData<UserProfile>, private var profileActivity: ProfileActivity) : Fragment() {
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0) // Solo padding top para la status bar
            insets
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ViewCompat.requestApplyInsets(view)
        // Cargar datos actuales del perfil
        loadCurrentProfile()

        binding.closeEdit.setOnClickListener {
            parentFragmentManager.popBackStack()  // Cierra el fragmento
        }

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
        val currentName = userProfile.value?.name // Reemplaza con datos reales


        binding.nameEditText.setText(currentName)
        binding.profileImage.apply{
            post { // Espera a que el view tenga dimensiones
                val size = width // Usamos el ancho como base
                layoutParams.height = size
                requestLayout()

                Glide.with(context)
                    .load(userProfile.value?.getProfileImageUrl())
                    .override(size, size)
                    .centerCrop()
                    .into(this)
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun updateProfile() {
        val newName = binding.nameEditText.text.toString().trim()


        if (newName.length < 4) {
            Toast.makeText(requireContext(), "El nombre es demasiado corto", Toast.LENGTH_SHORT).show()
            return
        }

        var ctx = this

        lifecycleScope.launch {
            try {

                //ctx.userProfile.removeObservers(ctx.profileActivity)
                // Subir imagen si se seleccionó una nueva
                var newProfile = ProfileRequest()
                /*selectedImageUri?.let { uri ->
                    val file = File(uri.path) // Necesitas convertir Uri a File (ver nota abajo)
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)

                    // Llamada a la API para actualizar imagen
                    //RetrofitClient.apiService.updateProfileImage(imagePart)
                }*/*/

                newProfile.name = newName
                // Actualizar nombre
                //val updateRequest = ProfileRequest(newName)
                //RetrofitClient.apiService.updateProfile(updateRequest)

                val currentPassword = binding.currentPasswordEditText.text.toString()
                val newPassword1 = binding.password1EditText.text.toString()
                val newPassword2 = binding.password2EditText.text.toString()

                if (    currentPassword != ""
                    ||  newPassword1 != ""
                    ||  newPassword2 != ""){

                    if ( newPassword1.length < 4){
                        Toast.makeText(requireContext(), "Contraseña demasiado corta", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    if ( newPassword1 != newPassword2){
                        Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    newProfile.password = listOf<String>(currentPassword, newPassword1, newPassword2)
                }

                val response = RetrofitClient.apiService.updateProfile(newProfile)
                val errorBody = response.errorBody()?.string()

                if (errorBody != null && errorBody.contains("Wrong") ){
                    Toast.makeText(requireContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                // Cerrar fragmento o actualizar vista
                userProfile.value?.name = newName
                //userProfile.profile_image = newImage



                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.userProfile.observe(this.profileActivity,this.profileActivity.profileObserver)
        _binding = null
    }
}
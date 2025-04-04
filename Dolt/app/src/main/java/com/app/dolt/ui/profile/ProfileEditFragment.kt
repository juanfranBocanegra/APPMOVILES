import android.app.Activity
import android.app.AlertDialog
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
import com.app.dolt.R
import com.app.dolt.databinding.FragmentProfileEditBinding
import com.app.dolt.model.ProfileRequest
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.app.dolt.api.RetrofitClient
import com.app.dolt.model.UserProfile
import com.app.dolt.ui.login.LoginActivity
import com.app.dolt.ui.profile.ProfileActivity
import timber.log.Timber


/**
 * Fragmento encargado de editar el perfil de un usuario.
 * Permite cambiar el nombre y la contraseña, y en un futuro la imagen de perfil.
 *
 * @property userProfile : Datos actuales del perfil del usuario.
 * @property profileActivity : Actividad asociada que muestra el perfil.
 */
class ProfileEditFragment(
    private var userProfile: MutableLiveData<UserProfile>, 
    private var profileActivity: ProfileActivity
) : Fragment() {

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
                binding.profileImage.apply {  post {
                    val size = width
                    layoutParams.height = size
                    requestLayout()

                    Glide.with(context)
                        .load(uri)
                        .override(size, size)
                        .centerCrop()
                        .into(this)
                }
                }
            }
        }
    }

    /**
     * Infla la vista del fragmento.
     *
     * @param inflater : Objeto utilizado para inflar la vista.
     * @param container : Vista padre en la que se incluirá la vista inflada.
     * @param savedInstanceState : Estado guardado previamente (si existe).
     * @return Vista raíz del fragmento.
     */
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

    /**
     * Configura la vista una vez creada, incluyendo los eventos de los botones.
     *
     * @param view : Vista creada.
     * @param savedInstanceState : Estado guardado previamente (si existe).
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.requestApplyInsets(view)

        // Carga datos actuales del perfil
        loadCurrentProfile()

        // Cierra el fragmento
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

        binding.deleteUserButton.setOnClickListener {
            showDeleteAccountConfirmationDialog()
        }
    }

    private fun showDeleteAccountConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_account))
            .setMessage(getString(R.string.delete_account_confirmation))
            .setPositiveButton(R.string.delete) { dialog, _ ->
                deleteUserAccount()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    
    /**
     * Elimina la cuenta del usuario actual.
     * 
     * Esta función se encarga de realizar las operaciones necesarias para
     * eliminar la cuenta del usuario de la aplicación. Asegúrate de que
     * el usuario confirme esta acción antes de llamarla, ya que esta operación
     * podría ser irreversible.
     */
    private fun deleteUserAccount() {
        lifecycleScope.launch {
            try {
                // Mostrar progreso


                val response = RetrofitClient.apiService.deleteUser()

                if (response.isSuccessful) {

                    Toast.makeText(requireContext(), "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar cuenta", Toast.LENGTH_SHORT).show()
                }


            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    /**
     * Carga los datos actuales del perfil en la vista.
     */
    private fun loadCurrentProfile() {
        val currentName = userProfile.value?.name 

        binding.nameEditText.setText(currentName)
        binding.profileImage.apply{
            post {
                val size = width
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

    /**
     * Abre el selector de imágenes para cambiar la imagen de perfil.
     */
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    /**
     * Actualiza el perfil del usuario con los nuevos datos introducidos.
     * Valida el nombre y las contraseñas antes de enviar la solicitud.
     */
    private fun updateProfile() {
        val newName = binding.nameEditText.text.toString().trim()

        if (newName.length < 4) {
            Toast.makeText(requireContext(), "El nombre es demasiado corto", Toast.LENGTH_SHORT).show()
            return
        }

        var ctx = this

        lifecycleScope.launch {
            try {
                binding.saveButton.isEnabled = false
                var newProfile = ProfileRequest()

                newProfile.name = newName

                val currentPassword = binding.currentPasswordEditText.text.toString()
                val newPassword1 = binding.password1EditText.text.toString()
                val newPassword2 = binding.password2EditText.text.toString()

                if (currentPassword != "" ||  newPassword1 != "" ||  newPassword2 != ""){
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

                if (selectedImageUri != null){
                    val imagePart = UriToMultipartConverter.convert(
                        uri = selectedImageUri!!,
                        context = requireContext(),
                        formDataName = "profile_image"  // Nombre que espera tu backend
                    ) ?: run {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    try {
                        RetrofitClient.apiService.updateProfileImage(imagePart)
                        Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        binding.saveButton.isEnabled = true
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                Toast.makeText(requireContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show()
                binding.saveButton.isEnabled = true

                userProfile.value?.name = newName


                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error al actualizar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Limpia el binding y reactiva el observador al destruir la vista.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        this.userProfile.observe(this.profileActivity,this.profileActivity.profileObserver)
        _binding = null
    }
}
package com.app.dolt.ui.profile

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dolt.R
import com.app.dolt.api.RetrofitClient
import com.app.dolt.databinding.FragmentProfileFollowBinding
import com.app.dolt.model.FollowResponse
import com.app.dolt.model.UserSimple
import com.app.dolt.ui.UserSimpleAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Fragmento que muestra la lista de seguidores y seguidos de un usuario.
 * Permite alternar entre ambas listas y navegar a los perfiles de otros usuarios.
 *
 * @property username Nombre de usuario del perfil que se está visualizando
 * @property option Indica qué lista mostrar inicialmente (1 = following, otros = followers)
 */
class ProfileFollowFragment(
    private val username: String,
    private var option: Int
) : Fragment() {

    private lateinit var binding: FragmentProfileFollowBinding

    /**
     * Método llamado durante la creación del fragmento.
     *
     * @param savedInstanceState Estado previamente guardado del fragmento.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Parámetros opcionales podrían ser procesados aquí
        }
    }

    /**
     * Infla la vista del fragmento.
     *
     * @param inflater Objeto utilizado para inflar la vista.
     * @param container Vista padre en la que se incluirá la vista inflada.
     * @param savedInstanceState Estado guardado previamente (si existe).
     * @return Vista raíz del fragmento.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Configura la vista una vez creada, incluyendo:
     * - Manejo de insets (sistema UI)
     * - Configuración inicial de las listas
     * - Eventos de los botones
     *
     * @param view Vista creada.
     * @param savedInstanceState Estado guardado previamente (si existe).
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.requestApplyInsets(view)

        // Configuración inicial de la UI
        setupInitialView()

        // Carga los datos de seguidos/seguidores
        loadFollow()

        // Configuración de listeners
        setupClickListeners()
    }

    /**
     * Configura la vista inicial basada en el parámetro 'option'.
     * Muestra la lista de seguidos o seguidores según corresponda.
     */
    private fun setupInitialView() {
        binding.followingButton.text = binding.followingButton.text.dropLast(4)
        binding.followersButton.text = binding.followersButton.text.dropLast(4)

        if (option == 1) {
            showFollowingList()
        } else {
            showFollowersList()
        }
    }

    /**
     * Muestra la lista de usuarios seguidos y actualiza los estilos de los botones.
     */
    private fun showFollowingList() {
        binding.recyclerViewFollowing.visibility = View.VISIBLE
        binding.recyclerViewFollowers.visibility = View.GONE
        binding.followingButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
        binding.followersButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light))
    }

    /**
     * Muestra la lista de seguidores y actualiza los estilos de los botones.
     */
    private fun showFollowersList() {
        binding.recyclerViewFollowing.visibility = View.GONE
        binding.recyclerViewFollowers.visibility = View.VISIBLE
        binding.followingButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light))
        binding.followersButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
    }

    /**
     * Configura los listeners para los elementos interactivos:
     * - Botón de cerrar
     * - Botones de seguidos/seguidores
     */
    private fun setupClickListeners() {
        binding.closeFollow.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.followingButton.setOnClickListener {
            showFollowingList()
        }

        binding.followersButton.setOnClickListener {
            showFollowersList()
        }
    }

    /**
     * Carga y muestra las listas de seguidos y seguidores desde la API.
     * Configura los adaptadores y los listeners para navegar a otros perfiles.
     */
    private fun loadFollow() {
        lifecycleScope.launch {
            try {
                // Inicialización de adaptadores
                val followingAdapter = UserSimpleAdapter()
                val followersAdapter = UserSimpleAdapter()

                // Configuración de RecyclerViews
                binding.recyclerViewFollowing.apply {
                    adapter = followingAdapter
                    layoutManager = LinearLayoutManager(context)
                }

                binding.recyclerViewFollowers.apply {
                    adapter = followersAdapter
                    layoutManager = LinearLayoutManager(context)
                }

                // Obtención de datos desde la API
                val response = RetrofitClient.apiService.getFollow(username)
                followingAdapter.updateUsers(response.following)
                followersAdapter.updateUsers(response.followers)

                // Configuración de click listeners para navegación
                setupAdapterListeners(followingAdapter, followersAdapter, response)

            } catch (e: Exception) {
                Timber.e("Error loading follow data: ${e.message}")
            }
        }
    }

    /**
     * Configura los listeners de los adaptadores para navegar a otros perfiles.
     *
     * @param followingAdapter Adaptador para la lista de seguidos
     * @param followersAdapter Adaptador para la lista de seguidores
     * @param response Respuesta de la API con los datos de relación
     */
    private fun setupAdapterListeners(
        followingAdapter: UserSimpleAdapter,
        followersAdapter: UserSimpleAdapter,
        response: FollowResponse
    ) {
        followingAdapter.setOnItemClickListener { position ->
            navigateToProfile(response.following[position])
        }

        followersAdapter.setOnItemClickListener { position ->
            navigateToProfile(response.followers[position])
        }
    }

    /**
     * Navega al perfil de un usuario específico.
     *
     * @param user Objeto UserSimple que representa al usuario destino
     */
    private fun navigateToProfile(user: UserSimple) {
        Intent(context, ProfileActivity::class.java).apply {
            putExtra("USERNAME", user.username)
            putExtra("MYSELF", "false")
            startActivity(this, ActivityOptions.makeCustomAnimation(context, 0, 0).toBundle())
        }
    }
}
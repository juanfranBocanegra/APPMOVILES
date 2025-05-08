package com.app.dolt.ui.challenge


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dolt.databinding.ActivityFeedCBinding
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.ui.MenuActivity

import kotlinx.coroutines.launch
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.dolt.databinding.FragmentFeedCBinding
import com.app.dolt.model.Challenge
import com.app.dolt.repository.UserStatsRepository
import com.app.dolt.ui.challenge.ChallengeAdapter
import com.app.dolt.ui.challenge.ChallengeViewModel
import timber.log.Timber


class FeedCFragment : Fragment() {
    private var _binding: FragmentFeedCBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = UserStatsRepository()

        lifecycleScope.launch {
            repository.refreshUserStats()
            val userStats = repository.getLocalUserStats()
            if (userStats?.vote_times == 0){
                binding.buttonNavigateToVote.visibility = View.GONE
            }else{
                binding.buttonNavigateToVote.visibility = View.VISIBLE
            }
            viewModel.refreshChallenges()
            viewModel.loadChallenges()
        }
        setupUI()
        observeViewModel()

        binding.buttonNavigateToVote.setOnClickListener {
            // Cierra el diálogo

            // Navega a VoteFragment usando el NavController del NavHostFragment
            val navController = findNavController()
            navController.navigate(R.id.action_feedCFragment_to_voteFragment)
            Timber.d("Navigating to VoteFragment from ChallengeDetailFragment")
        }
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.swipeRefresh.setOnRefreshListener {
            val repository = UserStatsRepository()
            lifecycleScope.launch {
                repository.refreshUserStats()
                val userStats = repository.getLocalUserStats()
                if (userStats?.vote_times == 0){
                    binding.buttonNavigateToVote.visibility = View.GONE
                }else{
                    binding.buttonNavigateToVote.visibility = View.VISIBLE
                }
                viewModel.refreshChallenges()
                viewModel.loadChallenges()
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.challenges.collect { challenges ->
                binding.swipeRefresh.isRefreshing = false

                val adapter = ChallengeAdapter().apply {
                    updateChallenges(challenges)
                    setOnItemClickListener { position ->
                        val challenge = challenges[position]
                        showChallengeDetail(challenge.name, challenge.detail, challenge.id, challenge.available.toString())
                    }
                }
                binding.recyclerView.adapter = adapter
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showChallengeDetail(name: String, detail: String, id: String, available: String) {
        val fragment = ChallengeDetailFragment.newInstance(name, detail, id, available)
        fragment.show(parentFragmentManager, "ChallengeDetailFragment")
        Timber.d("Showing dialog for: $name")
    }

}

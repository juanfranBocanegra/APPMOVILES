package com.app.dolt.ui.challenge.vote

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dolt.R
import com.app.dolt.databinding.FragmentVoteBinding
import com.app.dolt.model.Post
import com.app.dolt.repository.UserStatsRepository
import com.app.dolt.ui.post.PostAdapter
import com.app.dolt.ui.post.PostViewModel
import com.app.dolt.utils.StatsHandler
import kotlinx.coroutines.launch
import timber.log.Timber

fun <T> MutableList<T>.swap(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}


class VoteFragment : Fragment(R.layout.fragment_vote) {

    private lateinit var binding: FragmentVoteBinding

    private val viewModel: VoteViewModel by viewModels()

    private lateinit var voteAdapter: VoteAdapter
    private var localPosts = mutableListOf<Post>()

    private var userStatsRepository = UserStatsRepository()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentVoteBinding.bind(view)

        binding.close.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_voteFragment_to_feedCFragment)
            Timber.Forest.d("Navigating to ChallengeDetailFragment")
        }

        voteAdapter = VoteAdapter()
        binding.recyclerView.adapter = voteAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        lifecycleScope.launch {




            viewModel.posts.collect { posts ->
                if (posts.isNotEmpty()) {
                    localPosts = posts.toMutableList()
                    val adapter = voteAdapter
                    adapter.updatePosts(localPosts)

                    // Establecer el listener para cada elemento
                    adapter.setOnItemClickListener { position, dir : Int ->
                        if (dir == 0 && position > 0) {
                            localPosts.swap(position, position - 1)
                        } else if (dir == 1 && position < localPosts.size - 1) {
                            localPosts.swap(position, position + 1)
                        }
                        voteAdapter.updatePosts(localPosts)
                    }

                    binding.voteTitle.text = getString(R.string.vote_title) + viewModel.challenge?.name
                }

            }
        }

        binding.voteButton.setOnClickListener {
            lifecycleScope.launch {
                userStatsRepository.refreshUserStats()
                viewModel.sendVote(localPosts)
                val navController = findNavController()
                navController.navigate(R.id.action_voteFragment_to_feedCFragment)
                StatsHandler.restartRepeatingTask()
                userStatsRepository.refreshUserStats()

            }
        }

        viewModel.loadVote()




    }
}
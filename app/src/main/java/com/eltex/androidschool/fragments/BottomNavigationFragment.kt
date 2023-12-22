package com.eltex.androidschool.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.eltex.androidschool.R
import com.eltex.androidschool.databinding.FragmentBottomNavigationBinding

class BottomNavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentBottomNavigationBinding.inflate(inflater, container, false)

        val navController = requireNotNull(
            childFragmentManager.findFragmentById(R.id.container)
        ).findNavController()

        binding.bottomNavigation.setupWithNavController(navController)

        val newEventListener = View.OnClickListener {
            findNavController().navigate(R.id.action_bottomNavigationFragment_to_newEventFragment)
        }

        val newPostListener = View.OnClickListener {
            // TODO
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentEvents -> {
                    binding.newEvent.setOnClickListener(newEventListener)
                    binding.newEvent.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.fragmentPosts -> {
                    binding.newEvent.setOnClickListener(newPostListener)
                    binding.newEvent.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                }

                R.id.fragmentUsers -> {
                    binding.newEvent.setOnClickListener(null)
                    binding.newEvent.animate()
                        .scaleX(0F)
                        .scaleY(0F)
                }
            }
        }

        return binding.root
    }
}
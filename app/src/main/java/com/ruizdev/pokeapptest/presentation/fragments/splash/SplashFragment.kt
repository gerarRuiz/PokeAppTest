package com.ruizdev.pokeapptest.presentation.fragments.splash

import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentSplashBinding
import com.ruizdev.pokeapptest.presentation.common.BaseFragment
import com.ruizdev.pokeapptest.util.Constants.SPLASH_TIMEOUT

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private lateinit var binding: FragmentSplashBinding

    override fun FragmentSplashBinding.initialize() {
        binding = this
        skipSplash()
    }

    private fun skipSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_pokedexFragment2)
            binding.lottieAnimation.repeatCount = 0
        }, SPLASH_TIMEOUT)
        setAnim()
    }

    private fun setAnim() {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
        binding.tvTitleApp.startAnimation(animation)
    }

}
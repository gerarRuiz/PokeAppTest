package com.ruizdev.pokeapptest.presentation.fragments.avatar

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nvt.color.ColorPickerDialog
import com.ruizdev.pokeapptest.R
import com.ruizdev.pokeapptest.databinding.FragmentAvatarBinding
import com.ruizdev.pokeapptest.presentation.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AvatarFragment : BaseFragment<FragmentAvatarBinding>(FragmentAvatarBinding::inflate) {

    private lateinit var binding: FragmentAvatarBinding

    private val viewModel: AvatarViewModel by viewModels()

    private var loadImageJob: Job? = null
    private var initials = ""

    override fun FragmentAvatarBinding.initialize() {
        binding = this

        initUI()
        checkDataStoreInfo()

    }

    private fun initUI() {

        binding.etUrl.addTextChangedListener { value ->
            debounce(value?.toString() ?: "")
        }

        binding.etFrase.addTextChangedListener { value ->
            if (value != null && value.toString().isNotEmpty()) {
                changedText(value)
            } else {
                binding.tvInitials.text = ""
                initials = ""
            }
        }

        binding.btnBackgroundColor.setOnClickListener {
            colorPickerDialog { color ->
                viewModel.saveBackgroundColor(color)
                binding.viewBackgroundColor.setBackgroundColor(color)
                binding.imageViewAvatar.backgroundTintList = ColorStateList.valueOf(color)
            }
        }

        binding.btnTextColor.setOnClickListener {
            colorPickerDialog { color ->
                viewModel.saveTextColor(color)
                binding.viewTextColor.setBackgroundColor(color)
                binding.tvInitials.setTextColor(color)
            }
        }

    }

    private fun changedText(value: Editable) {

        viewModel.saveWord(value.toString())

        val split = value.toString().split("\\s".toRegex())

        if (split.size == 1) {

            initials = split[0].first().uppercaseChar().toString()
            if (binding.etUrl.text?.toString().isNullOrEmpty()) {
                binding.tvInitials.text = initials
            }

        } else if (split.size >= 2) {

            if (split[1].isNotEmpty()) {
                initials =
                    "${split[0].first().uppercaseChar()}${split[1].first().uppercaseChar()}"
                if (binding.etUrl.text?.toString().isNullOrEmpty()) {
                    binding.tvInitials.text = initials
                }

            }

        }
    }

    private fun checkDataStoreInfo() {

        lifecycleScope.launch {
            viewModel.avatarUrl.collectLatest {
                delay(150)
                binding.etUrl.setText(it)
            }
        }

        lifecycleScope.launch {
            viewModel.word.collectLatest {
                delay(200)
                binding.etFrase.setText(it)
            }
        }

        lifecycleScope.launch {
            viewModel.backgroundColor.collectLatest { color ->
                delay(150)
                binding.viewBackgroundColor.setBackgroundColor(color)
                binding.imageViewAvatar.backgroundTintList = ColorStateList.valueOf(color)
            }
        }

        lifecycleScope.launch {
            viewModel.textColor.collectLatest { color ->
                binding.viewTextColor.setBackgroundColor(color)
                binding.tvInitials.setTextColor(color)
            }
        }

    }

    private fun debounce(
        text: String
    ) {

        loadImageJob?.cancel()
        loadImageJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveAvatarUrl(text)
            delay(500)
            loadImage(text)
        }

    }

    private fun loadImage(url: String) {
        Glide.with(requireContext())
            .load(url)
            .apply(
                RequestOptions.circleCropTransform()
            )
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (initials.isEmpty()) {
                        binding.imageViewAvatar.background =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_image, null)
                    } else {
                        if (initials.first().isDigit()) {
                            binding.imageViewAvatar.background =
                                ResourcesCompat.getDrawable(resources, R.drawable.ic_image, null)
                        } else {
                            binding.tvInitials.text = initials
                            binding.tvInitials.visibility = View.VISIBLE
                        }

                    }
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.imageViewAvatar.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.circle, null)
                    binding.tvInitials.visibility = View.GONE
                    return false
                }
            }).into(binding.imageViewAvatar)
    }

    private fun colorPickerDialog(callback: (Int) -> Unit) {
        ColorPickerDialog(
            requireContext(),
            Color.BLACK,
            true,
            object : ColorPickerDialog.OnColorPickerListener {
                override fun onCancel(dialog: ColorPickerDialog?) {

                }

                override fun onOk(dialog: ColorPickerDialog?, color: Int) {
                    callback(color)
                }
            }
        ).show()
    }

}
package com.ruizdev.pokeapptest.presentation.common

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.ruizdev.pokeapptest.databinding.LayoutLoadingBinding

class CustomLoader(private val context: Context) {

    private lateinit var binding: LayoutLoadingBinding
    private lateinit var dialog: Dialog

    init {
        initProgress()
    }

    private fun initProgress() {

        binding = LayoutLoadingBinding.inflate(LayoutInflater.from(context), null, false)
        dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)

        val width = (context.resources.displayMetrics.widthPixels * 1.0).toInt()
        val height = (context.resources.displayMetrics.heightPixels * 1.0).toInt()

        dialog.window?.setLayout(width, height)
    }

    fun show() {
        binding.root.backgroundTintList = null
        binding.root.background = null
        dialog.show()
    }

    fun getDialog(): Dialog = dialog

}
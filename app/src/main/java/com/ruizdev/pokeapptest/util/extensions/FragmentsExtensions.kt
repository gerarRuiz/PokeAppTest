package com.ruizdev.pokeapptest.util.extensions

import androidx.fragment.app.Fragment
import com.ruizdev.pokeapptest.presentation.common.DialogBasic
import com.ruizdev.pokeapptest.util.enums.DialogAnim
import com.ruizdev.pokeapptest.util.ConstantsDialogs.DIALOG_BASIC

fun Fragment.showBasicDialog(
    title: String,
    message: String,
    textButton: String,
    anim: DialogAnim,
    onAction: () -> Unit
) {

    DialogBasic.newInstance(
        title,
        message,
        textButton,
        anim
    ).apply {
        this.setOnAction(onAction)
    }.show(childFragmentManager, DIALOG_BASIC)

}
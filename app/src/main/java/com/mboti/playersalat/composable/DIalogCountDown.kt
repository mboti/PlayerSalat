package com.mboti.playersalat.composable


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogCountdown(context: Context, onDismiss: () -> Unit, onExit: () -> Unit) {

    Dialog(onDismissRequest = { onDismiss() }, properties = DialogProperties(
        dismissOnBackPress = false,dismissOnClickOutside = false
    )) {
//        Surface(
//            //color = Color(0xFF101010),
//            modifier = Modifier.fillMaxSize()
//        ) {

        //}

    }
}
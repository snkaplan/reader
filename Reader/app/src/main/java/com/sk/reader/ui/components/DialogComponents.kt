package com.sk.reader.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomAlertDialog(
    title: String,
    description: String,
    confirmText: String,
    dismissText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(description)
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm.invoke()
                    onDismiss.invoke()
                }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss.invoke()
                }) {
                Text(dismissText)
            }
        }
    )
}
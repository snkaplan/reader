package com.sk.reader.ui.screens.login.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sk.reader.R
import com.sk.reader.ui.components.EmailInput
import com.sk.reader.ui.components.InputField
import com.sk.reader.ui.components.PasswordInput

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginForm(
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    focusManager: FocusManager,
    loading: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    EmailInput(
        emailState = emailState,
        enabled = !loading,
        labelId = stringResource(id = R.string.email),
        onAction = KeyboardActions { focusManager.moveFocus(FocusDirection.Down) })
    PasswordInput(
        modifier = Modifier,
        passwordState = passwordState,
        labelId = stringResource(id = R.string.password),
        enabled = !loading,
        passwordVisibility = passwordVisibility,
        onAction = KeyboardActions { keyboardController?.hide() }
    )
}

@Composable
fun SignUpForm(
    nameState: MutableState<String>,
    surnameState: MutableState<String>,
    quoteState: MutableState<String>,
    professionState: MutableState<String>,
    focusManager: FocusManager,
    loading: Boolean = false,
) {
    Text(
        text = stringResource(id = R.string.create_acct),
        modifier = Modifier.padding(4.dp)
    )
    InputField(
        valueState = nameState,
        labelId = stringResource(id = R.string.name),
        enabled = !loading,
        onAction = KeyboardActions {
            focusManager.moveFocus(FocusDirection.Down)
        })
    InputField(
        valueState = surnameState,
        labelId = stringResource(id = R.string.surname),
        enabled = !loading,
        onAction = KeyboardActions {
            focusManager.moveFocus(FocusDirection.Down)
        })
    InputField(
        valueState = quoteState,
        labelId = stringResource(id = R.string.quote),
        enabled = !loading,
        onAction = KeyboardActions {
            focusManager.moveFocus(FocusDirection.Down)
        })
    InputField(
        valueState = professionState,
        labelId = stringResource(id = R.string.profession),
        enabled = !loading,
        onAction = KeyboardActions {
            focusManager.moveFocus(FocusDirection.Down)
        })
}
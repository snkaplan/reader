package com.sk.reader.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sk.reader.R
import com.sk.reader.ui.components.ReaderLogo
import com.sk.reader.ui.navigation.ReaderScreens
import com.sk.reader.ui.screens.login.components.LoginForm
import com.sk.reader.ui.screens.login.components.SignUpForm

@Composable
fun ReaderLoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState: AuthState by authViewModel.authState.collectAsStateWithLifecycle()
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate(ReaderScreens.ReaderHomeScreen.name) {
                    popUpTo(ReaderScreens.SplashScreen.name) {
                        inclusive = true
                    }
                }
            }

            else -> {}
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderLogo()
            if (showLoginForm.value) {
                UserForm(
                    loading = authState is AuthState.Authenticating || authState is AuthState.CreatingUser,
                    isCreateAccount = false
                ) { _, _, _, _, email, password ->
                    authViewModel.signIn(email, password)
                }
            } else {
                UserForm(
                    modifier = Modifier.weight(1f, false),
                    loading = authState is AuthState.Authenticating || authState is AuthState.CreatingUser,
                    isCreateAccount = true
                ) { name, surname, quote, profession, email, password ->
                    authViewModel.createUser(name, surname, quote, profession, email, password)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(15.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text =
                    if (showLoginForm.value) stringResource(id = R.string.sign_up) else stringResource(
                        id = R.string.login
                    )
                Text(text = stringResource(id = R.string.new_user))
                Text(
                    text = text,
                    modifier = Modifier
                        .clickable { showLoginForm.value = !showLoginForm.value }
                        .padding(start = 5.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String, String, String, String, String) -> Unit
) {
    val name = rememberSaveable {
        mutableStateOf("")
    }
    val surname = rememberSaveable {
        mutableStateOf("")
    }
    val quote = rememberSaveable {
        mutableStateOf("")
    }
    val profession = rememberSaveable {
        mutableStateOf("")
    }
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(name.value, surname.value, email.value, password.value) {
        val mailPwValidation = email.value.trim()
            .isNotEmpty() && password.value.trim().isNotEmpty()
        if (isCreateAccount) {
            name.value.trim().isNotEmpty() && surname.value.trim()
                .isNotEmpty() && mailPwValidation
        } else {
            mailPwValidation
        }
    }
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isCreateAccount) {
            SignUpForm(name, surname, quote, profession, focusManager, loading)
        }
        LoginForm(email, password, focusManager, loading)
        SubmitButton(
            textId = if (isCreateAccount) stringResource(id = R.string.create_account) else stringResource(
                id = R.string.login
            ),
            loading = loading,
            validInputs = valid
        ) {
            onDone(
                name.value.trim(),
                surname.value.trim(),
                quote.value.trim(),
                profession.value.trim(),
                email.value.trim(),
                password.value.trim()
            )
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}
package com.sk.reader.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sk.reader.R
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.navigation.ReaderScreens
import com.sk.reader.ui.screens.login.AuthState
import com.sk.reader.ui.screens.login.AuthViewModel
import com.sk.reader.ui.theme.Light_Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderHomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState: AuthState by authViewModel.authState.collectAsStateWithLifecycle()
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.SignedOut -> {
                navController.navigate(ReaderScreens.LoginScreen.name)
            }

            else -> {}
        }
    }
    Scaffold(
        topBar = {
            ReaderAppTopBar(stringResource(id = R.string.app_title)) {
                authViewModel.signOut()
            }
        },
        floatingActionButton = {
            FABContent {

            }
        }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

        }
    }
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() },
        shape = RoundedCornerShape(50.dp),
        containerColor = Light_Blue
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a Book",
            tint = Color.White
        )
    }
}

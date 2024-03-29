package com.sk.reader.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sk.reader.R
import com.sk.reader.ui.components.ReaderLogo
import com.sk.reader.ui.navigation.ReaderScreens
import com.sk.reader.ui.screens.login.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun ReaderSplashScreen(navController: NavController, authViewModel: AuthViewModel) {
    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(0.9f, animationSpec = tween(800, easing = {
            OvershootInterpolator(8f).getInterpolation(it)
        }))
        if (authViewModel.getCurrentUser()?.email.isNullOrEmpty().not()) {
            authViewModel.getUser()
            navController.navigate(ReaderScreens.ReaderHomeScreen.name) {
                popUpTo(ReaderScreens.SplashScreen.name) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(ReaderScreens.LoginScreen.name) {
                popUpTo(ReaderScreens.SplashScreen.name) {
                    inclusive = true
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(id = R.string.splash_title),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray
            )
        }
    }
}
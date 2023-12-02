package com.sk.reader.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.sk.reader.R
import com.sk.reader.model.Book
import com.sk.reader.model.User
import com.sk.reader.ui.components.FABContent
import com.sk.reader.ui.components.ListCard
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.components.TitleItem
import com.sk.reader.ui.navigation.ReaderScreens
import com.sk.reader.ui.screens.login.AuthState
import com.sk.reader.ui.screens.login.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderHomeScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState: AuthState by authViewModel.authState.collectAsStateWithLifecycle()
    val userState: User? by authViewModel.user.collectAsStateWithLifecycle()
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
            ReaderAppTopBar(
                stringResource(id = R.string.app_title),
                leftIcon = Icons.Default.Book,
                rightIcon = Icons.Filled.Logout,
                onRightIconClicked = { authViewModel.signOut() })
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreens.SearchScreen.name)
            }
        }) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            HomeContent(navController, userState?.name)
        }
    }
}

@Composable
fun HomeContent(navController: NavController, currentUser: String?) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleItem(label = stringResource(id = R.string.reading_now_title))
            Column(
                modifier = Modifier.padding(end = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer)
                Text(
                    text = currentUser ?: stringResource(id = R.string.not_available),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
        ReadingRightNowArea(books = dummyBooks(), navController = navController)
        TitleItem(label = stringResource(id = R.string.reading_list_title))
        ReadingListArea(books = dummyBooks(), navController = navController)
    }
}

@Composable
fun ReadingRightNowArea(books: List<Book>, navController: NavController) {
    HorizontalScrollableComponent(books) {}
}

@Composable
fun ReadingListArea(books: List<Book>, navController: NavController) {
    HorizontalScrollableComponent(books) {}
}

@Composable
fun HorizontalScrollableComponent(listOfBook: List<Book>, onCardClicked: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listOfBook) {
            ListCard(it) { id -> onCardClicked.invoke(id) }
        }
    }
}

private fun dummyBooks(): List<Book> {
    return listOf(
        Book(id = "dadfa", title = "Hello Again", authors = "All of us", notes = "null", "", "", ""),
        Book(id = "dadfa", title = " Again", authors = "All of us", notes = "null", "", "", ""),
        Book(id = "dadfa", title = "Hello ", authors = "The world us", notes = "null", "", "", ""),
        Book(id = "dadfa", title = "Hello Again", authors = "All of us", notes = "null", "", "", ""),
        Book(id = "dadfa", title = "Hello Again", authors = "All of us", notes = "null", "", "", "")
    )
}
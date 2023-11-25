package com.sk.reader.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
            FABContent {}
        }) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            HomeContent(navController, authViewModel.getCurrentUser()?.displayName)
        }
    }
}

@Composable
fun HomeContent(navController: NavController, currentUser: String?) {
    Column(
        modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleItem(label = stringResource(id = R.string.reading_now_title))
            Column(modifier = Modifier.padding(end = 5.dp)) {
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
                    modifier = Modifier.padding(end = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
        ReadingRightNowArea(books = listOf(), navController = navController)
        TitleItem(label = stringResource(id = R.string.reading_list_title))
        BookListArea(listOfBook = dummyBooks(), navController = navController)
    }
}

@Composable
fun BookListArea(listOfBook: List<Book>, navController: NavController) {
    HorizontalScrollableComponent(listOfBook) {
        // TODO Go to book detail
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBook: List<Book>, onCardClicked: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {
        listOfBook.forEach {
            ListCard(it) { id -> onCardClicked.invoke(id) }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<Book>, navController: NavController) {
    ListCard(dummyBooks()[0]) {}
}

private fun dummyBooks(): List<Book> {
    return listOf(
        Book(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
        Book(id = "dadfa", title = " Again", authors = "All of us", notes = null),
        Book(id = "dadfa", title = "Hello ", authors = "The world us", notes = null),
        Book(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
        Book(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null)
    )
}
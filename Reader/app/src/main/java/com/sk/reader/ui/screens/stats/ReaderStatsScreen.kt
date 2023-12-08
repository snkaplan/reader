package com.sk.reader.ui.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.sk.reader.R
import com.sk.reader.model.MBook
import com.sk.reader.model.User
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.navigation.ReaderScreens
import com.sk.reader.ui.screens.home.HomeScreenState
import com.sk.reader.ui.screens.home.HomeViewModel
import com.sk.reader.ui.screens.login.AuthViewModel
import com.sk.reader.utils.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderStatsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    booksViewModel: HomeViewModel,
    forceRefresh: Boolean
) {
    val userState: User? by authViewModel.user.collectAsStateWithLifecycle()
    val homeScreenState: HomeScreenState by booksViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = forceRefresh) {
        if (forceRefresh) {
            booksViewModel.getUserBooks()
        }
    }
    Scaffold(
        topBar = {
            ReaderAppTopBar(
                stringResource(id = R.string.profile),
                leftIcon = Icons.Default.ArrowBack,
                leftIconTint = Color.Red.copy(alpha = 0.7f),
                onLeftIconClicked = { navController.popBackStack() })
        }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                when (homeScreenState) {
                    HomeScreenState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    is HomeScreenState.Success -> {
                        val state = (homeScreenState as HomeScreenState.Success)
                        userState?.name?.let { safeName ->
                            HelloMessage(name = safeName)
                        }
                        StatsCard(state.data)
                        Divider()
                        BookList(bookList = state.data, navController = navController)
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun HelloMessage(name: String) {
    Text(
        text = stringResource(id = R.string.hello_message, name),
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic
        )
    )
}

@Composable
fun StatsCard(bookList: List<MBook?>) {
    val readingNowBooksSize = bookList.filter { mBook ->
        mBook?.startedReading != null && mBook.finishedReading == null
    }.size
    val finishedBooksSize = bookList.filter { mBook ->
        mBook?.finishedReading != null
    }.size
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp)) {
            Text(
                text = stringResource(id = R.string.your_stats),
                style = MaterialTheme.typography.headlineMedium
            )
            Divider()
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = stringResource(id = R.string.reading_stats, readingNowBooksSize),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = stringResource(id = R.string.have_read_stats, finishedBooksSize),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun BookList(bookList: List<MBook?>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = bookList) { book ->
            if (book != null) {
                BookItem(book) { navController.navigate(ReaderScreens.UpdateScreen.name + "/${book.id}") }
            }
        }
    }
}

@Composable
fun BookItem(book: MBook, onBookClicked: () -> Unit) {
    Card(modifier = Modifier
        .clickable { onBookClicked.invoke() }
        .fillMaxWidth()
        .padding(3.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(book.thumbnail)
                        .crossfade(true).transformations(RoundedCornersTransformation()).build()
                ),
                contentScale = ContentScale.Crop,
                contentDescription = "Book image",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .padding(end = 5.dp)
            )
            Column(modifier = Modifier.padding(5.dp)) {
                Row {
                    Text(
                        modifier = Modifier.weight(0.9f),
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        overflow = TextOverflow.Ellipsis
                    )
                    book.rating?.let { safeRating ->
                        if (safeRating > 3.0) {
                            Icon(
                                imageVector = Icons.Filled.ThumbUp,
                                tint = Color.Blue.copy(alpha = 0.8f),
                                contentDescription = "ThumbUp"
                            )
                        }
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text = stringResource(id = R.string.authors) + "[" + book.authors + "]",
                    style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                )
                book.startedReading?.let { safeStartTime ->
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = stringResource(
                            id = R.string.started_reading_on,
                            safeStartTime.formatDate()
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                    )
                }
                book.finishedReading?.let { safeFinishTime ->
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = stringResource(
                            id = R.string.finished_on,
                            safeFinishTime.formatDate()
                        ),
                        style = MaterialTheme.typography.labelSmall.copy(fontStyle = FontStyle.Italic),
                    )
                }
            }
        }
    }
}

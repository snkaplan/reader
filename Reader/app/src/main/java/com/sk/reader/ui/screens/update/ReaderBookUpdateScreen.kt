package com.sk.reader.ui.screens.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sk.reader.R
import com.sk.reader.model.MBook
import com.sk.reader.ui.components.InputField
import com.sk.reader.ui.components.RatingBar
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.components.RoundedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookUpdateScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookUpdateViewModel
) {
    val uiState: BookUpdateScreenState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorFlow: String by viewModel.errorFlow.collectAsStateWithLifecycle("")
    if (errorFlow.isEmpty().not()) {
        println(errorFlow)
    }
    LaunchedEffect(key1 = bookId) {
        viewModel.getBookFromFirestoreById(bookId)
    }
    Scaffold(
        topBar = {
            ReaderAppTopBar(
                stringResource(id = R.string.book_update),
                leftIcon = Icons.Default.ArrowBack,
                leftIconTint = Color.Red.copy(alpha = 0.7f),
                onLeftIconClicked = { navController.popBackStack() }
            )
        }) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (uiState.isLoading && uiState.mBook == null) {
                CircularProgressIndicator(modifier = Modifier.size(25.dp))
            } else {
                uiState.mBook?.let { safeBook ->
                    Column(
                        modifier = Modifier.padding(3.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(), shape = CircleShape, shadowElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 40.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                BookCard(book = safeBook) {}
                            }
                        }
                        FormField(isLoading = uiState.isLoading, book = safeBook)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormField(modifier: Modifier = Modifier, isLoading: Boolean, book: MBook) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(3.dp)
            .background(Color.White, CircleShape)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        val notes = rememberSaveable {
            mutableStateOf(book.notes ?: "")
        }
        val readingStarted = rememberSaveable {
            mutableStateOf(book.startedReading != null)
        }
        val readingFinished = rememberSaveable {
            mutableStateOf(book.finishedReading != null)
        }
        val ratingVal = rememberSaveable {
            mutableIntStateOf(book.rating?.toInt() ?: 0)
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        InputField(
            valueState = notes,
            labelId = "Enter Your Thoughts",
            enabled = isLoading.not(),
            isSingleLine = false,
            onAction = KeyboardActions {
                keyboardController?.hide()
            }
        )
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(onClick = {
                readingStarted.value = true
            }, enabled = readingStarted.value.not()) {
                Text(text = if (readingStarted.value.not()) "Start Reading" else "Started on ${book.startedReading}")
            }
            TextButton(onClick = {
                readingFinished.value = true
            }, enabled = readingFinished.value.not() && readingStarted.value) {
                Text(text = if (readingFinished.value.not()) "Mark as Read" else "Already ended")
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
            RatingBar(rating = ratingVal.intValue) { rating ->
                ratingVal.intValue = rating
            }
        }
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RoundedButton(label = "Update") {

            }
            RoundedButton(label = "Delete") {

            }
        }
    }
}

@Composable
fun BookCard(book: MBook, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick.invoke() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Image(
                painter = rememberAsyncImagePainter(model = book.thumbnail),
                contentDescription = "Book Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(120.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
            )
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    book.authors,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 0.dp)
                )
                Text(
                    book.publishedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 0.dp, bottom = 8.dp)
                )
            }
        }
    }
}
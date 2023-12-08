package com.sk.reader.ui.screens.update

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.Timestamp
import com.sk.reader.R
import com.sk.reader.model.MBook
import com.sk.reader.ui.components.CustomAlertDialog
import com.sk.reader.ui.components.InputField
import com.sk.reader.ui.components.RatingBar
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.components.RoundedButton
import com.sk.reader.ui.navigation.BOOK_UPDATED
import com.sk.reader.utils.formatDate
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookUpdateScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookUpdateViewModel
) {
    val isBookUpdated = remember {
        mutableStateOf(false)
    }
    val uiState: BookUpdateScreenState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorFlow: String by viewModel.errorFlow.collectAsStateWithLifecycle("")
    if (errorFlow.isEmpty().not()) {
        println(errorFlow)
    }
    LaunchedEffect(key1 = bookId) {
        viewModel.getBookFromFirestoreById(bookId)
    }
    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                BookUpdateScreenEvents.BookDeletedSuccessfully -> {
                    isBookUpdated.value = true
                    goBack(
                        navController,
                        isBookUpdated.value
                    )
                }
            }
        }
    }
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) {
        CustomAlertDialog(
            title = stringResource(id = R.string.delete_book),
            description = stringResource(id = R.string.delete_book_modal_desc),
            confirmText = stringResource(id = R.string.yes),
            dismissText = stringResource(id = R.string.cancel),
            onDismiss = { openDialog.value = false },
            onConfirm = { viewModel.deleteBook() })
    }
    Scaffold(
        topBar = {
            ReaderAppTopBar(
                stringResource(id = R.string.book_update),
                leftIcon = Icons.Default.ArrowBack,
                leftIconTint = Color.Red.copy(alpha = 0.7f),
                onLeftIconClicked = {
                    goBack(
                        navController,
                        isBookUpdated.value
                    )
                }
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
                    val ratingVal = rememberSaveable {
                        mutableIntStateOf(safeBook.rating?.toInt() ?: 0)
                    }
                    val notes = rememberSaveable {
                        mutableStateOf(safeBook.notes ?: "")
                    }
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
                        NotesField(
                            notesState = notes,
                            isLoading = uiState.isLoading,
                        )
                        ReadInfoField(
                            safeBook.startedReading,
                            safeBook.finishedReading,
                            uiState.isLoading,
                            viewModel
                        )
                        RatingField(ratingState = ratingVal)
                        ButtonsField(onUpdateClick = {
                            isBookUpdated.value = true
                            viewModel.updateBook(ratingVal.intValue, notes.value)
                        }, onDeleteClick = {
                            openDialog.value = true
                        })
                    }
                }
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NotesField(isLoading: Boolean, notesState: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    InputField(
        modifier = Modifier.padding(horizontal = 10.dp),
        valueState = notesState,
        labelId = stringResource(id = R.string.notes_field_label),
        enabled = isLoading.not(),
        isSingleLine = false,
        onAction = KeyboardActions {
            keyboardController?.hide()
        }
    )
}

@Composable
fun ReadInfoField(
    startedReadingTs: Timestamp?,
    finishedReadingTs: Timestamp?,
    isLoading: Boolean,
    viewModel: BookUpdateViewModel
) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            TextButton(onClick = {
                viewModel.startReading()
            }, enabled = startedReadingTs == null) {
                Text(
                    text = if (startedReadingTs == null) stringResource(id = R.string.start_reading) else stringResource(
                        id = R.string.started_reading_on, startedReadingTs.formatDate()
                    )
                )
            }
            TextButton(onClick = {
                viewModel.endReading()
            }, enabled = finishedReadingTs == null && startedReadingTs != null) {
                Text(
                    text = if (finishedReadingTs == null) stringResource(id = R.string.mark_as_read) else stringResource(
                        id = R.string.finished_on, finishedReadingTs.formatDate()
                    )
                )
            }
        }
    }
}

@Composable
fun RatingField(ratingState: MutableIntState) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.rating),
            modifier = Modifier.padding(bottom = 3.dp)
        )
        RatingBar(rating = ratingState.intValue) { rating ->
            ratingState.intValue = rating
        }
    }
}

@Composable
fun ButtonsField(onUpdateClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        RoundedButton(label = stringResource(id = R.string.update)) {
            onUpdateClick.invoke()
        }
        RoundedButton(label = stringResource(id = R.string.delete)) {
            onDeleteClick.invoke()
        }
    }
}

fun goBack(navController: NavController, isBookUpdated: Boolean = false) {
    navController.previousBackStackEntry?.savedStateHandle?.set(
        BOOK_UPDATED,
        isBookUpdated
    )
    navController.popBackStack()
}
package com.sk.reader.ui.screens.details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sk.reader.R
import com.sk.reader.model.Book
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.components.RoundedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookDetailsScreen(
    navController: NavController, bookId: String,
    viewModel: BookDetailsViewModel
) {
    LaunchedEffect(key1 = true) {
        viewModel.getBook(bookId)
    }
    val bookState: BookDetailsScreenState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorFlow: String by viewModel.errorFlow.collectAsStateWithLifecycle("")
    if (errorFlow.isEmpty().not()) {
        println(errorFlow)
        // TODO Show snackbar
    }
    Scaffold(
        topBar = {
            ReaderAppTopBar(
                stringResource(id = R.string.book_detail),
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
            Column(modifier = Modifier.fillMaxSize()) {
                if (bookState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    bookState.book?.let { safeBook ->
                        BookDetails(
                            safeBook,
                            bookState.bookSaved,
                            navController,
                            viewModel
                        )
                    } ?: kotlin.run {
                        // TODO Error state
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetails(
    data: Book,
    isBookSaved: Boolean,
    navController: NavController,
    viewModel: BookDetailsViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        GeneralInfoField(data)
        Spacer(modifier = Modifier.height(5.dp))
        DescriptionField(data.description)
        ActionButtons(
            modifier = Modifier.align(Alignment.End),
            isBookSaved,
            navController,
            viewModel
        )
    }
}

@Composable
fun GeneralInfoField(data: Book) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = data.thumbnail),
                contentDescription = "Book Image",
                modifier = Modifier
                    .size(90.dp)
                    .padding(1.dp)
            )
        }
        Text(
            text = data.title,
            style = MaterialTheme.typography.titleLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            textAlign = TextAlign.Center
        )
        Text(text = "${stringResource(id = R.string.authors)}: ${data.authors}")
        Text(text = "${stringResource(id = R.string.page_count)}: ${data.pageCount}")
        Text(
            text = "${stringResource(id = R.string.categories)}: ${data.categories}",
            style = MaterialTheme.typography.labelMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
        Text(
            text = "${stringResource(id = R.string.published)}: ${data.publishedDate}",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun DescriptionField(description: String) {
    val cleanDescription =
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    if (cleanDescription.isEmpty().not()) {
        val localDims = LocalContext.current.resources.displayMetrics
        Surface(
            modifier = Modifier
                .height(localDims.heightPixels.dp.times(0.09f))
                .padding(14.dp), shape = RectangleShape,
            border = BorderStroke(1.dp, Color.DarkGray)
        ) {
            LazyColumn(modifier = Modifier.padding(3.dp)) {
                item {
                    Text(text = cleanDescription)
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    isBookSaved: Boolean,
    navController: NavController,
    viewModel: BookDetailsViewModel
) {
    Row(
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (isBookSaved.not()) {
            RoundedButton(label = stringResource(id = R.string.save), radius = 20) {
                viewModel.saveBook()
            }
        }
        RoundedButton(label = stringResource(id = R.string.cancel), radius = 20) {
            navController.popBackStack()
        }
    }
}

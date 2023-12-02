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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sk.reader.model.Book
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.components.RoundedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookDetailsScreen(
    navController: NavController, bookId: String,
    viewModel: BookDetailsViewModel = hiltViewModel()
) {
    viewModel.getBook(bookId)
    val bookState: BookDetailsScreenState by viewModel.bookState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            ReaderAppTopBar(
                "Book Detail",
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
            Column(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (bookState) {
                    is BookDetailsScreenState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    is BookDetailsScreenState.Success -> {
                        BookDetails(
                            (bookState as BookDetailsScreenState.Success).data,
                            navController
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun BookDetails(data: Book, navController: NavController) {
    Card(
        modifier = Modifier.padding(34.dp),
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
        maxLines = 3
    )
    Text(text = "Authors: ${data.authors}")
    Text(text = "Page Count: ${data.pageCount}")
    Text(
        text = "Categories: ${data.categories}",
        style = MaterialTheme.typography.labelMedium, overflow = TextOverflow.Ellipsis, maxLines = 3
    )
    Text(
        text = "Published: ${data.publishedDate}",
        style = MaterialTheme.typography.labelMedium
    )
    Spacer(modifier = Modifier.height(5.dp))
    DescriptionField(data.description, navController)
    Row(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        RoundedButton(label = "Save", radius = 20) {
            // Save this book to DB
        }
        RoundedButton(label = "Cancel", radius = 20) {
            navController.popBackStack()
        }
    }
}

@Composable
fun DescriptionField(description: String, navController: NavController) {
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

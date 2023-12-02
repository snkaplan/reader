package com.sk.reader.ui.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.sk.reader.R
import com.sk.reader.model.Book
import com.sk.reader.ui.components.InputField
import com.sk.reader.ui.components.ReaderAppTopBar
import com.sk.reader.ui.navigation.ReaderScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderBookSearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchState: SearchScreenState by viewModel.searchState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            ReaderAppTopBar(
                stringResource(id = R.string.search_screen_title),
                leftIcon = Icons.Default.ArrowBack,
                leftIconTint = Color.Red.copy(alpha = 0.7f),
                onLeftIconClicked = { navController.popBackStack() }
            )
        }) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), loading = false
                ) { searchQuery ->
                    viewModel.searchBooks(searchQuery)
                }
                Spacer(modifier = Modifier.height(15.dp))
                when (searchState) {
                    is SearchScreenState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(CenterHorizontally)
                        )
                    }

                    is SearchScreenState.Success -> {
                        BookList(navController, (searchState as SearchScreenState.Success).data)
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun BookList(navController: NavController, books: List<Book>) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        items(items = books) { book ->
            BookRowItem(book, navController)
        }
    }
}

@Composable
fun BookRowItem(book: Book, navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ReaderScreens.DetailScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .padding(3.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            Image(
                painter = rememberAsyncImagePainter(model = book.thumbnail),
                contentDescription = "Book image",
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .padding(end = 5.dp)
            )
            Column {
                Text(text = book.title, overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Author ${book.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    "Date: ${book.publishedDate}", softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    book.categories, softWrap = true,
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(modifier: Modifier = Modifier, loading: Boolean, onSearch: (String) -> Unit) {
    val searchQueryState = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchQueryState.value) {
        searchQueryState.value.trim().isNotEmpty()
    }
    InputField(
        valueState = searchQueryState,
        labelId = "Search",
        enabled = !loading,
        onAction = KeyboardActions {
            if (!valid) return@KeyboardActions
            onSearch.invoke(searchQueryState.value.trim())
            keyboardController?.hide()
        })
}
package com.sk.reader.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.sk.reader.R
import com.sk.reader.model.Book
import com.sk.reader.ui.theme.Light_Blue
import com.sk.reader.ui.theme.Light_Red

@Composable
fun ReaderLogo(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(16.dp),
        text = stringResource(id = R.string.app_title),
        style = MaterialTheme.typography.headlineLarge,
        color = Color.Red.copy(alpha = 0.5f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderAppTopBar(
    title: String,
    leftIcon: ImageVector? = null,
    leftIconTint: Color = Light_Blue,
    rightIcon: ImageVector? = null,
    rightIconTint: Color = Color.Green.copy(alpha = 0.4f),
    onRightIconClicked: (() -> Unit)? = null,
    onLeftIconClicked: (() -> Unit)? = null
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
        modifier = Modifier,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leftIcon != null) {
                    Icon(
                        imageVector = leftIcon,
                        contentDescription = "Icon",
                        tint = leftIconTint,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.9f)
                            .clickable {
                                onLeftIconClicked?.invoke()
                            }
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = title,
                    color = Light_Red,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
            }
        },
        actions = {
            if (rightIcon != null) {
                IconButton(onClick = {
                    onRightIconClicked?.invoke()
                }) {
                    Icon(
                        imageVector = rightIcon,
                        contentDescription = "Logout",
                        tint = rightIconTint
                    )
                }
            }
        }
    )
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

@Composable
fun TitleItem(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier.padding(start = 5.dp)) {
        Column {
            Text(
                text = label,
                fontSize = 19.sp,
                textAlign = TextAlign.Left,
                style = TextStyle(fontStyle = FontStyle.Normal, fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun ListCard(
    book: Book, cardRadius: Dp = 30.dp, onClickDetails: (String) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(cardRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClickDetails.invoke(book.title) }
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
                Image(
                    modifier = Modifier
                        .width(100.dp)
                        .height(140.dp)
                        .padding(4.dp),
                    painter = rememberAsyncImagePainter(model = book.photoURl),
                    contentDescription = "Book Image"
                )
                Spacer(modifier = Modifier.width(50.dp))
                Column(
                    modifier = Modifier
                        .wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon"
                    )
                    RatingItem(score = 3.5)
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = book.title.toString(),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.authors.toString(),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                modifier = Modifier.align(Alignment.End),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                RoundedButton("Reading", radius = 70)
            }
        }
    }
}

@Composable
fun RatingItem(score: Double) {
    Surface(
        modifier = Modifier.padding(top = 4.dp),
        shape = RoundedCornerShape(56.dp),
        shadowElevation = 6.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "Star",
                modifier = Modifier.padding(3.dp)
            )
            Text(text = score.toString(), style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun RoundedButton(label: String, radius: Int = 30, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEndPercent = radius,
                topStartPercent = radius
            )
        ),
        color = Color(0XFF92CBDF)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable { onClick.invoke() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = TextStyle(color = Color.White, fontSize = 15.sp))
        }
    }
}
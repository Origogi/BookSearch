package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.origogi.booksearch.R
import com.origogi.booksearch.dummyData
import com.origogi.booksearch.model.BookDetail
import com.origogi.booksearch.view.compose.LightGray
import com.origogi.booksearch.view.compose.MyTypography
import com.origogi.booksearch.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        viewModel.loadDataFromISBN(intent.getStringExtra("isbn")!!)

        setContent {
            DetailScreen(viewModel)
        }
    }
}

@Composable
fun DetailScreen(viewModel: DetailViewModel) {
    val bookDetail = viewModel.bookDetail.observeAsState().value
    val errorMessage = viewModel.errorMessage.observeAsState().value

    MaterialTheme(
        typography = MyTypography,
    ) {
        if (!errorMessage.isNullOrEmpty()) {
            ErrorPage(errorMessage)
        } else if (bookDetail != null) {
            DetailPage(bookDetail)

        } else {
            LoadingPage()
        }
    }
}

@Composable
fun LoadingPage() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()

    }

}

@Composable
fun ErrorPage(message: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Filled.Close, "", modifier = Modifier.size(50.dp))
        Text(
            text = message, style = typography.caption,
            textAlign = TextAlign.Center
        )
    }

}

@Composable
fun DetailPage(bookDetail: BookDetail) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                bookDetail.title,
                textAlign = TextAlign.Center,
                style = typography.h6,
                modifier = Modifier.padding(top = 16.dp)
            )
            Image(
                painter = rememberImagePainter(
                    data = bookDetail.image,
                    builder = {
                        crossfade(true)
                        size(OriginalSize)
                        scale(Scale.FIT)
                        placeholder(R.drawable.placeholder)
                    }
                ),
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(300f / 350f)
            )

            Text(
                "Author: ${bookDetail.authors}",
                textAlign = TextAlign.Center,
                style = typography.subtitle1
            )
            Text(
                bookDetail.year, textAlign = TextAlign.Center,
                style = typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )
            Surface(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = LightGray
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(vertical = 20.dp)
                ) {
                    Rating(bookDetail.rating)
                    Divider()
                    Pages(bookDetail.pages)
                    Divider()
                    Price(bookDetail.price)
                }
            }
            Descriptions(bookDetail.desc)

        }
    }

}

@Composable
fun Descriptions(desc: String) {
    val scrollState = rememberScrollState()

    Column {
        Text(
            text = "Description", style = typography.subtitle2
        )
        Text(
            desc, style = typography.body2,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 16.dp)
                .verticalScroll(scrollState)
        )
    }
}

@Composable
fun Price(price: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(price.replace("$", ""), style = typography.subtitle2)
        Text(
            " $", style = typography.caption,
        )
    }
}

@Composable
fun Pages(pages: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(pages, style = typography.subtitle2)
        Text(
            " pages", style = typography.caption
        )
    }
}

@Composable
private fun Rating(rating: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.star),
            modifier = Modifier.size(20.dp), contentDescription = ""
        )
        Text("${rating}.0", style = typography.subtitle2)
        Text(" /", style = typography.caption)
        Text(
            "5", style = typography.caption
        )
    }
}

@Composable
private fun Divider() {
    Divider(
        color = Color.Gray,
        modifier = Modifier
            .height(20.dp)
            .width(1.dp)
    )

}

@Composable
@Preview
fun DetailPagePreview() {
    DetailPage(dummyData)
}

@Composable
@Preview
fun ErrorPagePreview() {
    ErrorPage("error occured!!")
}

@Composable
@Preview
fun LoadingPreview() {
    LoadingPage()
}
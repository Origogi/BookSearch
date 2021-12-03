package com.origogi.booksearch.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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

    if (bookDetail != null) {
        DetailPage(bookDetail)
    }

}

@Composable
fun DetailPage(bookDetail: BookDetail) {
    MaterialTheme(
        typography = MyTypography,
    ) {
        Scaffold(
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    bookDetail.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h6
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
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    bookDetail.year, textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption

                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(7.dp)),
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
}

@Composable
fun Descriptions(desc: String) {

    Column {
        Text(
            text = "Description", style = MaterialTheme.typography.subtitle2
        )
        Text(desc, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun Price(price: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(price.replace("$", ""), style = MaterialTheme.typography.subtitle2)
        Text(
            " $", style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun Pages(pages: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(pages, style = MaterialTheme.typography.subtitle2)
        Text(
            " pages", style = MaterialTheme.typography.caption
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
        Text(rating, style = MaterialTheme.typography.subtitle2)
        Text(" /", style = MaterialTheme.typography.caption)
        Text(
            "5", style = MaterialTheme.typography.caption
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
fun DetailScreenPreview() {
    DetailPage(dummyData)
}

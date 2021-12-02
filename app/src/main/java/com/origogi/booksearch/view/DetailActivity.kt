package com.origogi.booksearch.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import coil.compose.rememberImagePainter
import com.origogi.booksearch.R
import com.origogi.booksearch.TAG
import com.origogi.booksearch.databinding.ActivityDetailBinding
import com.origogi.booksearch.databinding.ActivityMainBinding
import com.origogi.booksearch.dummyData
import com.origogi.booksearch.model.BookDetail
import com.origogi.booksearch.viewmodel.DetailViewModel
import com.origogi.booksearch.viewmodel.MainViewModel
import java.util.*

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
    MaterialTheme {
        Scaffold(
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Appbar()
                Text(bookDetail.title, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp))
                Image(
                    painter = rememberImagePainter(bookDetail.image),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(300.0f / 350.0f)
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                )
            }

        }
    }
}


@Composable
fun Appbar() {
    val activity = (LocalContext.current as? Activity)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = { activity?.finish() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "")
        }

    }
}


@Composable
@Preview
fun DetailScreenPreview() {

    DetailPage(dummyData)
}
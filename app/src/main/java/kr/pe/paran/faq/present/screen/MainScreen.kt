package kr.pe.paran.faq.present.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Preview(showBackground = true)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {

    val context = LocalContext.current
    val pageNo by viewModel.pageNo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp, horizontal = 32.dp)
    ) {
        Text(
            "KT FAQ Crawling...",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = pageNo == 0,
            onClick = {
                viewModel.startCrawling(context = context)
            }) {
            Text(text = if (pageNo > 0) "WAITING...($pageNo / 137)" else "START")
        }
    }
}

package com.mbj.upbit.feature.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mbj.upbit.feature.home.viewmodel.MainViewModel
import com.mbj.upbit.model.CoinInfo
import com.mbj.upbit.model.CoinInfo.Companion.coinInfoItems
import com.mbj.upbit.ui.theme.UpbitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel
        setContent {
            UpbitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FilterCoinInfo(coinInfoItems)
                }
            }
        }
    }
}

@Composable
fun FilterCoinInfo(coinInfoItems: List<CoinInfo>) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .height(20.dp)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = "한글명",
                    fontSize = 14.sp,
                )
                Column {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 50.dp)
            ) {
                Text(
                    text = "현재가",
                    fontSize = 14.sp,
                )
                Column {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "전일 대비",
                    fontSize = 14.sp,
                )
                Column {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 20.dp)
            ) {
                Text(
                    text = "거래대금",
                    fontSize = 14.sp,
                )
                Column {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
        }
        Divider(
            color = Color.DarkGray,
            thickness = 0.2.dp,
            modifier = Modifier.padding(top = 4.dp)
        )
        LazyColumn {
            items(coinInfoItems) { item ->
                CoinInfoItem(item)
            }
        }
    }
}

@Composable
fun CoinInfoItem(coinInfo: CoinInfo) {
    Spacer(modifier = Modifier.padding(top = 2.dp))
    Row(
        horizontalArrangement = Arrangement.Start,

        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(50.dp)
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(60.dp)
                .width(120.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.padding(end = 4.dp))
            Column(
                horizontalAlignment = Alignment.Start,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = coinInfo.name,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null
                    )
                }
                Text(
                    text = coinInfo.coinNameAbbreviation,
                    fontSize = 8.sp,
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(60.dp),
        ) {
            Text(
                text = coinInfo.currentPrice.toString(),
                fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.padding(end = 40.dp))

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${coinInfo.percentComparedPreviousDay}%",
                fontSize = 12.sp,
            )
            Text(
                text = "${coinInfo.moneyComparedPreviousDay}",
                fontSize = 10.sp,
            )
        }
        Spacer(modifier = Modifier.padding(end = 20.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${coinInfo.transactionPayment}백만",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    Spacer(modifier = Modifier.padding(bottom = 2.dp))
    Divider(
        color = Color.DarkGray,
        thickness = 0.2.dp,
    )
}

@Preview(showBackground = true)
@Composable
fun FilterCoinInfoPreview() {
    UpbitTheme {
        FilterCoinInfo(coinInfoItems)
    }
}

@Preview(showBackground = true)
@Composable
fun CoinInfoItemPreview() {
    UpbitTheme {
        CoinInfoItem(
            coinInfo = CoinInfo(
                name = "비트토렌트",
                coinNameAbbreviation = "BTT/KRW",
                currentPrice = 0.0006,
                percentComparedPreviousDay = 0.00,
                moneyComparedPreviousDay = 0.0000,
                transactionPayment = 171
            )
        )
    }
}

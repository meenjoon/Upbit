package com.mbj.upbit.feature.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mbj.upbit.R
import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.model.CoinInfoDetail
import com.mbj.upbit.data.remote.model.UpbitTickerResponse
import com.mbj.upbit.feature.home.viewmodel.MainViewModel
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter.calculateTextColor
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter.formatChangePrice
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter.formatChangeRate
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter.formatCurrentPrice
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter.formatMarketName
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter.formatTradePrice
import com.mbj.upbit.ui.theme.CustomColors.Companion.Grey500
import com.mbj.upbit.ui.theme.CustomColors.Companion.zeroChangeBoxColor
import com.mbj.upbit.ui.theme.UpbitTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UpbitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    FilterCoinInfo()
                }
            }
        }
    }
}

@Composable
fun FilterCoinInfo() {
    val viewModel: MainViewModel = hiltViewModel()

    val upbitTickerResponses by viewModel.upbitTickerResponses.collectAsState(emptyMap())
    val coinInfoList by viewModel.coinInfoList.collectAsState(emptyList())
    val combinedDataList = viewModel.combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .height(20.dp)
                .fillMaxSize()
                .padding(top = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.korean_name),
                    fontSize = 14.sp,
                    color = Grey500
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
                    text = stringResource(R.string.currency_price),
                    fontSize = 14.sp,
                    color = Grey500
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
                    text = stringResource(R.string.net_change),
                    fontSize = 14.sp,
                    color = Grey500
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
                    text = stringResource(R.string.trading_value),
                    fontSize = 14.sp,
                    color = Grey500
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
            items(combinedDataList) { combinedData ->
                CoinInfoItem(coinInfoDetail = combinedData)
            }
        }
    }
}

@Composable
fun CoinInfoItem(coinInfoDetail: CoinInfoDetail) {

    val viewModel: MainViewModel = hiltViewModel()

    val textColor = calculateTextColor(coinInfoDetail.upbitTickerResponse.signedChangeRate)

    var previousPrice by remember { mutableStateOf(coinInfoDetail.upbitTickerResponse.tradePrice) }
    val currentPrice = coinInfoDetail.upbitTickerResponse.tradePrice
    val (borderThickness, borderColor) = viewModel.calculateBorderInfo(currentPrice!!, previousPrice!!)
    val priceChanged = currentPrice != previousPrice

    if (priceChanged) {
        LaunchedEffect(priceChanged) {
            delay(400)
            previousPrice = currentPrice
        }
    }

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
                .width(110.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(zeroChangeBoxColor)
                    .width(10.dp)
                    .height(22.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(textColor)
                        .height(2.dp)
                )
            }
            Spacer(modifier = Modifier.padding(end = 4.dp))
            Column( //코인명
                horizontalAlignment = Alignment.Start,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = coinInfoDetail.coinInfo.koreanName,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                Text(
                    text = formatMarketName(coinInfoDetail.coinInfo.market),
                    fontSize = 8.sp,
                )
            }
        }
        Column( // 현재가
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(65.dp)
                .drawWithContent {
                    drawContent()
                    if (borderThickness > 0.dp) {
                        drawRect(
                            color = borderColor,
                            style = Stroke(width = borderThickness.toPx())
                        )
                    }
                }
        ) {
            Text(
                text = "${
                    coinInfoDetail.upbitTickerResponse.currentPrice?.let {
                        formatCurrentPrice(
                            it
                        )
                    }
                }",
                fontSize = 12.sp,
                color = textColor
            )
        }
        Spacer(modifier = Modifier.padding(end = 10.dp))

        Column( //전일대비
            horizontalAlignment = Alignment.End,
            modifier = Modifier.width(65.dp)
        ) {
            Text(
                text = "${formatChangeRate(coinInfoDetail.upbitTickerResponse.signedChangeRate)}%",
                fontSize = 12.sp,
                color = textColor
            )
            Text(
                text = "${formatChangePrice(coinInfoDetail.upbitTickerResponse.signedChangePrice)}",
                fontSize = 10.sp,
                color = textColor
            )
        }
        Spacer(modifier = Modifier.padding(end = 10.dp))

        Column( //거래대금
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${
                    coinInfoDetail.upbitTickerResponse.tradeVolumeInKRW?.let {
                        formatTradePrice(
                            it
                        )
                    }
                }",
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

@Composable
@Preview(showBackground = true)
fun FilterCoinInfoPreview() {
    UpbitTheme {
        FilterCoinInfo()
    }
}

@Preview(showBackground = true)
@Composable
fun CoinInfoItemPreview() {
    UpbitTheme {
        CoinInfoItem(
            coinInfoDetail = CoinInfoDetail(
                coinInfo = CoinInfo(
                    market = "KRW-BTC",
                    koreanName = "비트코인",
                    englishName = "Bitcoin"
                ),
                upbitTickerResponse = UpbitTickerResponse(
                    type = "ticker",
                    code = "KRW-BTC",
                    openingPrice = 3.6509E7,
                    highPrice = 3.6743E7,
                    lowPrice = 3.6319E7,
                    tradePrice = 3.6456E7,
                    prevClosingPrice = 3.65E7,
                    accTradePrice = 4.444947990314178E10,
                    change = "FALL",
                    changePrice = 44000.0,
                    signedChangePrice = -44000.0,
                    changeRate = 0.0012054795,
                    signedChangeRate = -0.0012054795,
                    askBid = "ASK",
                    tradeVolume = 0.05,
                    accTradeVolume = 1217.70517272,
                    tradeDate = "20230920",
                    tradeTime = "070428",
                    tradeTimestamp = 1695193468351,
                    accAskVolume = 657.65432189,
                    accBidVolume = 560.05085083,
                    highest52WeekPrice = 4.1569E7,
                    highest52WeekDate = "2023-06-30",
                    lowest52WeekPrice = 2.07E7,
                    lowest52WeekDate = "2022-12-30",
                    marketState = "ACTIVE",
                    isTradingSuspended = false,
                    delistingDate = null,
                    marketWarning = "NONE",
                    timestamp = 1695193470054,
                    accTradePrice24h = 1.2513542340670654E11,
                    accTradeVolume24h = 3430.84591112,
                    streamType = "REALTIME"
                )
            )
        )
    }
}

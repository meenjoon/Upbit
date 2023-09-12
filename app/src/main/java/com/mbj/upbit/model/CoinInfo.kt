package com.mbj.upbit.model

data class CoinInfo(
    val name: String,
    val coinNameAbbreviation: String,
    val currentPrice: Double,
    val percentComparedPreviousDay: Double,
    val moneyComparedPreviousDay: Double,
    val transactionPayment: Int
) {
    companion object {
        val coinInfoItems = listOf(
            CoinInfo(
                name = "비트토렌트",
                coinNameAbbreviation = "BTT/KRW",
                currentPrice = 0.0006,
                percentComparedPreviousDay = 0.00,
                moneyComparedPreviousDay = 0.0000,
                transactionPayment = 171
            ),
            CoinInfo(
                name = "시바이누",
                coinNameAbbreviation = "SHB/KRW",
                currentPrice = 0.0097,
                percentComparedPreviousDay = -3.96,
                moneyComparedPreviousDay = -0.0004,
                transactionPayment = 1987
            ),
            CoinInfo(
                name = "이캐시",
                coinNameAbbreviation = "XEC/KRW",
                currentPrice = 0.0301,
                percentComparedPreviousDay = -3.22,
                moneyComparedPreviousDay = -0.0010,
                transactionPayment = 3570
            ),
            CoinInfo(
                name = "무비블록",
                coinNameAbbreviation = "MBL/KRW",
                currentPrice = 3.46,
                percentComparedPreviousDay = -3.62,
                moneyComparedPreviousDay = -0.1300,
                transactionPayment = 1151
            ),
            CoinInfo(
                name = "시아코인",
                coinNameAbbreviation = "SC/KRW",
                currentPrice = 3.64,
                percentComparedPreviousDay = -4.21,
                moneyComparedPreviousDay = 0.1600,
                transactionPayment = 1540
            ),
            CoinInfo(
                name = "엠블",
                coinNameAbbreviation = "MVL/KRW",
                currentPrice = 3.79,
                percentComparedPreviousDay = -3.32,
                moneyComparedPreviousDay = -0.1300,
                transactionPayment = 638
            ),
            CoinInfo(
                name = "썬더코어",
                coinNameAbbreviation = "TT/KRW",
                currentPrice = 4.50,
                percentComparedPreviousDay = 4.46,
                moneyComparedPreviousDay = -0.2100,
                transactionPayment = 2075
            ),
            CoinInfo(
                name = "캐리프로토콜",
                coinNameAbbreviation = "CRE/KRW",
                currentPrice = 4.90,
                percentComparedPreviousDay = -5.04,
                moneyComparedPreviousDay = -0.2600,
                transactionPayment = 3949
            ),
            CoinInfo(
                name = "아이큐",
                coinNameAbbreviation = "IQ/KRW",
                currentPrice = 5.65,
                percentComparedPreviousDay = -3.91,
                moneyComparedPreviousDay = -0.2300,
                transactionPayment = 3191
            ),
            CoinInfo(
                name = "스톰엑스",
                coinNameAbbreviation = "STMAX/KRW",
                currentPrice = 6.16,
                percentComparedPreviousDay = -4.64,
                moneyComparedPreviousDay = 0.3000,
                transactionPayment = 6013
            ),

            CoinInfo(
                name = "리퍼리움",
                coinNameAbbreviation = "RFR/KRW",
                currentPrice = 7.37,
                percentComparedPreviousDay = -6.59,
                moneyComparedPreviousDay = -0.5200,
                transactionPayment = 21726
            ),

            CoinInfo(
                name = "아하토큰",
                coinNameAbbreviation = "AHT/KRW",
                currentPrice = 8.59,
                percentComparedPreviousDay = -5.19,
                moneyComparedPreviousDay = -0.4700,
                transactionPayment = 37022
            ),
        )
    }
}

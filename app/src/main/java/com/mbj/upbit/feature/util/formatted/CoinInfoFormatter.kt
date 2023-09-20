package com.mbj.upbit.feature.util.formatted

import kotlin.math.round

object CoinInfoFormatter {

    fun formatCurrentPrice(value: Double): String {
        val formattedValue = String.format("%,.0f", value)
        return formattedValue
    }

    fun formatChangeRate(value: Double?): Double {
        if (value != null) {
            val roundedValue = (round(value * 100000.0) / 1000.0)

            return if (roundedValue != roundedValue.toInt().toDouble()) {
                (round(roundedValue * 100) / 100.0)
            } else {
                roundedValue
            }
        }
        return 0.0
    }

    fun formatChangePrice(value: Double?): String? {
        return if (value != null) {
            val absValue = Math.abs(value)

            val formattedValue = if (absValue >= 1000) {
                String.format("%,.0f", absValue)
            } else {
                String.format("%.2f", absValue)
            }

            val result = if (absValue >= 100 && absValue - absValue.toInt() == 0.0) {
                String.format("%,.0f", absValue)
            } else {
                formattedValue
            }

            if (value < 0) {
                "-$result"
            } else {
                result
            }
        } else {
            null
        }
    }

    fun formatTradePrice(value: Double): String {
        val millionValue = (value / 1_000_000).toInt()
        val formattedValue = millionValue.toString().reversed().chunked(3).joinToString(",").reversed()
        return "${formattedValue}백만"
    }

    fun formatMarketName(inputMarket: String): String {
        val parts = inputMarket.split("-")
        if (parts.size == 2) {
            return "${parts[1]}/${parts[0]}"
        }
        return inputMarket
    }
}

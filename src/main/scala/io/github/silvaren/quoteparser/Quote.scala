package io.github.silvaren.quoteparser

import com.github.nscala_time.time.Imports._

sealed trait Quote {
  def symbol: String
  def date: DateTime
  def openPrice: BigDecimal
  def highPrice: BigDecimal
  def lowPrice: BigDecimal
  def closePrice: BigDecimal
  def tradedVolume: Long
  def trades: Long
}

final case class StockQuote(symbol: String, date: DateTime, openPrice: BigDecimal, highPrice: BigDecimal,
                      lowPrice: BigDecimal, closePrice: BigDecimal, tradedVolume: Long, trades: Long) extends Quote
final case class OptionQuote(symbol: String, date: DateTime, openPrice: BigDecimal, highPrice: BigDecimal,
                       lowPrice: BigDecimal, closePrice: BigDecimal, tradedVolume: Long, trades: Long,
                       strikePrice: BigDecimal, exerciseDate: DateTime) extends Quote
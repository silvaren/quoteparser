package io.github.silvaren.quoteparser

import com.github.nscala_time.time.Imports._

trait Quote {
  def stockSymbol: String
  def date: DateTime
  def openPrice: BigDecimal
  def highPrice: BigDecimal
  def lowPrice: BigDecimal
  def closePrice: BigDecimal
  def tradedVolume: Long
  def trades: Long
}

case class StockQuote(stockSymbol: String, date: DateTime, openPrice: BigDecimal, highPrice: BigDecimal,
                      lowPrice: BigDecimal, closePrice: BigDecimal, tradedVolume: Long, trades: Long) extends Quote
case class OptionQuote(stockSymbol: String, date: DateTime, openPrice: BigDecimal, highPrice: BigDecimal,
                       lowPrice: BigDecimal, closePrice: BigDecimal, tradedVolume: Long, trades: Long,
                       strikePrice: BigDecimal, exerciseDate: DateTime) extends Quote
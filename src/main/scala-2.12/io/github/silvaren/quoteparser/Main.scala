package io.github.silvaren.quoteparser

import com.github.nscala_time.time.Imports._

object Main {
  val formatter = DateTimeFormat.forPattern("yyyyMMdd").withZone(DateTimeZone.forID("America/Sao_Paulo"))

  def priceParser(strLine: String, integerStart: Int): BigDecimal = {
    val priceStr = strLine.substring(integerStart, integerStart + 11) + '.' +
      strLine.substring(integerStart + 11, integerStart + 13)
    BigDecimal(priceStr)
  }

  def dateParser(strLine: String, dateStart: Int): DateTime = {
    val parsedDate = formatter.parseDateTime(strLine.substring(dateStart,dateStart + 8))
    parsedDate.withHour(18)
  }

  def isOption(symbol: String): Boolean = {
    val symbolPrefix = symbol.substring(0, 4)
    //matches all symbols with up to four letters plus one A to X letter and plus at least one digit at the end.
    val optionMatcher = s"ˆ${symbolPrefix}[A-X][\\d]+$$".r
    optionMatcher.findFirstIn(symbol).isDefined
  }

  def main(args: Array[String]) {
    val sc = new java.util.Scanner (System.in)

    sc.nextLine() // skip first line

    val quote = sc.nextLine()
    val stockSymbol = quote.substring(12,24).trim
    val date = dateParser(quote, 2)
    val closePrice = priceParser(quote, 108)
    val highPrice = priceParser(quote, 69)
    val lowPrice = priceParser(quote, 82)
    val openPrice = priceParser(quote, 56)
    val tradedVolume = quote.substring(152, 170).toInt
    val trades = quote.substring(147, 152).toInt

    if (isOption(stockSymbol)) {
      val exerciseDate = dateParser(quote, 202);
      val strikePrice = priceParser(quote, 188);
      println("OPTION",stockSymbol, strikePrice, exerciseDate, date,
        openPrice, highPrice, lowPrice, closePrice, tradedVolume, trades)
    }
    else {
      println(stockSymbol, date, openPrice, highPrice, lowPrice, closePrice, tradedVolume, trades)
    }

  }

}

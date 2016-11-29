package io.github.silvaren.quoteparser

import java.io.InputStream

import com.github.nscala_time.time.Imports._

import scala.annotation.tailrec

object QuoteParser {
  val formatter = DateTimeFormat.forPattern("yyyyMMdd").withZone(DateTimeZone.forID("America/Sao_Paulo"))

  private[this] def priceParser(strLine: String, integerStart: Int): BigDecimal = {
    val priceStr = strLine.substring(integerStart, integerStart + 11) + '.' +
      strLine.substring(integerStart + 11, integerStart + 13)
    BigDecimal(priceStr)
  }

  private[this] def dateParser(strLine: String, dateStart: Int): DateTime = {
    val parsedDate = formatter.parseDateTime(strLine.substring(dateStart,dateStart + 8))
    parsedDate.withHour(18)
  }

  private[this] def isOption(symbol: String): Boolean = {
    val symbolPrefix = symbol.substring(0, 4)
    // matches all symbols with up to four letters plus one A to X letter and plus at least one digit at the end.
    val optionMatcher = s"^$symbolPrefix[A-X][\\d]+$$".r
    optionMatcher.findFirstIn(symbol).isDefined
  }

  def parseLine(quote: String): Quote = {
    val stockSymbol = quote.substring(12, 24).trim
    val date = dateParser(quote, 2)
    val closePrice = priceParser(quote, 108)
    val highPrice = priceParser(quote, 69)
    val lowPrice = priceParser(quote, 82)
    val openPrice = priceParser(quote, 56)
    val tradedVolume = quote.substring(152, 170).toLong
    val trades = quote.substring(147, 152).toLong

    if (isOption(stockSymbol)) {
      val exerciseDate = dateParser(quote, 202)
      val strikePrice = priceParser(quote, 188)
      OptionQuote(stockSymbol, date, openPrice, highPrice, lowPrice, closePrice, tradedVolume,
        trades, strikePrice, exerciseDate)
    }
    else {
      StockQuote(stockSymbol, date, openPrice, highPrice, lowPrice, closePrice, tradedVolume, trades)
    }
  }

  @tailrec
  private[this] def parseStream(sc: java.util.Scanner, acc: Seq[Quote]): Seq[Quote] = {
    if (sc.hasNext) {
      val quote = sc.nextLine()
      if (sc.hasNext) { // this checks that we are not parsing the last line which does not contains quotes
      val parsedQuote = parseLine(quote)
        parseStream(sc, parsedQuote +: acc)
      } else acc
    } else acc
  }

  def parse(inputStream: InputStream): Seq[Quote] = {
    val sc = new java.util.Scanner(inputStream)
    sc.nextLine() //skip first line
    parseStream(sc, Seq()).reverse
  }

}

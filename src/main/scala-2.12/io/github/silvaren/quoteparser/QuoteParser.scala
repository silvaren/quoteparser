package io.github.silvaren.quoteparser

import com.github.nscala_time.time.Imports._

import scala.annotation.tailrec

object QuoteParser {
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
    // matches all symbols with up to four letters plus one A to X letter and plus at least one digit at the end.
    val optionMatcher = s"^$symbolPrefix[A-X][\\d]+$$".r
    optionMatcher.findFirstIn(symbol).isDefined
  }

  @tailrec
  def loopStream(sc: java.util.Scanner, acc: List[Quote]): List[Quote] = {
    if (sc.hasNext) {
      val quote = sc.nextLine()
      if (sc.hasNext) { // this checks that we are not parsing the last line which does not contains quotes
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
          val optionQuote = OptionQuote(stockSymbol, date, openPrice, highPrice, lowPrice, closePrice, tradedVolume,
            trades, strikePrice, exerciseDate)
          loopStream(sc, optionQuote :: acc)
        }
        else {
          val stockQuote = StockQuote(stockSymbol, date, openPrice, highPrice, lowPrice, closePrice, tradedVolume, trades)
          loopStream(sc, stockQuote :: acc)
        }
      } else acc
    } else acc
  }

  def main(args: Array[String]) {
    val sc = new java.util.Scanner (System.in)

    sc.nextLine() //skip first line

    val quotes = loopStream(sc, List())
    quotes.foreach{ case q: OptionQuote => println(q); case _ => }
  }

}

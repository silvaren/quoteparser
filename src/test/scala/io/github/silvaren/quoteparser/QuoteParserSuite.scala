package io.github.silvaren.quoteparser

import java.io.ByteArrayInputStream

import org.joda.time.{DateTime, DateTimeZone}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QuoteParserSuite extends FunSuite {

  val StockLine: String = "012015010202PETR4       010PETROBRAS   PN           R$  000000000099900000000009990000"+
    "000000936000000000095700000000009360000000000936000000000094039738000000000048837200000000046754792500000000"+
    "000000009999123100000010000000000000BRPETRACNPR6184"

  val OptionLine: String = "012015010878PETRA10     070PETR        PN        000R$  0000000000057000000000009700000"+
    "0000005000000000000720000000000082000000000008100000000000821429100000000004449220000000000321699010000000000"+
    "0086102015011900000010000000000000BRPETRACNPR6184"

  val ExpectedStockQuote = StockQuote("PETR4", buildDate(2015, 1, 2), BigDecimal("9.99"), BigDecimal("9.99"),
    BigDecimal("9.36"), BigDecimal("9.36"), 48837200, 39738)

  val ExpectedOptionQuote = OptionQuote("PETRA10", buildDate(2015, 1, 8), BigDecimal("0.57"), BigDecimal("0.97"),
    BigDecimal("0.50"), BigDecimal("0.82"), 44492200, 14291, BigDecimal("8.61"), buildDate(2015, 1, 19))

  def buildDate(year: Int, month: Int, day: Int): DateTime = {
    val d = new DateTime()
    d.withZone(DateTimeZone.forID("America/Sao_Paulo"))
      .withYear(year)
      .withMonthOfYear(month)
      .withDayOfMonth(day)
      .withHourOfDay(18)
      .withMinuteOfHour(0)
      .withSecondOfMinute(0)
      .withMillisOfSecond(0)
  }

  test("stock quote is correctly parsed") {
    val parsedQuote = QuoteParser.parseLine(StockLine, Set(10))

    assert(parsedQuote == Some(ExpectedStockQuote))
  }

  test("option quote is correctly parsed") {
    val parsedQuote = QuoteParser.parseLine(OptionLine, Set(70))

    assert(parsedQuote == Some(ExpectedOptionQuote))
  }

  test("parsing is correctly ignoring first and last lines of the stream") {
    val quoteStream = s"00COTAHIST.2015BOVESPA 20151230\n${StockLine}\n${OptionLine}\n" +
      "99COTAHIST.2015BOVESPA 2015123000000414179"

    val inputStream = new ByteArrayInputStream(quoteStream.getBytes())
    val parsedQuotes = QuoteParser.parse(inputStream, Set(10, 70))
    assert(parsedQuotes == Seq(ExpectedStockQuote, ExpectedOptionQuote))
  }

  test("parsing is correctly ignoring non selected markets") {
    val quoteStream = s"00COTAHIST.2015BOVESPA 20151230\n${StockLine}\n${OptionLine}\n" +
      "99COTAHIST.2015BOVESPA 2015123000000414179"

    val inputStream = new ByteArrayInputStream(quoteStream.getBytes())
    val parsedQuotes = QuoteParser.parse(inputStream, Set(70))
    assert(parsedQuotes == Seq(ExpectedOptionQuote))
  }

}

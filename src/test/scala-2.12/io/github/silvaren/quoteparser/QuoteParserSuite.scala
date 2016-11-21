package io.github.silvaren.quoteparser

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class QuoteParserSuite extends FunSuite {

  test("stock quote is correctly parsed") {
    val quoteLine: String = "012015010202PETR4       010PETROBRAS   PN           R$  000000000099900000000009990000"+
      "000000936000000000095700000000009360000000000936000000000094039738000000000048837200000000046754792500000000"+
      "000000009999123100000010000000000000BRPETRACNPR6184"
    val date = {
      val d = new DateTime()
      d.withZone(DateTimeZone.forID("America/Sao_Paulo"))
        .withYear(2015)
        .withMonthOfYear(1)
        .withDayOfMonth(2)
        .withHourOfDay(18)
        .withMinuteOfHour(0)
        .withSecondOfMinute(0)
        .withMillisOfSecond(0)
    }

    val expectedQuote = StockQuote("PETR4", date, BigDecimal("9.99"), BigDecimal("9.99"), BigDecimal("9.36"),
      BigDecimal("9.36"), 48837200, 39738)

    val parsedQuote = QuoteParser.parseLine(quoteLine)
    assert(parsedQuote == expectedQuote)
  }

  test("option quote is correctly parsed") {
    val quoteLine: String = "012015010878PETRA10     070PETR        PN        000R$  0000000000057000000000009700000"+
      "0000005000000000000720000000000082000000000008100000000000821429100000000004449220000000000321699010000000000"+
      "0086102015011900000010000000000000BRPETRACNPR6184"
    val date = {
      val d = new DateTime()
      d.withZone(DateTimeZone.forID("America/Sao_Paulo"))
        .withYear(2015)
        .withMonthOfYear(1)
        .withDayOfMonth(8)
        .withHourOfDay(18)
        .withMinuteOfHour(0)
        .withSecondOfMinute(0)
        .withMillisOfSecond(0)
    }
    val exerciseDate = {
      val d = new DateTime()
      d.withZone(DateTimeZone.forID("America/Sao_Paulo"))
        .withYear(2015)
        .withMonthOfYear(1)
        .withDayOfMonth(19)
        .withHourOfDay(18)
        .withMinuteOfHour(0)
        .withSecondOfMinute(0)
        .withMillisOfSecond(0)
    }
    val expectedQuote = OptionQuote("PETRA10", date, BigDecimal("0.57"), BigDecimal("0.97"), BigDecimal("0.50"),
      BigDecimal("0.82"), 44492200, 14291, BigDecimal("8.61"), exerciseDate)

    val parsedQuote = QuoteParser.parseLine(quoteLine)
    assert(parsedQuote == expectedQuote)
  }

}

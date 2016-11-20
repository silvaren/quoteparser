package io.github.silvaren

object Main {

  def priceParser(strLine: String, integerStart: Int): BigDecimal = {
    val priceStr = strLine.substring(integerStart, integerStart + 11) + '.' +
      strLine.substring(integerStart + 11, integerStart + 13)
    BigDecimal(priceStr)
  }

  def main(args: Array[String]) {
    val sc = new java.util.Scanner (System.in)

    sc.nextLine() // skip first line

    val quote = sc.nextLine()
    val stockSymbol = quote.substring(12,24).trim
    println(stockSymbol)

    val closePrice = priceParser(quote, 108)
    val highPrice = priceParser(quote, 69)
    val lowPrice = priceParser(quote, 82)
    val openPrice = priceParser(quote, 56)
    val tradedVolume = quote.substring(152, 170).toInt
    val trades = quote.substring(147, 152).toInt
    println(openPrice, highPrice, lowPrice, closePrice, tradedVolume, trades)
  }

}

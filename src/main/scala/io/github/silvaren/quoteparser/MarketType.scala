package io.github.silvaren.quoteparser

sealed abstract class MarketType (val code: Int)

case object Regular extends MarketType(10)
case object CallOptionExercise extends MarketType(12)
case object PutOptionExercise extends MarketType(13)
case object Bid extends MarketType(17)
case object Fractionary extends MarketType(20)
case object Term extends MarketType(30)
case object FutureWithGainRetention extends MarketType(50)
case object FutureWithContinuousMovement extends MarketType(60)
case object CallOption extends MarketType(70)
case object PutOption extends MarketType(80)

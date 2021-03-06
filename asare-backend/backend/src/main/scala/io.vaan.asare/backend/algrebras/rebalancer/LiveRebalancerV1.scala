package io.vaan.asare.backend.algrebras.rebalancer

import io.vaan.asare.backend.domain.rebalance._
import cats._
import cats.effect.Sync
import cats.syntax.all._

class LiveRebalancerV1[F[_]: Sync] private[rebalancer] () extends RebalancerA[F] {
  override def calcCurrentAllocation(portfolio: Portfolio): F[Portfolio] =
    F.delay {
      val sum = portfolio.values.sum
      portfolio.map {
        case (ticker: String, value: Double) => (ticker, value / sum.toDouble * 100)
      }
    }

  override def calcExpectedPortfolio(rebalanceInput: RebalanceInput): F[Portfolio] =
    F.delay(
      rebalanceInput.requiredAllocation.map {
        case (ticker: String, value: Double) =>
          rebalanceInput.target match {
            case Some(targetValue) => (ticker, value / 100 * targetValue)
            case None              => (ticker, value / 100 * rebalanceInput.currentPortfolio.values.sum)
          }
      }
    )

  override def calcPurchase(rebalanceInput: RebalanceInput): F[Portfolio] =
    F.map(calcExpectedPortfolio(rebalanceInput)) { expectedPortfolio =>
      expectedPortfolio.map {
        case (ticker: String, value: Double) =>
          (ticker, value - rebalanceInput.currentPortfolio(ticker))
      }
    }
}

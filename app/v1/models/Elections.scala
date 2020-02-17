package v1.models

trait Elections {

  case object GroupRatioBlended extends WithName("groupRatioBlended") with Elections
  case object GroupEBITDA extends WithName("groupEBITDA") with Elections
  case object InterestAllowanceAlternativeCalculation extends WithName("interestAllowanceAlternativeCalculation") with Elections
  case object InterestAllowanceNonConsolidatedInvestment extends WithName("interestAllowanceNonConsolidatedInvestment") with Elections
  case object InterestAllowanceConsolidatedPartnership extends WithName("interestAllowanceConsolidatedPartnership") with Elections

  val allValues = Seq(
    GroupRatioBlended,
    GroupEBITDA,
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  val fixedRatioValues = Seq(
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )
}

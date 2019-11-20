package schemas

import java.time.LocalDate

import models.SubmissionType
import play.api.libs.json.Json

trait FullSchemaBaseSpec extends BaseSchemaSpec {

  case class AdjustedGroupInterest(qngie: Option[BigDecimal],
                                   groupEBITDA: Option[BigDecimal],
                                   groupRatio: Option[BigDecimal])

  object AdjustedGroupInterest {
    implicit val format = Json.format[AdjustedGroupInterest]
  }

  case class AllocatedReactivations(ap1NetDisallowances: Option[Int],
                                    currentPeriodReactivation: Option[Int],
                                    totalReactivation: Option[Int],
                                    reactivationCap: Option[Int])

  object AllocatedReactivations {
    implicit val format = Json.format[AllocatedReactivations]
  }

  case class AllocatedRestrictions(ap1End: Option[LocalDate],
                                   ap2End: Option[LocalDate],
                                   ap3End: Option[LocalDate],
                                   disallowanceAp1: Option[BigDecimal],
                                   disallowanceAp2: Option[BigDecimal],
                                   disallowanceAp3: Option[BigDecimal])

  object AllocatedRestrictions {
    implicit val format = Json.format[AllocatedRestrictions]
  }

  case class GroupLevelAmount(totalDisallowedAmount: Option[BigDecimal],
                              interestReactivationCap: Option[BigDecimal],
                              interestAllowanceForward: Option[BigDecimal],
                              interestAllowanceForPeriod: Option[BigDecimal],
                              interestCapacityForPeriod: Option[BigDecimal])

  object GroupLevelAmount {
    implicit val format = Json.format[GroupLevelAmount]
  }

  case class UkCompanyModel(companyName: Option[String],
                            utr: Option[String],
                            consenting: Option[Boolean],
                            netTaxInterestExpense: Option[Int],
                            netTaxInterestIncome: Option[Int],
                            taxEBITDA: Option[Int],
                            allocatedRestrictions: Option[AllocatedRestrictions],
                            allocatedReactivations: Option[AllocatedReactivations])

  object UkCompanyModel {
    implicit val format = Json.format[UkCompanyModel]
  }

  case class FullReturnModel(agentDetails: AgentDetails,
                             reportingCompany: ReportingCompany,
                             parentCompany: ParentCompany,
                             publicInfrastructure: Boolean,
                             groupCompanyDetails: GroupCompanyDetails,
                             submissionType: SubmissionType,
                             revisedReturnDetails: Option[String],
                             groupLevelElections: Option[GroupLevelElections],
                             ukCompanies: Seq[UkCompanyModel],
                             angie: Option[BigDecimal],
                             groupSubjectToInterestRestrictions: Boolean,
                             groupSubjectToInterestReactivation: Boolean,
                             groupLevelAmount: GroupLevelAmount,
                             adjustedGroupInterest: AdjustedGroupInterest)

  object FullReturnModel {
    implicit val format = Json.format[FullReturnModel]
  }













}

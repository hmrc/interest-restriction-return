/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package schemas.subSchemas

import play.api.libs.json.{JsValue, Json}
import schemas.BaseSchemaSpec

class ParentCompanySchemaSpec extends BaseSchemaSpec {

  def validate(json: JsValue): Boolean =
    validateJson("subSchemas/parentCompany.json", json)


  "ParentCompany Json Schema" should {

    "Return valid" when {

      "Validated a successful JSON payload with UK Parent company" in {

        val json = Json.toJson(ParentCompany())

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with NonUK Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(NonUkCompany())
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with Deemed Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(DeemedParent()))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with 3 Deemed Parent companies" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(
            DeemedParent(), DeemedParent(), DeemedParent()
          ))
        ))

        validate(json) shouldBe true
      }

      //TODO undecided
      "Validated a successful JSON with a Deemed Parent company with no UTR" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, Some(Seq(
            DeemedParent(utr = None)
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when otherUkTaxReference is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UkCompany(
            otherUkTaxReference = None
          ))
        ))

        validate(json) shouldBe true
      }


      "Validated a successful JSON payload when knownAs is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UkCompany(
            knownAs = None
          ))
        ))

        validate(json) shouldBe true
      }

    }
    "parent Company" when {

      "ultimateParent" when {

        "Uk Company" when {

          "company name" when {

            "is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkCompany(
                  registeredCompanyName = None
                ))
              ))

              validate(json) shouldBe false
            }

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkCompany(
                  registeredCompanyName = Some("")
                ))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkCompany(
                  registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))
                ))
              ))

              validate(json) shouldBe false
            }
          }

          "utr" when {

            s"below $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(utr = Some("1" * (utrLength - 1)))
                ))
              )
              validate(json) shouldBe false
            }

            s"above $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(utr = Some("1" * (utrLength + 1)))
                ))
              )

              validate(json) shouldBe false
            }

            "is non numeric" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(utr = Some("a" * utrLength))
                ))
              )

              validate(json) shouldBe false
            }

            "is a symbol" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(utr = Some("@"))
                ))
              )
              validate(json) shouldBe false
            }
          }

          "crn" when {

            s"below $crnLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(crn = Some("1" * (crnLength - 1))
                  )
                ))
              )
              validate(json) shouldBe false
            }

            s"above $crnLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(crn = Some("1" * (crnLength + 1)))
                ))
              )

              validate(json) shouldBe false
            }

            "starts with 1 letter" in {
              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(crn = Some("A" + ("1" * (crnLength - 1))))
                ))
              )
              validate(json) shouldBe false
            }

            "starts with 3 letters" in {
              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(crn = Some("AAA" + ("1" * (crnLength - 3))))
                ))
              )
              validate(json) shouldBe false

            }
          }

          "otherUkTaxReference" when {

            s"below $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(otherUkTaxReference = Some("1" * (utrLength - 1)))
                ))
              )
              validate(json) shouldBe false
            }

            s"above $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(otherUkTaxReference = Some("1" * (utrLength + 1)))
                ))
              )
              validate(json) shouldBe false
            }

            "is non numeric" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(otherUkTaxReference = Some("a" * utrLength))
                )
              ))

              validate(json) shouldBe false
            }

            "is a symbol" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkCompany(otherUkTaxReference = Some("@"))

                ))
              )

              validate(json) shouldBe false
            }
          }

          "knownAs" when {

            "knownAs is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkCompany(
                  knownAs = Some("")
                ))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkCompany(
                  knownAs = Some("A" * (maxCompanyNameLength + 1))
                ))
              ))

              validate(json) shouldBe false
            }
          }
          "non-uk company" when {

            "company name" when {

              "is None" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    registeredCompanyName = None
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is empty" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    registeredCompanyName = Some("")
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is longer than $maxCompanyNameLength characters" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            "countryOfIncorporation" when {

              "is only one letter" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    countryOfIncorporation = Some("A")
                  ))
                ))
                validate(json) shouldBe false
              }

              "is three letters" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    countryOfIncorporation = Some("AAA")
                  ))
                ))
                validate(json) shouldBe false
              }

              "contains a number" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    countryOfIncorporation = Some("A1")
                  ))
                ))
                validate(json) shouldBe false
              }

              "contains a symbol" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    countryOfIncorporation = Some("A@")
                  ))
                ))
                validate(json) shouldBe false
              }
            }

            "non-uk crn" when {

              "is None" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    crn = None
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is empty" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    crn = Some("")
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            "knownAs" when {

              "knownAs is empty" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = Some(NonUkCompany(
                    knownAs = Some("")
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkCompany(
                  knownAs = Some("A" * (maxCompanyNameLength + 1))
                ))
              ))
              validate(json) shouldBe false
            }
          }

          "deemedParent" when {

            "company name" when {

              "is None" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(
                      companyName = None
                    )
                  )))
                )

                validate(json) shouldBe false
              }

              s"is empty" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(
                      companyName = Some("")
                    )
                  ))
                ))

                validate(json) shouldBe false
              }

              s"is longer than $maxCompanyNameLength characters" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(
                      companyName = Some("A" * (maxCompanyNameLength + 1))
                    )
                  ))
                ))
                validate(json) shouldBe false
              }
            }

            "utr" when {

              s"below $utrLength" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("1" * (utrLength - 1)))
                  ))
                ))
                validate(json) shouldBe false
              }

              s"above $utrLength" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("1" * (utrLength + 1)))
                  ))
                ))

                validate(json) shouldBe false
              }

              "is non numeric" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("a" * utrLength))
                  ))
                ))

                validate(json) shouldBe false
              }

              "is a symbol" in {

                val json = Json.toJson(ParentCompany(
                  ultimateParent = None, Some(Seq(
                    DeemedParent(utr = Some("@"))
                  ))
                ))

                validate(json) shouldBe false
              }
            }

            "deemed parents and ultimate parent is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = None, deemedParent = None
              ))

              validate(json) shouldBe false
            }

            "deemed parents is an empty list and ultimate parent is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = None, deemedParent = Some(Seq.empty))
              )

              validate(json) shouldBe false
            }

            "more than 3 deemed parents and ultimate parent is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = None,
                Some(Seq(DeemedParent(), DeemedParent(), DeemedParent(), DeemedParent())))
              )

              validate(json) shouldBe false
            }
          }
        }
      }
    }
  }
}
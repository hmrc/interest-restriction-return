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
import schemas.helpers._

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
          ultimateParent = Some(NonUkUltimateParent())
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with Deemed Parent company" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(UkDeemedParent()))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload with 3 Deemed Parent companies" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, deemedParent = Some(Seq(
            UkDeemedParent(), NonUkDeemedParent(), UkDeemedParent()
          ))
        ))

        validate(json) shouldBe true
      }

      //TODO undecided
      "Validated a successful JSON with a Deemed Parent company with no UTR" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = None, Some(Seq(
            UkDeemedParent(ctutr = None)
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when sautr is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UkUltimateParent(
            sautr = None
          ))
        ))

        validate(json) shouldBe true
      }

      "Validated a successful JSON payload when ctutr is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UkUltimateParent(
            ctutr = None
          ))
        ))

        validate(json) shouldBe true
      }


      "Validated a successful JSON payload when knownAs is none" in {

        val json = Json.toJson(ParentCompany(
          ultimateParent = Some(UkUltimateParent(
            knownAs = None
          ))
        ))

        validate(json) shouldBe true
      }

    }

    "Return invalid" when {

      "ultimateParent" when {

        "Uk Company" when {

          "company name" when {

            "is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkUltimateParent(
                  registeredCompanyName = None
                ))
              ))

              validate(json) shouldBe false
            }

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkUltimateParent(
                  registeredCompanyName = Some("")
                ))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkUltimateParent(
                  registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))
                ))
              ))

              validate(json) shouldBe false
            }
          }

          "ctutr" when {

            s"below $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(ctutr = Some("1" * (utrLength - 1)))
                ))
              )
              validate(json) shouldBe false
            }

            s"above $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(ctutr = Some("1" * (utrLength + 1)))
                ))
              )

              validate(json) shouldBe false
            }

            "is non numeric" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(ctutr = Some("a" * utrLength))
                ))
              )

              validate(json) shouldBe false
            }

            "is a symbol" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(ctutr = Some("@"))
                ))
              )
              validate(json) shouldBe false
            }
          }

          "crn" when {

            s"below $crnLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(crn = Some("1" * (crnLength - 1))
                  )
                ))
              )
              validate(json) shouldBe false
            }

            s"above $crnLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(crn = Some("1" * (crnLength + 1)))
                ))
              )

              validate(json) shouldBe false
            }

            "starts with 1 letter" in {
              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(crn = Some("A" + ("1" * (crnLength - 1))))
                ))
              )
              validate(json) shouldBe false
            }

            "starts with 3 letters" in {
              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(crn = Some("AAA" + ("1" * (crnLength - 3))))
                ))
              )
              validate(json) shouldBe false

            }
          }

          "sautr" when {

            s"below $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(sautr = Some("1" * (utrLength - 1)))
                ))
              )
              validate(json) shouldBe false
            }

            s"above $utrLength" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(sautr = Some("1" * (utrLength + 1)))
                ))
              )
              validate(json) shouldBe false
            }

            "is non numeric" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(sautr = Some("a" * utrLength))
                )
              ))

              validate(json) shouldBe false
            }

            "is a symbol" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(
                  UkUltimateParent(sautr = Some("@"))

                ))
              )

              validate(json) shouldBe false
            }
          }

          "knownAs" when {

            "knownAs is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkUltimateParent(
                  knownAs = Some("")
                ))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(UkUltimateParent(
                  knownAs = Some("A" * (maxCompanyNameLength + 1))
                ))
              ))

              validate(json) shouldBe false
            }
          }
        }

        "non-uk company" when {

          "company name" when {

            "is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  registeredCompanyName = None
                ))
              ))

              validate(json) shouldBe false
            }

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  registeredCompanyName = Some("")
                ))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  registeredCompanyName = Some("A" * (maxCompanyNameLength + 1))
                ))
              ))

              validate(json) shouldBe false
            }
          }

          "countryOfIncorporation" when {

            "is only one letter" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  countryOfIncorporation = Some("A")
                ))
              ))
              validate(json) shouldBe false
            }

            "is three letters" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  countryOfIncorporation = Some("AAA")
                ))
              ))
              validate(json) shouldBe false
            }

            "contains a number" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  countryOfIncorporation = Some("A1")
                ))
              ))
              validate(json) shouldBe false
            }

            "contains a symbol" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  countryOfIncorporation = Some("A@")
                ))
              ))
              validate(json) shouldBe false
            }
          }

          "non-uk crn" when {

            "is None" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  crn = None
                ))
              ))

              validate(json) shouldBe false
            }

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  crn = Some("")
                ))
              ))

              validate(json) shouldBe false
            }
          }

          "knownAs" when {

            "knownAs is empty" in {

              val json = Json.toJson(ParentCompany(
                ultimateParent = Some(NonUkUltimateParent(
                  knownAs = Some("")
                ))
              ))

              validate(json) shouldBe false
            }
          }

          s"is longer than $maxCompanyNameLength characters" in {

            val json = Json.toJson(ParentCompany(
              ultimateParent = Some(NonUkUltimateParent(
                knownAs = Some("A" * (maxCompanyNameLength + 1))
              ))
            ))
            validate(json) shouldBe false
          }
        }
      }

      "deemedParent" when {

        "Uk Deemed Company" when {

          "company name" when {

            "is None" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  companyName = None
                )))
              ))

              validate(json) shouldBe false
            }

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  companyName = Some("")
                )))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  companyName = Some("A" * (maxCompanyNameLength + 1))
                )))
              ))

              validate(json) shouldBe false
            }
          }

          "ctutr" when {

            s"below $utrLength" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  ctutr = Some("1" * (utrLength - 1))
                )))
              ))
              validate(json) shouldBe false
            }

            s"above $utrLength" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  ctutr = Some("1" * (utrLength + 1))
                )))
              ))

              validate(json) shouldBe false
            }

            "is non numeric" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  ctutr = Some("a" * utrLength)
                )))
              ))

              validate(json) shouldBe false
            }

            "is a symbol" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  ctutr = Some("@")
                )))
              ))
              validate(json) shouldBe false
            }
          }

          "crn" when {

            s"below $crnLength" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  crn = Some("1" * (crnLength - 1))
                )))
              ))
              validate(json) shouldBe false
            }

            s"above $crnLength" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  crn = Some("1" * (crnLength + 1))
                )))
              ))

              validate(json) shouldBe false
            }
          }

          "sautr" when {

            s"below $utrLength" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  sautr = Some("1" * (utrLength - 1))
                )))
              ))
              validate(json) shouldBe false
            }

            s"above $utrLength" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  sautr = Some("1" * (utrLength + 1))
                )))
              ))
              validate(json) shouldBe false
            }

            "is non numeric" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  sautr = Some("a" * utrLength)
                )))
              ))

              validate(json) shouldBe false
            }

            "is a symbol" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  sautr = Some("@")
                )))
              ))

              validate(json) shouldBe false
            }
          }

          "knownAs" when {

            "knownAs is empty" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  knownAs = Some("")
                )))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(UkDeemedParent(
                  knownAs = Some("A" * (maxCompanyNameLength + 1))
                )))
              ))

              validate(json) shouldBe false
            }
          }
        }

        "non-uk Deemed company" when {

          "company name" when {

            "is None" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  companyName = None
                )))
              ))

              validate(json) shouldBe false
            }

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  companyName = Some("")
                )))
              ))

              validate(json) shouldBe false
            }

            s"is longer than $maxCompanyNameLength characters" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  companyName = Some("A" * (maxCompanyNameLength + 1))
                )))
              ))

              validate(json) shouldBe false
            }
          }

          "countryOfIncorporation" when {

            "is only one letter" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  countryOfIncorporation = Some("A")
                )))
              ))
              validate(json) shouldBe false
            }

            "is three letters" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  countryOfIncorporation = Some("AAA")
                )))
              ))
              validate(json) shouldBe false
            }

            "contains a number" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  countryOfIncorporation = Some("A1")
                )))
              ))
              validate(json) shouldBe false
            }

            "contains a symbol" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  countryOfIncorporation = Some("A@")
                )))
              ))
              validate(json) shouldBe false
            }
          }

          "non-uk crn" when {

            s"is empty" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  crn = Some("")
                )))
              ))

              validate(json) shouldBe false
            }
          }

          "knownAs" when {

            "knownAs is empty" in {

              val json = Json.toJson(ParentCompany(
                deemedParent = Some(Seq(NonUkDeemedParent(
                  knownAs = Some("")
                )))
              ))

              validate(json) shouldBe false
            }
          }

          s"is longer than $maxCompanyNameLength characters" in {

            val json = Json.toJson(ParentCompany(
              deemedParent = Some(Seq(NonUkDeemedParent(
                knownAs = Some("A" * (maxCompanyNameLength + 1))
              )))
            ))
            validate(json) shouldBe false
          }
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
          Some(Seq(UkDeemedParent(), UkDeemedParent(), UkDeemedParent(), UkDeemedParent())))
        )

        validate(json) shouldBe false
      }
    }
  }
}
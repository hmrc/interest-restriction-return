
# Interest Restriction Return

This is a protected backend microservice that is the conduit between Public facing services and HMRC HoD systems.

## API Endpoint Definitions

**Manage the Reporting Company for the Interest Restriction Return filing**

- [Appoint a Reporting Company](#Appoint-a-Reporting-Company)
- [Revoke a Reporting Company](#Revoke-a-Reporting-Company)

**File the Interest Restriction Return**

- [Submit Abbreviated Return](#Submit-an-Abbreviated-Interest-Restriction-Return)
- [Submit Full Return](#Submit-a-Full-Interest-Restriction-Return)

### Appoint a Reporting Company

**URL:** `/reporting-company/appoint`

**Method:** `POST`

**Authentication required:** `Yes, bearer token required`

**Request Schema:** [Json Schema](public/api/conf/1.0/schemas/appointReportingCompanySchema.json)

**Example Request**
```json
{
  "agentDetails": {
    "agentActingOnBehalfOfCompany": true,
    "agentName": "some agent"
  },
  "reportingCompany": {
    "companyName": "company name a",
    "ctutr": "1123456789",
    "sameAsUltimateParent": false
  },
  "isReportingCompanyAppointingItself": false,
  "identityOfAppointingCompany": {
    "companyName": "company name b",
    "countryOfIncorporation": "IT"
  },
  "ultimateParentCompany": {
    "isUk": true,
    "companyName": "parent company a",
    "sautr": "1111111111"
  },
  "authorisingCompanies": [
    {
      "companyName": "company name c",
      "utr": "1111111111"
    },
    {
      "companyName": "company name d",
      "utr": "1111111111"
    }
  ],
  "accountingPeriod": {
    "startDate": "2018-11-01",
    "endDate": "2019-12-01"
  },
  "declaration": true
}
```

#### Success Response

**Code:** `200 (OK)`

**Response Schema**
```json
{
    "type": "object",
    "properties": {
        "acknowledgementReference": {
            "type": "string"
        }   
    }   
}
```

**Example Response**

Status: `200 (OK)`
```json
{
    "acknowledgementReference": "ABC1234"
}
```

#### Error Responses

**Response Schema**
```json
{
  "type": "array",
  "minItems": 1,
  "items": {
     "type": "object",
     "properties": {
       "code": {
         "type": "string"
       },
       "message": {
         "type": "string"
       }
     }
  } 
}
```

**Responses**

| Http Status | Code | Message |
|-------------|------|---------|
| 400 | BadRequest | No JSON body received with the request |
| 400 | BadRequest | Invalid JSON body received. Failed to parse with reason {x} |
| 400 | BadRequest | Field {x} failed validation rules for reason {y} |
| 401 | Unauthenticated | Unauthenticated request received |
| 500 | InternalServerError | Unexpected Internal Server Error |x


**Example response**

Status: `500 (INTERNAL_SERVER_ERROR)`
```javascript
[{
    "code": "InternalServerError"
    "message": "Unexpected Internal Server Error"
}]
```

### Revoke a Reporting Company


**URL:** `/reporting-company/revoke`

**Method:** `POST`

**Authentication required:** `Yes, bearer token required`

**Request Schema:** [Json Schema](public/api/conf/1.0/schemas/revokeReportingCompanySchema.json)

**Example Request**
```json
{
  "agentDetails": {
    "agentActingOnBehalfOfCompany": true,
    "agentName": "some agent"
  },
  "reportingCompany": {
    "companyName": "company name a",
    "ctutr": "1123456789",
    "sameAsUltimateParent": false
  },
  "isReportingCompanyRevokingItself": false,
  "companyMakingRevocation": {
    "companyName": "company name b",
    "countryOfIncorporation": "IT"
  },
  "ultimateParentCompany": {
    "isUk": true,
    "companyName": "parent company a",
    "sautr": "1111111111"
  },
  "authorisingCompanies": [
    {
      "companyName": "company name c",
      "utr": "1111111111"
    },
    {
      "companyName": "company name d",
      "utr": "1111111111"
    }
  ],
  "accountingPeriod": {
    "startDate": "2018-11-01",
    "endDate": "2019-12-01"
  },
  "declaration": true
}
```

#### Success Response

**Code:** `200 (OK)`

**Response Schema**
```json
{
    "type": "object",
    "properties": {
        "acknowledgementReference": {
            "type": "string"
        }   
    }   
}
```

**Example Response**

Status: `200 (OK)`
```json
{
    "acknowledgementReference": "ABC1234"
}
```

#### Error Responses

**Response Schema**
```json
{
  "type": "array",
  "minItems": 1,
  "items": {
     "type": "object",
     "properties": {
       "code": {
         "type": "string"
       },
       "message": {
         "type": "string"
       }
     }
  } 
}
```

**Responses**

| Http Status | Code | Message |
|-------------|------|---------|
| 400 | BadRequest | No JSON body received with the request |
| 400 | BadRequest | Invalid JSON body received. Failed to parse with reason {x} |
| 400 | BadRequest | Field {x} failed validation rules for reason {y} |
| 401 | Unauthenticated | Unauthenticated request received |
| 500 | InternalServerError | Unexpected Internal Server Error |x


**Example response**

Status: `500 (INTERNAL_SERVER_ERROR)`
```javascript
[{
    "code": "InternalServerError"
    "message": "Unexpected Internal Server Error"
}]
```

### Submit an Abbreviated Interest Restriction Return

**URL:** `/return/abbreviated`

**Method:** `POST`

**Authentication required:** `Yes, bearer token required`

**Request Schema:** [Json Schema](public/api/conf/1.0/schemas/abbreviatedReturnSchema.json)

**Example Request**
```json
   {
    "declaration": true,
    "appointedReportingCompany": true,
    "agentDetails": {
      "agentActingOnBehalfOfCompany": true,
      "agentName": "some agent"
    },
    "reportingCompany": {
      "companyName": "Company Name ltd",
      "ctutr": "1123456789",
      "sameAsUltimateParent": false
    },
    "parentCompany": {
      "ultimateParent": {
        "companyName": "Company Name ltd",
        "isUk": true,
        "ctutr": "1123456789"
      }
    },
    "groupCompanyDetails": {
      "accountingPeriod": {
        "startDate": "2019-02-18",
        "endDate": "2020-08-17"
      }
    },
    "submissionType": "revised",
    "revisedReturnDetails": "some details",
    "groupLevelElections": {
      "activeInterestAllowanceAlternativeCalculation": true,
      "groupRatio": {
        "isElected": true,
        "activeGroupEBITDAChargeableGains": true,
        "groupEBITDAChargeableGains": true,
        "groupRatioBlended": {
          "isElected": true,
          "investorGroups": [{
            "groupName": "some investor group",
            "elections": ["groupRatioBlended", "groupEBITDA", "interestAllowanceAlternativeCalculation", "interestAllowanceNonConsolidatedInvestment", "interestAllowanceConsolidatedPartnership"]
          }, {
            "groupName": "some investor group",
            "elections": ["interestAllowanceAlternativeCalculation", "interestAllowanceNonConsolidatedInvestment", "interestAllowanceConsolidatedPartnership"]
          }]
        }
      },
      "interestAllowanceAlternativeCalculation": true,
      "interestAllowanceNonConsolidatedInvestment": {
        "isElected": true,
        "nonConsolidatedInvestments": [{
          "investmentName": "investment1"
        }]
      },
      "interestAllowanceConsolidatedPartnership": {
        "isElected": true,
        "isActive": true,
        "consolidatedPartnerships": [{
          "partnershipName": "some partner",
          "sautr": "1123456789"
        }]
      }
    },
    "ukCompanies": [{
      "companyName": "Company Name ltd",
      "utr": "1123456789",
      "consenting": true,
      "qicElection": true
    }, {
      "companyName": "Company Name ltd",
      "utr": "1123456789",
      "consenting": true,
      "qicElection": true
    }]
  }
```

#### Success Response

**Code:** `200 (OK)`

**Response Schema**
```json
{
    "type": "object",
    "properties": {
        "acknowledgementReference": {
            "type": "string"
        }   
    }   
}
```

**Example Response**

Status: `200 (OK)`
```json
{
    "acknowledgementReference": "ABC1234"
}
```

#### Error Responses

**Response Schema**
```json
{
  "type": "array",
  "minItems": 1,
  "items": {
     "type": "object",
     "properties": {
       "code": {
         "type": "string"
       },
       "message": {
         "type": "string"
       }
     }
  } 
}
```

**Responses**

| Http Status | Code | Message |
|-------------|------|---------|
| 400 | BadRequest | No JSON body received with the request |
| 400 | BadRequest | Invalid JSON body received. Failed to parse with reason {x} |
| 400 | BadRequest | Field {x} failed validation rules for reason {y} |
| 401 | Unauthenticated | Unauthenticated request received |
| 500 | InternalServerError | Unexpected Internal Server Error |x


**Example response**

Status: `500 (INTERNAL_SERVER_ERROR)`
```javascript
[{
    "code": "InternalServerError"
    "message": "Unexpected Internal Server Error"
}]
```

### Submit a Full Interest Restriction Return

**URL:** `/return/full`

**Method:** `POST`

**Authentication required:** `Yes, bearer token required`

**Request Schema:** [Json Schema](public/api/conf/1.0/schemas/fullReturnSchema.json)

**Example Request**
```json
   {
    "declaration": true,
    "appointedReportingCompany": true,
    "agentDetails": {
      "agentActingOnBehalfOfCompany": true,
      "agentName": "some agent"
    },
    "reportingCompany": {
      "companyName": "Company Name ltd",
      "ctutr": "1123456789",
      "sameAsUltimateParent": false
    },
    "parentCompany": {
      "ultimateParent": {
        "companyName": "Company Name ltd",
        "isUk": true,
        "ctutr": "1123456789"
      }
    },
    "groupCompanyDetails": {
      "accountingPeriod": {
        "startDate": "2019-02-18",
        "endDate": "2020-08-17"
      }
    },
    "submissionType": "revised",
    "revisedReturnDetails": "some details",
    "groupLevelElections": {
      "activeInterestAllowanceAlternativeCalculation": true,
      "groupRatio": {
        "isElected": true,
        "activeGroupEBITDAChargeableGains": true,
        "groupEBITDAChargeableGains": true,
        "groupRatioBlended": {
          "isElected": true,
          "investorGroups": [{
            "groupName": "some investor group",
            "elections": ["groupRatioBlended", "groupEBITDA", "interestAllowanceAlternativeCalculation", "interestAllowanceNonConsolidatedInvestment", "interestAllowanceConsolidatedPartnership"]
          }, {
            "groupName": "some investor group",
            "elections": ["interestAllowanceAlternativeCalculation", "interestAllowanceNonConsolidatedInvestment", "interestAllowanceConsolidatedPartnership"]
          }]
        }
      },
      "interestAllowanceAlternativeCalculation": true,
      "interestAllowanceNonConsolidatedInvestment": {
        "isElected": true,
        "nonConsolidatedInvestments": [{
          "investmentName": "investment1"
        }]
      },
      "interestAllowanceConsolidatedPartnership": {
        "isElected": true,
        "isActive": true,
        "consolidatedPartnerships": [{
          "partnershipName": "some partner",
          "sautr": "1123456789"
        }]
      }
    },
    "ukCompanies": [{
      "companyName": "Company Name ltd",
      "utr": "1123456789",
      "consenting": true,
      "qicElection": true,
      "netTaxInterestExpense": 0,
      "netTaxInterestIncome": 50,
      "taxEBITDA": 5,
      "allocatedReactivations": {
        "currentPeriodReactivation": 2
      }
    }, {
      "companyName": "Company Name ltd",
      "utr": "1123456789",
      "consenting": true,
      "qicElection": true,
      "netTaxInterestExpense": 0,
      "netTaxInterestIncome": 50,
      "taxEBITDA": 5,
      "allocatedReactivations": {
        "currentPeriodReactivation": 2
      }
    }],
    "angie": 1.11,
    "returnContainsEstimates": false,
    "groupSubjectToInterestRestrictions": false,
    "groupSubjectToInterestReactivation": true,
    "totalRestrictions": 0,
    "groupLevelAmount": {
      "interestReactivationCap": 300,
      "interestAllowanceBroughtForward": 3,
      "interestAllowanceForPeriod": 4,
      "interestCapacityForPeriod": 5
    },
    "adjustedGroupInterest": {
      "qngie": 100,
      "groupEBITDA": 200,
      "groupRatio": 50
    }
  }
```

#### Success Response

**Code:** `200 (OK)`

**Response Schema**
```json
{
    "type": "object",
    "properties": {
        "acknowledgementReference": {
            "type": "string"
        }   
    }   
}
```

**Example Response**

Status: `200 (OK)`
```json
{
    "acknowledgementReference": "ABC1234"
}
```

#### Error Responses

**Response Schema**
```json
{
  "type": "array",
  "minItems": 1,
  "items": {
     "type": "object",
     "properties": {
       "code": {
         "type": "string"
       },
       "message": {
         "type": "string"
       }
     }
  } 
}
```

**Responses**

| Http Status | Code | Message |
|-------------|------|---------|
| 400 | BadRequest | No JSON body received with the request |
| 400 | BadRequest | Invalid JSON body received. Failed to parse with reason {x} |
| 400 | BadRequest | Field {x} failed validation rules for reason {y} |
| 401 | Unauthenticated | Unauthenticated request received |
| 500 | InternalServerError | Unexpected Internal Server Error |x


**Example response**

Status: `500 (INTERNAL_SERVER_ERROR)`
```javascript
[{
    "code": "InternalServerError"
    "message": "Unexpected Internal Server Error"
}]
```

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

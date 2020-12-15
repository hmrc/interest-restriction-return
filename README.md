
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
    	"ctutr": "1234567890",
    	"sameAsUltimateParent": true,
    	"reportingCompanyDeemed": false
    },
    "authorisingCompanies": [
      {
    	"companyName": "company name b",
    	"ctutr": "1234567890"
      },
      {
    	"companyName": "company name c",
    	"ctutr": "1234567890"
      }
    ],
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
      "companyName": "some reporting company",
      "ctutr": "1234567890",
      "sameAsUltimateParent": true,
      "reportingCompanyDeemed": true
    },
    "isReportingCompanyRevokingItself": true,
    "companyMakingRevocation": {
      "companyName": "some company",
      "ctutr": "1234567890",
      "countryOfIncorporation": "US"
    },
    "ultimateParent": {
      "registeredCompanyName": "some company",
      "ctutr": "1234567890"
    },
    "accountingPeriod": {
      "startDate": "2019-05-01",
      "endDate": "2020-04-30"
    },
    "authorisingCompanies": [
      {
        "companyName": "some authorising company",
        "ctutr": "1234567890"
      }
    ],
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
     "agentDetails": {
       "agentActingOnBehalfOfCompany": true,
       "agentName": "some agent"
     },
     "reportingCompany": {
       "companyName": "some reporting company",
       "ctutr": "1234567890",
       "sameAsUltimateParent": true,
       "reportingCompanyDeemed": true
     },
     "parentCompany": {
       "ultimateParent": {
         "registeredCompanyName": "some company",
         "ctutr": "1234567890"
       },
       "deemedParent": [
         {
           "companyName": "some company ltd",
           "ctutr": "1234567890",
           "countryOfIncorporation": "US",
           "sautr": "9876543210"
         }
       ]
     },
     "publicInfrastructure": true,
     "groupCompanyDetails": {
       "totalCompanies": 1,
       "accountingPeriod": {
         "startDate": "2019-05-01",
         "endDate": "2020-04-30"
       }
     },
     "submissionType": "original",
     "revisedReturnDetails": "revised details",
     "groupLevelElections": {
       "isElected": true,
       "groupRatioBlended": {
         "isElected": true,
         "investorGroups": [
           "investment1",
           "investment2",
           "investment3"
         ]
       },
       "groupEBITDAChargeableGains": true,
       "interestAllowanceAlternativeCalculation": true,
       "interestAllowanceNonConsolidatedInvestment": {
         "isElected": true,
         "nonConsolidatedInvestments": [
           "investment1",
           "investment2",
           "investment3"
         ]
       },
       "interestAllowanceConsolidatedPartnership": {
         "isElected": true,
         "consolidatedPartnerships": [
           "investment1",
           "investment2",
           "investment3"
         ]
       }
     },
     "ukCompanies": [
       {
         "companyName": "some company",
         "utr": "1234567890",
         "consenting": true,
         "qicElection": false
       }
     ]
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
     "agentDetails": {
       "agentActingOnBehalfOfCompany": true,
       "agentName": "some agent"
     },
     "reportingCompany": {
       "companyName": "some reporting company",
       "ctutr": "1234567890",
       
       "sameAsUltimateParent": true,
       "reportingCompanyDeemed": true
     },
     "parentCompany": {
       "deemedParent": [
         {
           "companyName": "some company ltd",
           "sautr": "9876543210"
         },
         {
           "companyName": "some company ltd",
           "countryOfIncorporation": "US"
         }
       ]
     },
     "publicInfrastructure": true,
     "groupCompanyDetails": {
       "totalCompanies": 1,
       "accountingPeriod": {
         "startDate": "2019-05-01",
         "endDate": "2020-04-30"
       }
     },
     "submissionType": "original",
     "revisedReturnDetails": "some details",
     "groupLevelElections": {
       "isElected": true,
       "groupRatioBlended": {
         "isElected": true,
         "investorGroups": [
           "investment1",
           "investment2",
           "investment3"
         ]
       },
       "groupEBITDAChargeableGains": true,
       "interestAllowanceAlternativeCalculation": true,
       "interestAllowanceNonConsolidatedInvestment": {
         "isElected": true,
         "nonConsolidatedInvestments": [
           "investment1",
           "investment2",
           "investment3"
         ]
       },
       "interestAllowanceConsolidatedPartnership": {
         "isElected": true,
         "consolidatedPartnerships": [
           "investment1",
           "investment2",
           "investment3"
         ]
       }
     },
     "ukCompanies": [
       {
         "companyName": "some company",
         "utr": "1234567890",
         "consenting": true,
         "qicElection": false,
         "netTaxInterestExpense": 1.11,
         "netTaxInterestIncome": 2.22,
         "taxEBITDA": 3.33
       }
     ],
     "angie": 1.1,
     "groupSubjectToInterestRestrictions": true,
     "groupSubjectToInterestReactivation": true,
     "groupLevelAmount": {
       "totalDisallowedAmount": 1,
       "interestReactivationCap": 1,
       "interestAllowanceBroughtForward": 1,
       "interestAllowanceForPeriod": 1,
       "interestCapacityForPeriod": 1
     },
     "adjustedGroupInterest": {
       "qngie": 1,
       "groupEBITDA": 1,
       "groupRatio": 1
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


# Interest Restriction Return

This is a protected backend microservice that offers endpoints to:

**Manage the Reporting Company for the Interest Restriction Return filing**

- Appoint a Reporting Company
- Revoke a Reporting Company

**File the Interest Restriction Return**

- Submit Abbreviated Return
- Submit Full Return

## APIs

### Appoint a Reporting Company 

**URL:** `/interest-restriction-retriction/reporting-company/appoint`

**Method:** `POST`

**Authentication required:** `Yes, bearer token required`

**Request Schema:** [Json Schema](./docs/schemas/appointReportingCompanySchema.json)

**Example Request**
```json
  {
    "agentDetails": {
    	"agentActingOnBehalfOfCompany": true,
    	"agentName": "some agent"
	},
    "reportingCompany": {
    	"companyName": "company name a",
    	"utr": "1234567890",
    	"crn": "12345678",
    	"sameAsUltimateParent": true,
    	"reportingCompanyDeemed": false
    },
    "authorisingCompanies": [
      {
    	"companyName": "company name b",
    	"utr": "1234567890"
      },
      {
    	"companyName": "company name c",
    	"utr": "1234567890"
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
        "acknowledgemenetReference": {
            "type": "string"
        }   
    }   
}
```

**Example Response**
```json
{
    "acknowledgemenetReference": "ABC1234"
}
```

#### Error Responses

**Response Schema**
```json
{
  "type": "array",
  "properties": {
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

**Code:** `200 (OK)`

**Content example**
```javascript
{
    "acknowledgemenetReference": "ABC1234"
}
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

# Interest Restriction Return

This is a protected backend microservice that is the conduit between Public facing services and HMRC HoD systems.

## Running tests
To compile and run all tests locally run `./run_all_tests.sh`

## Viewing Documentation
### Locally
- Run Interest Restriction Return and other required services with the script:

    ```
     ./run_local_preview_documentation.sh
    ```
- Navigate to the preview page at http://localhost:9680/api-documentation/docs/openapi/preview
- Enter the full URL path of the OpenAPI specification file with the appropriate port and version:

    ```
     http://localhost:9261/api/conf/1.0/application.yaml
    ```
- Ensure to uncomment the lines [here](https://github.com/hmrc/interest-restriction-return/blob/main/conf/application.conf#L60-L63) in application.conf, in case of CORS errors

### On Developer Hub
Full documentation can be found on the [Developer Hub](https://developer.service.hmrc.gov.uk/api-documentation/docs/api/service/interest-restriction-return).

## Running locally
This API can be run in a local development environment connected to dependent services,
with [Interest Restriction Return Dynamic Stub](https://github.com/hmrc/interest-restriction-return-dynamic-stub) stubbing the behaviour of DES.

The `./run_local_with_dependencies.sh` script will use Service Manager 2 to start up
the dependent services before running Interest Restriction Return on port `9261`.

You can then use Authority Wizard to obtain a bearer token to call the API with:
* Go to the [Authority Wizard GG Sign In](http://localhost:9949/auth-login-stub/gg-sign-in) page
* Set the Redirect URL to `http://localhost:9025`
* Set the Credential Strength to `strong`
* Set the Confidence Level to `50`
* Set the Affinity Group to `Organisation` or `Agent`
* Submit the form. You'll get an error about not being able to connect to a server
  because you aren't running anything on the redirect URL that you specified.
* Go to the [Authority Wizard Session](http://localhost:9949/auth-login-stub/session) page
* Copy the value of the `authToken`. This is the bearer token that you will use to call the API.

To make the API endpoint calls:
* Use postman collections and environment variables [here](https://github.com/hmrc/interest-restriction-return-contract-tests/tree/main/postman)
* Ensure correct url `http://localhost:9261{endpoint route}` is set, where the endpoint routes are found [here](https://github.com/hmrc/interest-restriction-return/blob/main/conf/v1.routes) e.g. `http://localhost:9261/return/full` 
* Set `Authorization` header to the bearer token that you obtained from Authority Wizard
* Ensure `Accept` header is set to `application/vnd.hmrc.1.0+json`

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

# Interest Restriction Return

This is a protected backend microservice that is the conduit between Public facing services and HMRC HoD systems.

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

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
- Ensure to uncomment the lines 60-63 in application.conf, in case of CORS errors

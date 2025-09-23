# cautious-computing-machine

## Infos and Resources
- [Movie Service - REST](https://github.com/dilipsundarraj1/reactive-movies-restful-api)


## Challenges

### Unit testing with Wiremock (and WebTestClient)

Works but ...
- Wiremock is currently bundled with Jetty 11
- WebTestClient won't work with that version
- in this non Spring Boot project, the straightforward solution is
  - switch to Jetty 12 as supported by Wiremock too
  - exclude all the Jetty 11 dependencies
  - use the following Spring dependencies
    - `spring-test` - maybe?
    - `spring-webflux`
    - `spring-context`
- otherwise you will experience some crazy shit like
  - `NoSuchMethodError`
  - `IncompatibleClassChangeError`

### Wiremock Response Templating
- [Response Templating](https://wiremock.org/docs/response-templating/)
- [Handlebars](https://handlebarsjs.com/)

#### Challenge: Map query parameter to a response body file
Easy but don't trust that Copilot crap. The following tips may help instead
- use `urlPathTemplate` to specify the path ([RFC 6570](https://www.rfc-editor.org/rfc/rfc6570))
- narrow down the path matching avoiding future matching conflicts
- place the response files under `src/test/resources/__files/` which seems to be a Wiremock 'standard'
- add `"transformers": ["response-template"]` to the response attributes; see also [Enabling/disabling response templating](https://wiremock.org/docs/response-templating/#enablingdisabling-response-templating)

#### Provide mappings and test data for Wiremock
Just thinking ...  

**Options for mappings**
- hard coded in Unit test (Wiremock Stub)
  - only necessary mappings are to be loaded
  - reduces confusion due to missing information when reading the test code
- as JSON in `src/test/resources/mappings`
  - nice if there is some copy & paste possible
- import from Open API yaml
  - example data only which might be not sufficient to cover all test cases

**Options for test data**
- hard code in Unit test (Wiremock Stub)
  - always reasonable for 'small' tests
- read from resource files (JSON, CSV)
  - needs some IO logic
  - data dumps might be already available
- in-memory database (object store)
  - supports state
  - reuse of already existing scripts to initialize (static) data

**Reasoning when not using an in memory database**
- state won't be needed at all, because testing the boundary only instead of a virtualizing a system
- Wiremock might support states also
- increase of complexity
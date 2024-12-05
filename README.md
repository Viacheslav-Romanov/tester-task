[![Tester Task CI](https://github.com/Viacheslav-Romanov/tester-task/actions/workflows/ci.yml/badge.svg)](https://github.com/Viacheslav-Romanov/tester-task/actions/workflows/ci.yml)

##### Tester Task project features:

- Use [Fleet](https://www.jetbrains.com/fleet) as an IDE to open the project as it uses [Amper](https://github.com/JetBrains/amper) for build and package management
- To run tests execute the `./gradlew test` command
- GHA as a CI for quality status
- Simple performance testing's implemented with [taurus](https://gettaurus.org) and cloud reporting capability. All is integrated into GHA pipeline.
- Test reports can be downloaded from the pipeline outputs
- okhttp3 client is used to make api request and receive ws updates

##### Not part of test automation:

- models for responses and requests are not implemented (retrofit with the kotlinx's json serializer library can be used but for the lack of time it's omited)
- for the same reason an extensive performance testing has been skiped (can be extended to utilize [junit executors](https://gettaurus.org/docs/JUnit/))

##### Key points to check in test task:

- README file presence
- Logical project structure
- Minimal hardcoding, proper constants usage
- Code scalability and maintainability
- Test atomicity and independence
- Code reusability

The primary focus is on the quality of automated tests and project structure. The load test is an additional task, and its absence will not be considered as a disadvantage in the assessment.
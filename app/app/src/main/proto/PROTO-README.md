# Build protobuf with gradle (updated 11/2/23)
To generate proto code using gradle, use command `./gradlew <task>` where task is:
- generateDemoDebugProto
- generateDemoReleaseProto
- generateDevDebugProto
- generateDevReleaseProto
- generateProdDebugProto
- generateProdReleaseProto

These are all the different build variants - I do not think it matters in any way, as they all probably do the same thing. Something to check on later

proto code is generated to `app/build/generated/source/proto`
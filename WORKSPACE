load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

maven_jar(
    name = "hamcrest_maven",
    artifact = "org.hamcrest:hamcrest-library:1.3",
)

maven_jar(
    name = "mockito_maven",
    artifact = "org.mockito:mockito-core:2.13.0",
)

maven_jar(
    name = "junit_maven",
    artifact = "junit:junit:4.12",
)

maven_jar(
    name = "bytebuddy_maven",
    artifact = "net.bytebuddy:byte-buddy:1.7.9",
)

maven_jar(
    name = "bytebuddy_agent_maven",
    artifact = "net.bytebuddy:byte-buddy-agent:1.7.9",
)

maven_jar(
    name = "objenesis_maven",
    artifact = "org.objenesis:objenesis:2.6",
)

maven_jar(
    name = "javax_annotations_maven",
    artifact = "com.google.code.findbugs:jsr305:3.0.2",
)

http_archive(
    name = "com_google_protobuf",
    sha256 = "cef7f1b5a7c5fba672bec2a319246e8feba471f04dcebfe362d55930ee7c1c30",
    strip_prefix = "protobuf-3.5.0",
    urls = ["https://github.com/google/protobuf/archive/v3.5.0.zip"],
)

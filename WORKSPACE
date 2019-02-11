load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

maven_jar(
    name = "hamcrest_maven",
    artifact = "org.hamcrest:hamcrest-library:1.3",
)

maven_jar(
    name = "mockito_maven",
    artifact = "org.mockito:mockito-core:2.13.0",
)

# This is unfortunately required because of a bug in JUnit 5 where it requires
# org.junit.ComparisonFailure.
maven_jar(
    name = "junit",
    artifact = "junit:junit:4.12",
)

maven_jar(
    name = "junit_jupiter_api",
    artifact = "org.junit.jupiter:junit-jupiter-api:5.3.2",
)

maven_jar(
    name = "junit_jupiter_engine",
    artifact = "org.junit.jupiter:junit-jupiter-engine:5.3.2",
)

maven_jar(
    name = "junit_platform_commons",
    artifact = "org.junit.platform:junit-platform-commons:1.4.0",
)

maven_jar(
    name = "junit_platform_console",
    artifact = "org.junit.platform:junit-platform-console:1.4.0",
)

maven_jar(
    name = "junit_platform_engine",
    artifact = "org.junit.platform:junit-platform-engine:1.4.0",
)

maven_jar(
    name = "junit_platform_launcher",
    artifact = "org.junit.platform:junit-platform-launcher:1.4.0",
)

maven_jar(
    name = "opentest4j",
    artifact = "org.opentest4j:opentest4j:1.0.0-M1",
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

maven_jar(
    name = "google_truth",
    artifact = "com.google.truth:truth:0.42",
)

maven_jar(
    name = "google_auto_value_annotations",
    artifact = "com.google.auto.value:auto-value-annotations:1.6.2",
)

maven_jar(
    name = "google_auto_value",
    artifact = "com.google.auto.value:auto-value:1.6.2",
)

maven_jar(
    name = "google_guava",
    artifact = "com.google.guava:guava:27.0.1-jre",
)

maven_jar(
    name = "diff_utils",
    artifact = "com.googlecode.java-diff-utils:diffutils:1.2",
)

git_repository(
    name = "junit5_bazel_support",
    remote = "https://github.com/junit-team/junit5-samples.git",
    tag = "prototype-1",
)

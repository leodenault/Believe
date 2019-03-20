load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Load common Bazel targets defined by Google.
http_archive(
    name = "google_bazel_common",
    strip_prefix = "bazel-common-1c225e62390566a9e88916471948ddd56e5f111c",
    urls = ["https://github.com/google/bazel-common/archive/1c225e62390566a9e88916471948ddd56e5f111c.zip"],
)

load("@google_bazel_common//:workspace_defs.bzl", "google_common_workspace_rules")

google_common_workspace_rules()

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
    name = "google_truth_java8",
    artifact = "com.google.truth.extensions:truth-java8-extension:0.42",
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

# We need this specific commit number because of a bug in the Dagger library at API v21.
# See https://github.com/google/dagger/pull/1366#issuecomment-470016185 for context on the issue.
DAGGER_VERSION_NUM = "9e0baea74aa2b2ef83f78898bac44851b9840f30"

http_archive(
    name = "google_dagger",
    strip_prefix = "dagger-" + DAGGER_VERSION_NUM,
    urls = ["https://github.com/google/dagger/archive/" + DAGGER_VERSION_NUM + ".zip"],
)

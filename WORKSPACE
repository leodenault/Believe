load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

#############################################################
# Protobuf
#############################################################

protobuf_version = "3.19.1"

http_archive(
    name = "com_google_protobuf",
    sha256 = "c10405fb99b361388d8dbfbe5fe43ef3c53a06dfd6a9fa8c33e70c34d243b044",
    strip_prefix = "protobuf-%s" % protobuf_version,
    urls = [
        "https://github.com/protocolbuffers/protobuf/releases/download/v3.19.1/protobuf-java-%s.tar.gz" % protobuf_version,
    ],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

#############################################################
# Bazel proto rules
#############################################################

http_archive(
    name = "rules_java",
    sha256 = "ccf00372878d141f7d5568cedc4c42ad4811ba367ea3e26bc7c43445bbc52895",
    strip_prefix = "rules_java-d7bf804c8731edd232cb061cb2a9fe003a85d8ee",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_java/archive/d7bf804c8731edd232cb061cb2a9fe003a85d8ee.tar.gz",
        "https://github.com/bazelbuild/rules_java/archive/d7bf804c8731edd232cb061cb2a9fe003a85d8ee.tar.gz",
    ],
)

http_archive(
    name = "rules_proto",
    sha256 = "2490dca4f249b8a9a3ab07bd1ba6eca085aaf8e45a734af92aad0c42d9dc7aaf",
    strip_prefix = "rules_proto-218ffa7dfa5408492dc86c01ee637614f8695c45",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_proto/archive/218ffa7dfa5408492dc86c01ee637614f8695c45.tar.gz",
        "https://github.com/bazelbuild/rules_proto/archive/218ffa7dfa5408492dc86c01ee637614f8695c45.tar.gz",
    ],
)

load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")

rules_java_dependencies()

rules_java_toolchains()

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

#############################################################
# Load common Bazel targets defined by Google.
#
# MAKE SURE TO LOAD ANY CONFLICTING LIBRARIES BEFORE THIS OR
# ELSE VERY CONFUSING ERRORS WILL OCCUR.
#############################################################
http_archive(
    name = "google_bazel_common",
    strip_prefix = "bazel-common-76d25d1921c2534c7654aebb2e7cf687cfb469aa",
    urls = ["https://github.com/google/bazel-common/archive/76d25d1921c2534c7654aebb2e7cf687cfb469aa.zip"],
)

load("@google_bazel_common//:workspace_defs.bzl", "google_common_workspace_rules")

google_common_workspace_rules()

#############################################################
# Maven Dependencies
#############################################################

RULES_JVM_EXTERNAL_TAG = "3.1"

RULES_JVM_EXTERNAL_SHA = "e246373de2353f3d34d35814947aa8b7d0dd1a58c2f7a6c41cfeaff3007c2d14"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "com.google.auto.factory:auto-factory:1.0-beta6",
        "com.google.auto.value:auto-value:1.6.2",
        "com.google.auto.value:auto-value-annotations:1.6.2",
        "com.google.code.findbugs:jsr305:3.0.2",
        "com.google.guava:guava:29.0-jre",
        "com.google.truth:truth:1.0.1",
        "com.google.truth.extensions:truth-java8-extension:0.42",
        "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0",
        # This is unfortunately required because of a bug in JUnit 5 where it requires
        # org.junit.ComparisonFailure.
        "junit:junit:4.12",
        "org.hamcrest:hamcrest-library:1.3",
        "org.jetbrains.kotlin:kotlin-stdlib:1.3.50",
        "org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.1.0",
        "org.junit.jupiter:junit-jupiter-api:5.3.2",
        "org.junit.jupiter:junit-jupiter-engine:5.3.2",
        "org.junit.platform:junit-platform-engine:1.4.0",
        "org.junit.platform:junit-platform-commons:1.4.0",
        "org.junit.platform:junit-platform-console:1.4.0",
        "org.junit.platform:junit-platform-launcher:1.4.0",
        "org.mockito:mockito-core:2.13.0",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

#############################################################
# Dagger
#############################################################

DAGGER_VERSION_NUM = "2.26"

http_archive(
    name = "google_dagger",
    strip_prefix = "dagger-dagger-" + DAGGER_VERSION_NUM,
    urls = ["https://github.com/google/dagger/archive/dagger-" + DAGGER_VERSION_NUM + ".zip"],
)

#############################################################
# Kotlin
#############################################################
rules_kotlin_version = "1.5.0"

http_archive(
    name = "io_bazel_rules_kotlin",
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/v%s/rules_kotlin_release.tgz" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:repositories.bzl", "kotlin_repositories")

kotlin_repositories()

load("@io_bazel_rules_kotlin//kotlin:core.bzl", "kt_register_toolchains")

kt_register_toolchains()

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Load common Bazel targets defined by Google.
http_archive(
    name = "google_bazel_common",
    strip_prefix = "bazel-common-76d25d1921c2534c7654aebb2e7cf687cfb469aa",
    urls = ["https://github.com/google/bazel-common/archive/76d25d1921c2534c7654aebb2e7cf687cfb469aa.zip"],
)

load("@google_bazel_common//:workspace_defs.bzl", "google_common_workspace_rules")

google_common_workspace_rules()

#############################################################
# Protobuf
#############################################################

http_archive(
    name = "com_google_protobuf",
    strip_prefix = "protobuf-master",
    urls = ["https://github.com/protocolbuffers/protobuf/archive/master.zip"],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

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
        "com.google.truth:truth:0.42",
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
rules_kotlin_version = "legacy-1.3.0-rc4"

rules_kotlin_sha = "fe32ced5273bcc2f9e41cea65a28a9184a77f3bc30fea8a5c47b3d3bfc801dff"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = rules_kotlin_sha,
    strip_prefix = "rules_kotlin-%s" % rules_kotlin_version,
    type = "zip",
    urls = ["https://github.com/bazelbuild/rules_kotlin/archive/%s.zip" % rules_kotlin_version],
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kotlin_repositories", "kt_register_toolchains")

kotlin_repositories()  # if you want the default. Otherwise see custom kotlinc distribution below

kt_register_toolchains()  # to use the default toolchain, otherwise see toolchains below

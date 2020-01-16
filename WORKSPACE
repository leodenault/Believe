load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Load common Bazel targets defined by Google.
http_archive(
    name = "google_bazel_common",
    sha256 = "f20aaaf1d80fa5fbe9843792cd199ab213ab63ee8a0f3931dd14ae7a9d528f05",
    strip_prefix = "bazel-common-4c4c70bdf2a9f5bc9afdf2c27dfcb905cac2eea1",
    urls = ["https://github.com/google/bazel-common/archive/4c4c70bdf2a9f5bc9afdf2c27dfcb905cac2eea1.zip"],
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
    artifact = "net.bytebuddy:byte-buddy:1.9.7",
)

maven_jar(
    name = "bytebuddy_agent_maven",
    artifact = "net.bytebuddy:byte-buddy-agent:1.9.7",
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
    sha256 = "594dbf9bb549dd117108460ea2782bfde9cc47a118465200bea5e02c7edfa04f",
    strip_prefix = "protobuf-master",
    urls = ["https://github.com/protocolbuffers/protobuf/archive/master.zip"],
)

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()

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
    name = "google_auto_factory",
    artifact = "com.google.auto.factory:auto-factory:1.0-beta6",
)

maven_jar(
    name = "diff_utils",
    artifact = "com.googlecode.java-diff-utils:diffutils:1.2",
)

#############################################################
# Dagger
#############################################################

# Load dependencies.

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
        "org.jetbrains.kotlin:kotlin-stdlib:1.3.50",
        "org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.1.0",
        "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0",
    ],
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

DAGGER_VERSION_NUM = "2.25.2"

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

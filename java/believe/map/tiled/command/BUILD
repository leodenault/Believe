load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "command",
    srcs = ["TiledCommandParser.kt"],
    deps = [
        "//java/believe/command",
        "//java/believe/core",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["TiledCommandParserImpl.kt"],
    deps = [
        ":command",
        ":internal_qualifiers",
        "//java/believe/command",
        "//java/believe/core",
        "//java/believe/proto",
        "//java/believe/util:kt",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["TiledCommandDaggerModule.java"],
    deps = [
        ":command",
        ":impl",
        ":internal_qualifiers",
        "//java/believe/command/proto:command_java_proto",
        "//java/believe/proto",
        "//third_party/dagger",
    ],
)

kt_jvm_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.kt"],
    deps = ["//third_party/javax:annotations"],
)

load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = [
        "//java/believe/app:app_pkg",
        "//javatests/believe:believe_tests",
    ],
)

java_library(
    name = "flag_parsers",
    srcs = [
        "Flag.java",
        "FlagParser.java",
    ],
)

java_library(
    name = "impl",
    srcs = [
        "BooleanFlagParser.java",
        "CommandLineParser.java",
        "IntegerFlagParser.java",
    ],
    deps = [
        ":flag_parsers",
        ":qualifiers",
        "//third_party/dagger",
        "//third_party/javax:annotations",
    ],
)

kt_jvm_library(
    name = "qualifiers",
    srcs = ["CommandLineArguments.kt"],
    deps = ["//third_party/javax:annotations"],
)

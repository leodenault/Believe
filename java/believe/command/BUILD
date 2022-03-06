load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "command",
    srcs = [
        "Command.kt",
        "CommandGenerator.kt",
        "CommandParser.kt",
    ],
    deps = [
        "//java/believe/command/proto:command_java_proto",
        "//third_party/dagger",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = [
        "CommandGeneratorImpl.kt",
        "CommandSequenceParser.kt",
    ],
    deps = [
        ":command",
        "//java/believe/command/proto:command_java_proto",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CommandDaggerModule.java"],
    deps = [
        ":command",
        ":impl",
        "//java/believe/command/proto:command_java_proto",
        "//third_party/dagger",
        "//third_party/proto",
    ],
)

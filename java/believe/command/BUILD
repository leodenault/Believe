package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "command",
    srcs = [
        "Command.java",
        "CommandGenerator.java",
        "CommandSupplier.java",
    ],
    deps = [
        ":package_info",
        "//java/believe/core",
    ],
)

java_library(
    name = "impl",
    srcs = [
        "CommandGeneratorImpl.java",
        "CommandSequenceSupplier.java",
    ],
    deps = [
        ":command",
        ":internal_qualifiers",
        ":package_info",
        "//java/believe/command/proto:command_sequence_java_proto",
        "//java/believe/core",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CommandDaggerModule.java"],
    deps = [
        ":command",
        ":impl",
        ":internal_qualifiers",
        ":package_info",
        "//third_party/dagger",
    ],
)

java_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.java"],
    deps = [
        ":package_info",
        "//third_party/javax:annotations",
    ],
)

java_library(
    name = "package_info",
    srcs = ["package-info.java"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

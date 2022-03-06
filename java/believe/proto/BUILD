load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "proto",
    srcs = [
        "ProtoParser.kt",
        "TextProtoParser.kt",
    ],
    deps = [
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["ProtoParserImpl.kt"],
    deps = [
        ":proto",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/kotlin:kotlin_reflect",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ProtoDaggerModule.java"],
    deps = [
        ":impl",
        ":proto",
        "//third_party/dagger",
        "//third_party/proto",
    ],
)

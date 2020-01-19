load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "proto",
    srcs = ["TextProtoParser.kt"],
    deps = [
        "//third_party/auto_factory",
        "//third_party/proto",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["ProtoComponent.java"],
    deps = [
        ":dagger_module",
        ":proto",
        "//third_party/dagger",
        "//third_party/proto",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["ProtoDaggerModule.java"],
    deps = [
        "//third_party/dagger",
        "//third_party/proto",
    ],
)

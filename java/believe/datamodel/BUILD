load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "datamodel",
    srcs = [
        "DataCommitter.kt",
        "DataManager.kt",
        "LoadableData.kt",
        "MutableData.kt",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["MutableValue.kt"],
    deps = [":datamodel"],
)

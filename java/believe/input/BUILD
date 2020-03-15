load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "input",
    srcs = ["InputAdapter.kt"],
)

kt_jvm_library(
    name = "impl",
    srcs = ["InputAdapterImpl.kt"],
    deps = [":input"],
)

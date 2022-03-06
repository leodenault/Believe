load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "transition",
    srcs = ["GameStateTransition.kt"],
    deps = [
        "//java/believe/core",
        "//java/believe/core/display:display_v2",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["EmptyGameStateTransition.kt"],
    deps = [
        ":transition",
        "//third_party/slick",
    ],
)

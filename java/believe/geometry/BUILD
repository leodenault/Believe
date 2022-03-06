load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "geometry",
    srcs = [
        "FloatPoint.kt",
        "Rectangle.kt",
    ],
    deps = ["//third_party/slick"],
)

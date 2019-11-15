load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "level",
    srcs = ["LevelData.kt"],
    deps = [
        "//java/believe/command",
        "//java/believe/level/proto:level_java_proto",
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//third_party/javax:annotations",
    ],
)

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "util",
    srcs = [
        "MapEntry.java",
        "Util.java",
    ],
    deps = [
        "//java/believe/geometry",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "kt",
    srcs = ["KotlinHelpers.kt"],
)

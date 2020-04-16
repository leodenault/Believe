load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "util",
    srcs = [
        "KotlinHelpers.kt",
        "MapEntry.kt",
        "Util.kt",
    ],
    deps = [
        "//java/believe/geometry",
        "//third_party/slick",
    ],
)

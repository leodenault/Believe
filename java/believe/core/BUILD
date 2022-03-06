load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "core",
    srcs = [
        "SynchedComboPattern.java",
        "Timer.java",
        "Updatable.java",
    ],
    deps = [
        "//java/believe/xml",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "core_kt",
    srcs = ["PropertyProvider.kt"],
)

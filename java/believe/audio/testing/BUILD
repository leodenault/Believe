load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "testing",
    srcs = ["FakeSound.kt"],
    deps = ["//java/believe/audio"],
)

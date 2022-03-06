load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "testing",
    srcs = ["FakeInputAdapter.kt"],
    deps = [
        "//java/believe/input",
        "//java/believe/input:impl",
    ],
)

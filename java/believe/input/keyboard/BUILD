load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "keyboard",
    srcs = ["KeyboardInputAdapter.kt"],
    deps = [
        "//java/believe/input",
        "//java/believe/input:impl",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

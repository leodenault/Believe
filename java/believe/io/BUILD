load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "io",
    srcs = ["ResourceManager.kt"],
)

kt_jvm_library(
    name = "impl",
    srcs = ["ResourceManagerImpl.kt"],
    deps = [
        ":io",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["IoDaggerModule.java"],
    deps = [
        ":impl",
        ":io",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

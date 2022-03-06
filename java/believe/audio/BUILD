load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "audio",
    srcs = [
        "Music.kt",
        "Sound.kt",
    ],
    deps = [
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = ["SoundImpl.kt"],
    deps = [
        ":audio",
        "//third_party/slick",
    ],
)

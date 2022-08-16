load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "audio",
    srcs = [
        "LoadableSound.kt",
        "Music.kt",
        "Sound.kt",
    ],
    deps = [
        "//java/believe/datamodel",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

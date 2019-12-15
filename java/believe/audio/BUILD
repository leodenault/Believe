load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "audio",
    srcs = [
        "Music.kt",
        "OpenlAlFixer.kt",
        "Sound.kt",
    ],
    deps = [
        ":internal_qualifiers",
        "//java/believe/io",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

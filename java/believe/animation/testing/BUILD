load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "testing",
    srcs = [
        "AnimationFakes.kt",
        "FakeSpriteSheet.kt",
        "FrameData.kt",
        "Truth.kt",
    ],
    deps = [
        "//java/believe/gui/testing",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

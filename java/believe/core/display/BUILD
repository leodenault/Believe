load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "display",
    srcs = [
        "AnimationSet.java",
        "Renderable.java",
        "SpriteSheetManager.java",
    ],
    deps = [
        "//java/believe/geometry",
        "//java/believe/xml",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "display_v2",
    srcs = [
        "Bindable.kt",
        "Graphics.kt",
        "RenderableV2.kt",
    ],
    deps = [
        "//java/believe/animation",
        "//java/believe/geometry",
        "//third_party/slick",
    ],
)

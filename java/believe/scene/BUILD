load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "scene",
    srcs = [
        "Camera.kt",
        "SceneElement.kt",
    ],
    deps = [
        "//java/believe/core",
        "//java/believe/core/display:display_v2",
        "//java/believe/react",
    ],
)

kt_jvm_library(
    name = "impl",
    srcs = [
        "LevelMap.kt",
        "LevelMapLayer.kt",
        "MapBackground.kt",
    ],
    deps = [
        ":scene",
        "//java/believe/core/display:display_v2",
        "//java/believe/map/data",
        "//java/believe/map/tiled",
    ],
)

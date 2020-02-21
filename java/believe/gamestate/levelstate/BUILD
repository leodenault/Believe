load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "levelstate",
    srcs = ["LevelState.kt"],
    deps = [
        "//java/believe/action",
        "//java/believe/app/annotation",
        "//java/believe/character",
        "//java/believe/character:faction",
        "//java/believe/character/playable",
        "//java/believe/core/io",
        "//java/believe/datamodel:impl",
        "//java/believe/gamestate",
        "//java/believe/gamestate/temporarystate",
        "//java/believe/gamestate/temporarystate:impl",
        "//java/believe/gui",
        "//java/believe/level",
        "//java/believe/map/data",
        "//java/believe/map/gui",
        "//java/believe/map/io",
        "//java/believe/physics/manager",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

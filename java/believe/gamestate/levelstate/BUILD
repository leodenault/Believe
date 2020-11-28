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

kt_jvm_library(
    name = "levelstate_v2",
    srcs = [
        "LevelStateController.kt",
    ],
)

kt_jvm_library(
    name = "levelstate_v2_impl",
    srcs = [
        "LevelStateControllerImpl.kt",
        "LevelStateV2.kt",
        "RunningGameState.kt",
    ],
    deps = [
        ":annotations",
        ":levelstate_v2",
        "//java/believe/character:character_v2",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate/transition:impl",
        "//java/believe/gui:gui_v2",
        "//java/believe/input",
        "//java/believe/level",
        "//java/believe/physics/manager",
        "//java/believe/react",
        "//java/believe/scene",
        "//java/believe/scene:impl",
        "//third_party/auto_factory",
        "//third_party/javax:annotations",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["LevelStateDaggerModule.java"],
    deps = [
        ":annotations",
        ":levelstate_v2",
        ":levelstate_v2_impl",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//third_party/dagger",
    ],
)

kt_jvm_library(
    name = "annotations",
    srcs = ["Annotations.kt"],
    deps = ["//third_party/javax:annotations"],
)

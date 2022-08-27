load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

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
    srcs = ["LevelStateV2.kt"],
    deps = [
        ":controller",
        ":dagger_component",
        "//java/believe/core/display:display_v2",
        "//java/believe/core/io",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/io",
        "//third_party/auto_factory",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "levelstate_v2_impl",
    srcs = [
        "PauseState.kt",
        "RunningGameState.kt",
    ],
    deps = [
        ":annotations",
        ":controller",
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
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "controller",
    srcs = [
        "LevelStateController.kt",
    ],
    deps = [],
)

kt_jvm_library(
    name = "controller_impl",
    srcs = [
        "LevelStateControllerImpl.kt",
    ],
    deps = [
        ":annotations",
        ":levelstate_v2_impl",
        "//java/believe/gamestate:controller",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate/transition:impl",
        "//third_party/dagger",
        "//third_party/javax:annotations",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["LevelStateDaggerModule.java"],
    deps = [
        ":annotations",
        ":controller",
        ":controller_impl",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["LevelStateComponent.java"],
    deps = [
        ":annotations",
        ":controller",
        ":controller_impl",
        ":dagger_module",
        ":levelstate_v2_impl",
        "//java/believe/audio",
        "//java/believe/character:character_v2",
        "//java/believe/character/proto:character_animations_java_proto",
        "//java/believe/core/io",
        "//java/believe/gamestate:controller",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//java/believe/gui",
        "//java/believe/gui:annotations",
        "//java/believe/gui:gui_v2",
        "//java/believe/gui:package-info",
        "//java/believe/input",
        "//java/believe/io",
        "//java/believe/mob/proto:mob_animation_data_java_proto",
        "//java/believe/physics/collision",
        "//java/believe/physics/manager",
        "//java/believe/scene",
        "//java/believe/scene:impl",
        "//third_party/dagger",
        "//third_party/kotlin",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "annotations",
    srcs = ["Annotations.kt"],
    deps = ["//third_party/javax:annotations"],
)

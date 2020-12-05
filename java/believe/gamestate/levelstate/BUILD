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
        "//java/believe/animation:dagger_module",
        "//java/believe/character:dagger_module",
        "//java/believe/command:dagger_module",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//java/believe/gui:dagger_module_v2",
        "//java/believe/io:dagger_module",
        "//java/believe/level:dagger_module",
        "//java/believe/map/collidable/tile:dagger_module",
        "//java/believe/map/io:dagger_module",
        "//java/believe/mob:dagger_module",
        "//java/believe/physics/collision:dagger_module",
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
        "//java/believe/animation",
        "//java/believe/animation:dagger_module",
        "//java/believe/audio",
        "//java/believe/character:character_v2",
        "//java/believe/character:dagger_module",
        "//java/believe/character/proto:character_animations_java_proto",
        "//java/believe/command",
        "//java/believe/command:impl",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/datamodel/protodata",
        "//java/believe/gamestate:controller",
        "//java/believe/gamestate:gamestate_kt",
        "//java/believe/gamestate:impl_kt",
        "//java/believe/gui",
        "//java/believe/gui:annotations",
        "//java/believe/gui:dagger_module_v2",
        "//java/believe/gui:gui_v2",
        "//java/believe/gui:package-info",
        "//java/believe/input",
        "//java/believe/input/keyboard",
        "//java/believe/io",
        "//java/believe/level",
        "//java/believe/level:dagger_module",
        "//java/believe/level:impl",
        "//java/believe/map/collidable/tile",
        "//java/believe/map/io",
        "//java/believe/map/io:dagger_module",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/mob",
        "//java/believe/mob:dagger_module",
        "//java/believe/mob/proto:mob_animation_data_java_proto",
        "//java/believe/physics/collision:impl",
        "//java/believe/physics/damage",
        "//java/believe/physics/manager",
        "//java/believe/proto:impl",
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

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "gamestate",
    srcs = [
        "GameStateBase.java",
        "GameStateRegistry.java",
    ],
    deps = [
        ":package-info",
        "//third_party/slick",
    ],
)

java_library(
    name = "impl",
    srcs = [
        "FlowFilePickerMenuState.java",
        "MainMenuState.java",
        "OptionsMenuState.java",
        "PlayFlowFileState.java",
    ],
    deps = [
        ":gamestate",
        ":package-info",
        "//java/believe/action",
        "//java/believe/app:qualifiers",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/audio",
        "//java/believe/character",
        "//java/believe/character:faction",
        "//java/believe/character/playable",
        "//java/believe/core",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/gamestate/levelstate/arcadestate",
        "//java/believe/gamestate/levelstate/platformingstate",
        "//java/believe/gamestate/temporarystate",
        "//java/believe/gamestate/temporarystate:impl",
        "//java/believe/graphicstransitions",
        "//java/believe/gui",
        "//java/believe/io",
        "//java/believe/levelFlow/component",
        "//java/believe/levelFlow/parsing",
        "//java/believe/levelFlow/parsing/exceptions",
        "//java/believe/map/gui",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "gamestate_kt",
    srcs = [
        "GameState.kt",
        "GameStateRunner.kt",
        "StateController.kt",
    ],
    deps = [
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/gamestate/transition",
    ],
)

kt_jvm_library(
    name = "impl_kt",
    srcs = [
        "GameStateRunnerImpl.kt",
        "LevelStateV2.kt",
        "MainMenuStateV2.kt",
        "OptionsMenuStateV2.kt",
        "StateControllerImpl.kt",
    ],
    deps = [
        ":gamestate_kt",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/character:character_v2",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/core/display:display_v2",
        "//java/believe/datamodel",
        "//java/believe/datamodel:impl",
        "//java/believe/gamestate/transition",
        "//java/believe/gamestate/transition:impl",
        "//java/believe/gui",
        "//java/believe/gui:gui_v2",
        "//java/believe/level",
        "//java/believe/scene:impl",
        "//third_party/auto_factory",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["GamestateDaggerModule.java"],
    deps = [
        "gamestate_kt",
        ":impl_kt",
        "//third_party/dagger",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

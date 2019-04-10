package(
    default_visibility = [
        "//java/believe/app:app_pkg",
        "//javatests/believe:believe_tests",
    ],
)

java_library(
    name = "gamestate",
    srcs = [
        "ArcadeState.java",
        "ChangeStateAction.java",
        "ChangeToTemporaryStateAction.java",
        "ExitTemporaryStateAction.java",
        "ExternalLoadGameAction.java",
        "FlowFilePickerMenuState.java",
        "GameOverState.java",
        "GamePausedOverlay.java",
        "GameStateBase.java",
        "GameStateRegistry.java",
        "LevelState.java",
        "MainMenuState.java",
        "OptionsMenuState.java",
        "OverlayablePrecedingState.java",
        "PlatformingState.java",
        "PlayFlowFileState.java",
        "PrecedingState.java",
        "TemporaryState.java",
    ],
    deps = [
        ":package-info",
        "//java/believe/app/proto:game_options_java_proto",
        "//java/believe/audio",
        "//java/believe/character",
        "//java/believe/character:faction",
        "//java/believe/character/playable",
        "//java/believe/core",
        "//java/believe/core/io",
        "//java/believe/datamodel",
        "//java/believe/graphics_transitions",
        "//java/believe/gui",
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

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//:believe_test_pkgs"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

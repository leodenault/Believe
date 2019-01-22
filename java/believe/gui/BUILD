java_library(
    name = "gui",
    srcs = [
        "AbstractContainer.java",
        "CanvasContainer.java",
        "CharacterDialogue.java",
        "ComponentBase.java",
        "DebugBeat.java",
        "DirectionalPanel.java",
        "MenuSelection.java",
        "MenuSelectionGroup.java",
        "NumberPicker.java",
        "ProgressBar.java",
        "ScrollBar.java",
        "TextComponent.java",
        "VerticalKeyboardScrollpanel.java",
    ],
    visibility = [
        "//java/believe/character:character_pkg",
        "//java/believe/gamestate:__pkg__",
        "//java/believe/levelFlow/component:__pkg__",
        "//java/believe/map/gui:__pkg__",
        "//javatests/believe:believe_tests",
    ],
    deps = [
        ":package-info",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/util",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//javatests/believe/gui:__pkg__"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

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
        "//src/believe/character:character_pkg",
        "//src/believe/gamestate:__pkg__",
        "//src/believe/levelFlow/component:__pkg__",
        "//src/believe/map/gui:__pkg__",
        "//test/believe:believe_tests",
    ],
    deps = [
        ":package-info",
        "//src/believe/core",
        "//src/believe/core/display",
        "//src/believe/geometry",
        "//src/believe/util",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//test/believe/gui:__pkg__"],
    deps = [
        "//src/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

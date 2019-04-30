package(
    default_visibility = ["//:believe_all_pkgs"],
)

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
    deps = [
        ":package-info",
        "//java/believe/audio",
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

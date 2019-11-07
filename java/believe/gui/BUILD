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
        "ImageSupplier.java",
        "MenuSelection.java",
        "MenuSelectionGroup.java",
        "NumberPicker.java",
        "ProgressBar.java",
        "ScrollBar.java",
        "TextComponent.java",
        "VerticalKeyboardScrollpanel.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//java/believe/audio",
        "//java/believe/command",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/core/io",
        "//java/believe/geometry",
        "//java/believe/util",
        "//third_party/auto_factory",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["GuiDaggerModule.java"],
    deps = [
        ":gui",
        "//third_party/dagger",
    ],
)

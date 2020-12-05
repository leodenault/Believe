load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

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
    ],
    deps = [
        ":package-info",
        "//java/believe/app/annotation",
        "//java/believe/audio",
        "//java/believe/audio:impl",
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

kt_jvm_library(
    name = "gui_v2",
    srcs = [
        "CanvasContainerV2.kt",
        "Focusable.kt",
        "FocusableGroup.kt",
        "FocusableGroupImpl.kt",
        "GuiAction.kt",
        "GuiBuilders.kt",
        "GuiElement.kt",
        "GuiKeyboardActionMap.kt",
        "GuiLayoutFactory.kt",
        "ImageSupplier.java",
        "LayoutBuilder.kt",
        "MenuSelectionStyle.kt",
        "MenuSelectionV2.kt",
        "NumberPickerV2.kt",
        "ProgressBarV2.kt",
        "TextAlignment.kt",
        "TextBox.kt",
        "TextBoxStyle.kt",
        "TextDisplay.kt",
        "VerticalContainer.kt",
    ],
    deps = [
        ":annotations",
        "//java/believe/audio",
        "//java/believe/core/display:display_v2",
        "//java/believe/geometry",
        "//java/believe/input",
        "//java/believe/react",
        "//java/believe/react:impl",
        "//java/believe/util",
        "//third_party/auto_factory",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/kotlin:kotlin_reflect",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["GuiDaggerModule.java"],
    deps = [
        ":gui",
        ":package-info",
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_module_v2",
    srcs = ["GuiDaggerModuleV2.java"],
    deps = [
        ":annotations",
        ":gui_v2",
        ":package-info",
        "//java/believe/audio",
        "//java/believe/audio:impl",
        "//java/believe/core/io",
        "//java/believe/input",
        "//java/believe/input/keyboard",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "annotations",
    srcs = ["Annotations.kt"],
    deps = ["//third_party/javax:annotations"],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

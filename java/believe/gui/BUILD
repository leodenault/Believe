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
        "package-info.java",
    ],
    deps = [
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
        "Bindable.kt",
        "Focusable.kt",
        "FocusableGroup.kt",
        "FocusableGroupImpl.kt",
        "GuiAction.kt",
        "GuiBuilders.kt",
        "GuiContainer.kt",
        "GuiElement.kt",
        "GuiKeyboardActionMap.kt",
        "GuiLayoutFactory.kt",
        "LayoutBuilder.kt",
        "MenuSelectionStyle.kt",
        "MenuSelectionV2.kt",
        "TextAlignment.kt",
        "TextBox.kt",
        "TextBoxStyle.kt",
    ],
    deps = [
        ":internal_qualifiers",
        "//java/believe/audio",
        "//java/believe/core/display",
        "//java/believe/geometry",
        "//java/believe/input",
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
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_module_v2",
    srcs = ["GuiDaggerModuleV2.java"],
    deps = [
        ":gui_v2",
        ":internal_qualifiers",
        "//java/believe/audio",
        "//java/believe/audio:impl",
        "//java/believe/input",
        "//java/believe/input/keyboard",
        "//java/believe/util",
        "//third_party/dagger",
    ],
)

kt_jvm_library(
    name = "internal_qualifiers",
    srcs = [
        "FocusSound.kt",
        "GuiConfigurations.kt",
        "SelectedSound.kt",
    ],
    deps = ["//third_party/javax:annotations"],
)

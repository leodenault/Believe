load("//bazel/bzl:rules.bzl", "kt_junit5_test")

java_test(
    name = "CharacterDialogueTest",
    srcs = ["CharacterDialogueTest.java"],
    deps = [
        "//java/believe/core/io/testing",
        "//java/believe/gui",
        "//java/believe/gui/testing",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/slick",
    ],
)

java_test(
    name = "DirectionalPanelTest",
    srcs = ["DirectionalPanelTest.java"],
    deps = [
        "//java/believe/gui",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/slick",
    ],
)

kt_junit5_test(
    name = "FocusableGroupTest",
    srcs = ["FocusableGroupImplTest.kt"],
    friends = ["//java/believe/gui:gui_v2"],
    deps = [
        "//java/believe/gui:gui_v2",
        "//java/believe/input/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "GuiContainerTest",
    srcs = ["GuiContainerTest.kt"],
    friends = ["//java/believe/gui:gui_v2"],
    deps = [
        "//java/believe/gui:gui_v2",
        "//java/believe/gui/testing",
        "//java/believe/gui/testing:dagger_component",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "GuiLayoutFactoryTest",
    srcs = ["GuiLayoutFactoryTest.kt"],
    friends = ["//java/believe/gui:gui_v2"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/gui:gui_v2",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

java_test(
    name = "MenuSelectionGroupTest",
    srcs = ["MenuSelectionGroupTest.java"],
    deps = [
        "//java/believe/gui",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
    ],
)

kt_junit5_test(
    name = "MenuSelectionV2Test",
    srcs = ["MenuSelectionV2Test.kt"],
    friends = ["//java/believe/gui:gui_v2"],
    deps = [
        "//java/believe/audio",
        "//java/believe/geometry",
        "//java/believe/gui:gui_v2",
        "//java/believe/gui/testing:dagger_component",
        "//java/believe/input/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

java_test(
    name = "NumberPickerTest",
    srcs = ["NumberPickerTest.java"],
    deps = [
        "//java/believe/audio/testing",
        "//java/believe/gui",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/slick",
    ],
)

kt_junit5_test(
    name = "TextAlignmentTest",
    srcs = ["TextAlignmentTest.kt"],
    friends = ["//java/believe/gui:gui_v2"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/gui:gui_v2",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TextBoxTest",
    srcs = ["TextBoxTest.kt"],
    friends = ["//java/believe/gui:gui_v2"],
    deps = [
        "//java/believe/gui:gui_v2",
        "//java/believe/gui/testing:dagger_component",
        "//third_party/mockito:mockito_kotlin",
    ],
)

load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

package(
    default_testonly = 1,
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "testing",
    srcs = [
        "FakeGuiContext.kt",
        "FakeImage.kt",
        "FakeLayoutBuilder.kt",
    ],
    deps = [
        "//java/believe/core",
        "//java/believe/geometry",
        "//java/believe/gui:gui_v2",
        "//third_party/dagger",
        "//third_party/lwjgl",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["GuiTestDaggerModule.java"],
    deps = [
        ":internal_qualifiers",
        ":testing",
        "//java/believe/audio",
        "//java/believe/audio/testing",
        "//java/believe/gui:annotations",
        "//java/believe/gui:gui_v2",
        "//java/believe/input",
        "//java/believe/input/testing",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["GuiTestComponent.java"],
    deps = [
        ":dagger_module",
        ":internal_qualifiers",
        ":testing",
        "//java/believe/audio",
        "//java/believe/gui:gui_v2",
        "//java/believe/input",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "internal_qualifiers",
    srcs = ["TestGuiConfigurations.kt"],
    deps = ["//third_party/javax:annotations"],
)

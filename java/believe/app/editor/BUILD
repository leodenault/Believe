package(
    default_visibility = ["//:__pkg__"],
)

java_library(
    name = "editor",
    srcs = [
        "LevelEditor.java",
    ],
    deps = [
        ":dagger_component",
        ":package-info",
        "//java/believe/app",
        "//java/believe/app:util",
        "//java/believe/app/flag_parsers",
        "//java/believe/app/flags",
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/map/gui",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["LevelEditorComponent.java"],
    visibility = ["//visibility:private"],
    deps = [
        ":dagger_module",
        ":package-info",
        "//java/believe/app:dagger_component",
        "//third_party/dagger",
        "//third_party/javax:annotations",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["LevelEditorModule.java"],
    deps = [
        ":package-info",
        "//java/believe/app:dagger_module",
        "//java/believe/app:qualifiers",
        "//java/believe/gamestate",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/slick",
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

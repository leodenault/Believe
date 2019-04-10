package(
    default_visibility = [
        "//:__pkg__",
        "//javatests/believe:believe_tests",
    ],
)

java_library(
    name = "game",
    srcs = ["Believe.java"],
    deps = [
        "package-info",
        ":dagger_module",
        "//java/believe/app",
        "//java/believe/app:dagger_module",
        "//java/believe/app/game:dagger_component",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["BelieveComponent.java"],
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
    srcs = ["BelieveGameModule.java"],
    visibility = ["//:believe_all_pkgs"],
    deps = [
        ":package-info",
        "//java/believe/app",
        "//java/believe/app:dagger_module",
        "//java/believe/app:qualifiers",
        "//java/believe/app/flag_parsers",
        "//java/believe/app/flags",
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/dagger",
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

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "platformingstate",
    srcs = [
        "PlatformingState.java",
    ],
    deps = [
        ":package-info",
        ":qualifiers",
        "//java/believe/audio",
        "//java/believe/character/playable",
        "//java/believe/core",
        "//java/believe/core/io",
        "//java/believe/datamodel:impl",
        "//java/believe/gamestate/levelstate",
        "//java/believe/levelFlow/component",
        "//java/believe/map/data",
        "//java/believe/map/gui",
        "//java/believe/map/io",
        "//java/believe/physics/manager",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "qualifiers",
    srcs = ["EventActions.java"],
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

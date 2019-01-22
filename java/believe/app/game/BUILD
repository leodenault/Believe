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
        ":package-info",
        "//java/believe/app",
        "//java/believe/app/flag_parsers",
        "//java/believe/app/flags",
        "//java/believe/gamestate",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/javax:annotations",
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

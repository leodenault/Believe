package(
    default_visibility = [
        "//:__pkg__",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "game",
    srcs = ["Believe.java"],
    deps = [
        ":package-info",
        "//src/believe/app",
        "//src/believe/app/flag_parsers",
        "//src/believe/app/flags",
        "//src/believe/gamestate",
        "//src/believe/util",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    visibility = ["//:believe_test_pkgs"],
    deps = [
        "//src/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

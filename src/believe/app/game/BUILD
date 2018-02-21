package(
    default_visibility = [
        "//:__pkg__",
        "//test/believe:believe_tests",
    ],
)

java_library(
    name = "game",
    srcs = glob(["*.java"]),
    deps = [
        "//src/believe/app",
        "//src/believe/app/flag_parsers",
        "//src/believe/gamestate",
        "//src/believe/util",
    ],
)

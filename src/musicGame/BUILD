package_group(
    name = "believe_pkg",
    packages = ["//src/musicGame/..."],
)

java_library(
    name = "believe_src",
    srcs = glob(["*.java"]),
    visibility = ["//:__pkg__"],
    deps = [
        "//lib",
        "//src/musicGame/core",
        "//src/musicGame/gamestate",
    ],
)

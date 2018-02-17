package_group(
    name = "believe_pkg",
    packages = ["//src/believe/..."],
)

java_library(
    name = "believe_src",
    srcs = glob(["*.java"]),
    visibility = ["//:__pkg__"],
    deps = [
        "//lib",
        "//src/believe/core",
        "//src/believe/gamestate",
    ],
)

package(
    default_visibility = [
        "//:__pkg__",
        "//src/believe/app:app_pkg",
        "//test/believe:believe_tests",
    ],
)

package_group(
    name = "app_pkg",
    packages = ["//src/believe/app/..."],
)

java_library(
    name = "app",
    srcs = glob(["*.java"]),
    deps = [
        "//lib",
        "//src/believe/core/io",
        "//src/believe/gamestate",
        "//src/believe/util",
    ],
)

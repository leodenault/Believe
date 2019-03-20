package(
    default_visibility = [
        "//:__pkg__",
        "//java/believe/app:app_pkg",
        "//javatests/believe:believe_tests",
    ],
)

package_group(
    name = "app_pkg",
    packages = ["//java/believe/app/..."],
)

java_library(
    name = "app",
    srcs = [
        "AppUtil.java",
        "ApplicationComponent.java",
        "Launcher.java",
        "StateInstantiator.java",
    ],
    deps = [
        "//java/believe/core/io",
        "//java/believe/gamestate",
        "//java/believe/util",
        "//third_party/slick",
    ],
)

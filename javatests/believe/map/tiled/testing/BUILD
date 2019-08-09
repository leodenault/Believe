load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "FakeTiledMapTest",
    srcs = ["FakeTiledMapTest.java"],
    deps = [
        "//java/believe/map/tiled/testing",
        "//third_party/truth:truth8",
    ],
)

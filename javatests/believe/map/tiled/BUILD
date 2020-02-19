load("//bazel/bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "EntityTypeTest",
    srcs = ["EntityTypeTest.java"],
    deps = [
        "//java/believe/map/tiled",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "TileTest",
    srcs = ["TileTest.java"],
    deps = [
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//java/believe/testing/mockito",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "TiledUtilTest",
    srcs = ["TiledUtilTest.java"],
    deps = [
        "//java/believe/map/tiled",
        "//third_party/truth",
    ],
)

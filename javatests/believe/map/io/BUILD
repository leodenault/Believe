load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "TiledMapLayerParserImplTest",
    srcs = ["TiledMapLayerParserImplTest.java"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/physics/manager",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "TiledMapParserTest",
    srcs = ["TiledMapParserTest.java"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/tiled",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

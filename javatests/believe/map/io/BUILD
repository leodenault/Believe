load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "MapManagerImplTest",
    srcs = ["MapManagerImplTest.java"],
    data = [
        "//javatests/believe/map/io/data",
        "//javatests/believe/map/io/data:test_map_textproto",
    ],
    deps = [
        "//java/believe/gui",
        "//java/believe/gui/testing",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/io",
        "//java/believe/map/io:impl",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/slick",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "TiledMapLayerParserImplTest",
    srcs = ["TiledMapLayerParserImplTest.java"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/physics/manager",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "TiledMapParserImplTest",
    srcs = ["TiledMapParserImplTest.java"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/io",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

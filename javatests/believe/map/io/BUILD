load("//bazel/bzl:rules.bzl", "java_junit5_test", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "BackgroundSceneParserImplTest",
    srcs = ["BackgroundSceneParserImplTest.kt"],
    friends = [],
    deps = [
        "//java/believe/gui/testing",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/io:impl",
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
        "//java/believe/map/tiled:impl",
        "//java/believe/map/tiled/testing",
        "//java/believe/physics/manager",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "TiledMapObjectLayerParserImplTest",
    srcs = ["TiledMapObjectLayerParserImplTest.java"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled:impl",
        "//java/believe/map/tiled/testing",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "MapMetadataParserImplTest",
    srcs = ["MapMetadataParserImplTest.java"],
    deps = [
        "//java/believe/gui/testing",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/data",
        "//java/believe/map/data/proto:map_metadata_java_proto",
        "//java/believe/map/io",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/slick",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "TiledMapParserTest",
    srcs = ["TiledMapParserTest.java"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/io",
        "//java/believe/map/io:impl",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled:impl",
        "//java/believe/map/tiled/testing",
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

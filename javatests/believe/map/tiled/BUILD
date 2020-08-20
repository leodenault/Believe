load("//bazel/bzl:rules.bzl", "java_junit5_test")
load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "ElementParserTest",
    srcs = ["ElementParserTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "ObjectTemplateManagerTest",
    srcs = ["ObjectTemplateManagerTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/io",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PartialTiledObjectTest",
    srcs = ["PartialTiledObjectTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PropertiesTest",
    srcs = ["PropertiesTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TiledObjectGroupTest",
    srcs = ["TiledObjectGroupTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TiledObjectTest",
    srcs = ["TiledObjectTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/io",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TileSetGroupTest",
    srcs = ["TileSetGroupTest.kt"],
    friends = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

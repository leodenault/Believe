load("//bazel/bzl:rules.bzl", "java_junit5_test")
load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "ElementParserTest",
    srcs = ["ElementParserTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "ObjectTemplateManagerTest",
    srcs = ["ObjectTemplateManagerTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/io",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PartialTiledObjectTest",
    srcs = ["PartialTiledObjectTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PartialTileSetTest",
    srcs = ["PartialTileSetTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/gui/testing",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PropertiesTest",
    srcs = ["PropertiesTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
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
    associates = ["//java/believe/map/tiled:impl"],
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
    associates = ["//java/believe/map/tiled:impl"],
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
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TileSetManagerTest",
    srcs = ["TileSetManagerTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/datamodel",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TileSetTest",
    srcs = ["TileSetTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/datamodel",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "TileTest",
    srcs = ["TileTest.kt"],
    associates = ["//java/believe/map/tiled:impl"],
    deps = [
        "//java/believe/gui/testing",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

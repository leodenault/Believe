load("//bazel/bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "CollidableTileCollisionHandlerTest",
    srcs = ["CollidableTileCollisionHandlerTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/map/collidable/tile",
        "//java/believe/map/data",
        "//java/believe/map/tiled/testing",
        "//java/believe/physics/collision",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "CollidableTileGeneratorTest",
    srcs = ["CollidableTileParserTest.java"],
    deps = [
        "//java/believe/map/collidable/tile",
        "//java/believe/map/data",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled:impl",
        "//java/believe/map/tiled/testing",
        "//java/believe/physics/manager",
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "CollidableTileTest",
    srcs = ["CollidableTileTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/map/collidable/tile",
        "//java/believe/map/data",
        "//java/believe/map/tiled/testing",
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

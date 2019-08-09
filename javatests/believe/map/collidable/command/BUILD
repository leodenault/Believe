load("//bzl:rules.bzl", "java_junit5_test")

java_junit5_test(
    name = "CommandGeneratorTest",
    srcs = ["CommandGeneratorTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:package-info",
        "//java/believe/map/data",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//java/believe/physics/manager",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/slick",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "CommandTest",
    srcs = ["CommandTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:package-info",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//java/believe/physics/collision",
        "//java/believe/physics/collision/testing",
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

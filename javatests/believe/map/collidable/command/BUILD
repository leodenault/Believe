load("//bazel/bzl:rules.bzl", "java_junit5_test")

java_junit5_test(
    name = "CollidableCommandCollisionHandlerTest",
    srcs = ["CollidableCommandCollisionHandlerTest.java"],
    deps = [
        "//java/believe/geometry",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:package-info",
        "//java/believe/physics/collision/testing",
        "//java/believe/physics/manager",
        "//java/believe/testing/mockito",
        "//java/believe/util",
        "//third_party/mockito",
        "//third_party/truth",
    ],
)

java_junit5_test(
    name = "CollidableCommandGeneratorTest",
    srcs = ["CollidableCommandGeneratorTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/core",
        "//java/believe/geometry",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:package-info",
        "//java/believe/map/data",
        "//java/believe/map/tiled",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/javax:annotations",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "CollidableCommandTest",
    srcs = ["CollidableCommandTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/geometry",
        "//java/believe/map/collidable/command",
        "//java/believe/map/collidable/command:package-info",
        "//java/believe/physics/collision",
        "//java/believe/util",
        "//third_party/truth",
    ],
)

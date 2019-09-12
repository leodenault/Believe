load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = 1,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "DialogueCommandCollisionHandlerTest",
    srcs = ["DialogueCommandCollisionHandlerTest.java"],
    deps = [
        "//java/believe/character/playable",
        "//java/believe/dialogue",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/map/collidable/command",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//java/believe/react:impl",
        "//java/believe/testing/mockito",
        "//third_party/mockito",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "DialogueSupplierTest",
    srcs = ["DialogueSupplierTest.java"],
    deps = [
        "//java/believe/character/playable",
        "//java/believe/dialogue",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/collidable/command",
        "//java/believe/map/tiled",
        "//java/believe/map/tiled/testing",
        "//java/believe/react:impl",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

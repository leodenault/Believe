load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "StationaryEnemyParserTest",
    srcs = ["StationaryEnemyParserTest.kt"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/tiled/testing",
        "//java/believe/mob",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "StationaryEnemyStateMachineTest",
    srcs = ["StationaryEnemyStateMachineTest.kt"],
    deps = [
        "//java/believe/animation/testing",
        "//java/believe/core",
        "//java/believe/gui/testing",
        "//java/believe/mob",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "StationaryEnemyTest",
    srcs = ["StationaryEnemyTest.kt"],
    deps = [
        "//java/believe/animation/testing",
        "//java/believe/core/display",
        "//third_party/mockito:mockito_kotlin",
    ],
)

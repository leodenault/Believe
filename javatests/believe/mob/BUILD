load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "MobAnimationDataManagerTest",
    srcs = ["MobAnimationDataManagerTest.kt"],
    associates = ["//java/believe/mob"],
    deps = [
        "//java/believe/animation",
        "//java/believe/animation/proto:animation_java_proto",
        "//java/believe/animation/testing",
        "//java/believe/datamodel",
        "//java/believe/mob/proto:mob_animation_data_java_proto",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "StationaryEnemyParserTest",
    srcs = ["StationaryEnemyParserTest.kt"],
    associates = ["//java/believe/mob"],
    deps = [
        "//java/believe/map/data",
        "//java/believe/map/data/testing",
        "//java/believe/map/tiled/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "StationaryEnemyTest",
    srcs = ["StationaryEnemyTest.kt"],
    associates = ["//java/believe/mob"],
    deps = [
        "//java/believe/animation/testing",
        "//java/believe/core",
        "//java/believe/core/display",
        "//java/believe/geometry/truth",
        "//java/believe/gui/testing",
        "//java/believe/physics/damage",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "AnimationTest",
    srcs = ["AnimationTest.kt"],
    deps = [
        "//java/believe/animation/testing",
        "//java/believe/gui/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "CompoundAnimationTest",
    srcs = ["CompoundAnimationTest.kt"],
    deps = [
        "//java/believe/animation/testing",
        "//java/believe/gui/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "FakeSpriteSheetTest",
    srcs = ["FakeSpriteSheetTest.kt"],
    deps = [
        "//java/believe/animation/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "SpriteSheetDataParserTest",
    srcs = ["SpriteSheetDataParserTest.kt"],
    deps = [
        "//java/believe/animation",
        "//java/believe/animation/proto:animation_java_proto",
        "//java/believe/animation/testing",
        "//java/believe/datamodel",
        "//java/believe/gui/testing",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

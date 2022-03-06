load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "CharacterStateMachineTest",
    srcs = ["CharacterStateMachineTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//java/believe/input/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "HorizontalMovementStateMachineTest",
    srcs = ["HorizontalMovementStateMachineTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//java/believe/input/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "MovementStateMachineTest",
    srcs = ["MovementStateMachineTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//java/believe/input/testing",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "OrientationStateMachineTest",
    srcs = ["OrientationStateMachineTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//java/believe/input/testing",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "PlayerObjectParserTest",
    srcs = ["PlayerObjectParserTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//java/believe/datamodel",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/map/data",
        "//java/believe/map/data/testing",
        "//java/believe/map/tiled/testing",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "VerticalMovementHandlerTest",
    srcs = ["VerticalMovementHandlerTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//java/believe/input/testing",
        "//third_party/slick",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "VulnerabilityStateMachineTest",
    srcs = ["VulnerabilityStateMachineTest.kt"],
    associates = ["//java/believe/character:character_v2"],
    deps = [
        "//third_party/truth",
    ],
)

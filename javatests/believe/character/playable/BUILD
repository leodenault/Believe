load("//bazel/bzl:rules.bzl", "java_junit5_test", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_test(
    name = "DamageProjectionTest",
    srcs = ["DamageProjectionTest.java"],
    deps = [
        "//java/believe/character",
        "//java/believe/character/playable",
        "//java/believe/character/playable:package-info",
        "//java/believe/physics/manager",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/slick",
    ],
)

kt_junit5_test(
    name = "PlayableCharacterMovementCommandParserTest",
    srcs = ["PlayableCharacterMovementCommandParserTest.kt"],
    associates = [],
    deps = [
        "//java/believe/character/playable",
        "//java/believe/character/proto:playable_character_movement_command_java_proto",
        "//java/believe/command",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//third_party/mockito:mockito_kotlin",
    ],
)

java_junit5_test(
    name = "PlayableCharacterMovementCommandTest",
    srcs = ["PlayableCharacterMovementCommandTest.java"],
    deps = [
        "//java/believe/character/playable",
        "//java/believe/character/playable:package-info",
        "//java/believe/character/proto:playable_character_movement_command_java_proto",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/statemachine",
        "//third_party/mockito",
    ],
)

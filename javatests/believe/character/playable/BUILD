load("//bzl:rules.bzl", "java_junit5_test")

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

java_junit5_test(
    name = "PlayableCharacterMovementCommandTest",
    srcs = ["PlayableCharacterMovementCommandTest.java"],
    deps = [
        "//java/believe/character/playable",
        "//java/believe/character/playable:package-info",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/statemachine",
        "//third_party/mockito",
    ],
)

load("//bazel/bzl:rules.bzl", "kt_junit5_test")

java_test(
    name = "ConcurrentStateMachineTest",
    srcs = ["ConcurrentStateMachineTest.java"],
    deps = [
        "//java/believe/statemachine",
        "//java/believe/util",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
    ],
)

kt_junit5_test(
    name = "ConcurrentStateMachineV2Test",
    srcs = ["ConcurrentStateMachineV2Test.kt"],
    friends = [],
    deps = [
        "//java/believe/statemachine:statemachine_v2",
        "//third_party/truth",
    ],
)

java_test(
    name = "EntityStateMachineTest",
    srcs = ["EntityStateMachineTest.java"],
    deps = [
        "//java/believe/statemachine",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
    ],
)

kt_junit5_test(
    name = "StateMachineV2Test",
    srcs = ["StateMachineV2Test.kt"],
    friends = [],
    deps = [
        "//java/believe/statemachine:statemachine_v2",
        "//third_party/truth",
    ],
)

java_test(
    name = "StateTest",
    srcs = ["StateTest.java"],
    deps = [
        "//java/believe/statemachine",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
    ],
)

java_test(
    name = "TransitionTest",
    srcs = ["TransitionTest.java"],
    deps = [
        "//java/believe/statemachine",
        "//third_party/hamcrest",
        "//third_party/junit",
        "//third_party/mockito",
    ],
)

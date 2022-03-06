load("//bazel/bzl:rules.bzl", "kt_junit5_test")

kt_junit5_test(
    name = "ConcurrentStateMachineParserTest",
    srcs = ["ConcurrentStateMachineParserTest.kt"],
    associates = ["//java/believe/statemachine/parser"],
    deps = [
        "//java/believe/statemachine/proto:state_machine_java_proto",
        "//java/believe/statemachine/proto/testing:fake_state_machine_java_proto",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "StateMachineParserTest",
    srcs = ["StateMachineParserTest.kt"],
    associates = ["//java/believe/statemachine/parser"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/statemachine/proto:state_machine_java_proto",
        "//java/believe/statemachine/proto/testing:fake_state_machine_java_proto",
        "//third_party/truth",
    ],
)

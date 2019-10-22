load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = 1,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "DialogueCommandCollisionHandlerTest",
    srcs = ["DialogueCommandTest.java"],
    deps = [
        "//java/believe/dialogue",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/react:impl",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "DialogueSupplierTest",
    srcs = ["DialogueSupplierTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/dialogue",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/react:impl",
        "//third_party/truth:truth8",
    ],
)

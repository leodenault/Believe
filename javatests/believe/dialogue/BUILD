load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = 1,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "DialogueManagerTest",
    srcs = ["DialogueManagerTest.java"],
    deps = [
        "//java/believe/dialogue",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/temporaryfolder",
        "//third_party/junit",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

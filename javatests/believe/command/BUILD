load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "CommandGeneratorImplTest",
    srcs = ["CommandGeneratorImplTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/command:impl",
        "//java/believe/command:package_info",
        "//java/believe/core",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/util",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "CommandSequenceParserImplTest",
    srcs = ["CommandSequenceParserImplTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/command:impl",
        "//java/believe/command:package_info",
        "//java/believe/command/proto:command_sequence_java_proto",
        "//java/believe/core",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/mockito",
        "//third_party/javax:annotations",
        "//third_party/mockito",
        "//third_party/proto",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "CommandSequenceSupplierTest",
    srcs = ["CommandSequenceSupplierTest.java"],
    deps = [
        "//java/believe/command",
        "//java/believe/command:impl",
        "//java/believe/command:package_info",
        "//java/believe/command/proto:command_sequence_java_proto",
        "//java/believe/core",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//third_party/proto",
        "//third_party/truth:truth8",
    ],
)

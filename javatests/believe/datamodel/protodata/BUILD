load("//bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "BinaryProtoFileTest",
    srcs = ["BinaryProtoFileTest.kt"],
    deps = [
        "//java/believe/datamodel/protodata",
        "//java/believe/io/testing",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/proto:test_java_proto",
        "//third_party/proto",
        "//third_party/truth",
    ],
)

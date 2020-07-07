load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "BinaryProtoFileManagerTest",
    srcs = ["BinaryProtoFileManagerTest.kt"],
    deps = [
        "//java/believe/datamodel/protodata",
        "//java/believe/datamodel/protodata/testing",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/proto/testing/proto:fake_java_proto",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/truth",
    ],
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
        "//third_party/mockito:mockito_kotlin",
        "//third_party/proto",
        "//third_party/truth",
    ],
)

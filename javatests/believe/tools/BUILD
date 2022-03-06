load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "ProtoFileSerializerTest",
    srcs = ["ProtoFileSerializerTest.kt"],
    associates = ["//java/believe/tools:proto_file_serializer"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/proto/testing",
        "//java/believe/testing/proto:test_java_proto",
        "//java/believe/testing/temporaryfolder",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/proto",
        "//third_party/truth",
    ],
)

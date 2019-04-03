load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "ProtoFileSerializerTest",
    srcs = ["ProtoFileSerializerTest.java"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/mockito",
        "//java/believe/testing/proto:test_java_proto",
        "//java/believe/testing/temporaryfolder",
        "//java/believe/tools:proto_file_serializer",
        "//third_party/junit",
        "//third_party/mockito",
        "//third_party/proto",
        "//third_party/truth",
    ],
)

load("//bazel/bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "TextProtoParserTest",
    srcs = ["TextProtoParserTest.kt"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/proto",
        "//java/believe/proto/testing/proto:fake_java_proto",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "ProtoParserImplTest",
    srcs = ["ProtoParserImplTest.kt"],
    friends = ["//java/believe/proto:impl"],
    deps = [
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/proto:impl",
        "//java/believe/proto/testing/proto:fake_java_proto",
        "//third_party/proto",
        "//third_party/truth",
    ],
)

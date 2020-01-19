load("//bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "FakeTextProtoParserFactoryTest",
    srcs = ["FakeTextProtoParserFactoryFactoryTest.kt"],
    deps = [
        "//java/believe/proto/testing",
        "//java/believe/proto/testing/proto:fake_java_proto",
        "//third_party/proto",
        "//third_party/truth",
    ],
)

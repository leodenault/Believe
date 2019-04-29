load("//bzl:rules.bzl", "java_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

java_junit5_test(
    name = "BinaryProtoFileUtilTest",
    srcs = ["BinaryProtoFileUtilTest.java"],
    deps = [
        "//java/believe/datamodel/protodata",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//java/believe/testing/proto:test_java_proto",
        "//java/believe/testing/temporaryfolder",
        "//third_party/junit",
        "//third_party/proto",
        "//third_party/truth",
        "//third_party/truth:truth8",
    ],
)

java_junit5_test(
    name = "ProtoDataProviderTest",
    srcs = ["MutableProtoDataCommitterTest.java"],
    deps = [
        "//java/believe/datamodel/protodata",
        "//java/believe/testing/proto:test_java_proto",
        "//java/believe/testing/temporaryfolder",
        "//third_party/junit",
        "//third_party/truth",
    ],
)

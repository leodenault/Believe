load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "testing",
    srcs = ["FakeTextProtoParserFactoryFactory.kt"],
    deps = [
        "//java/believe/proto",
        "//java/believe/proto/testing/proto:fake_java_proto",
        "//third_party/mockito:mockito_kotlin",
        "//third_party/proto",
    ],
)

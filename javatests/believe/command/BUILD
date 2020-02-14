load("//bzl:rules.bzl", "kt_junit5_test")

package(
    default_testonly = True,
    default_visibility = ["//visibility:private"],
)

kt_junit5_test(
    name = "CommandGeneratorImplTest",
    srcs = ["CommandGeneratorImplTest.kt"],
    friends = ["//java/believe/command:impl"],
    deps = [
        "//java/believe/command:impl",
        "//java/believe/command/proto:command_java_proto",
        "//java/believe/command/testing/proto:fake_command_java_proto",
        "//java/believe/logging/testing",
        "//java/believe/logging/truth",
        "//third_party/kotlin:kotlin_reflect",
        "//third_party/truth",
    ],
)

kt_junit5_test(
    name = "CommandSequenceParserTest",
    srcs = ["CommandSequenceParserTest.kt"],
    friends = ["//java/believe/command:impl"],
    deps = [
        "//java/believe/command",
        "//java/believe/command:impl",
        "//java/believe/command/proto:command_java_proto",
        "//third_party/dagger",
        "//third_party/truth",
    ],
)

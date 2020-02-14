package(
    default_testonly = True,
    default_visibility = ["//:believe_test_pkgs"],
)

proto_library(
    name = "fake_command_proto",
    srcs = ["fake_command.proto"],
    deps = ["//java/believe/command/proto:command_proto"],
)

java_proto_library(
    name = "fake_command_java_proto",
    deps = [":fake_command_proto"],
)

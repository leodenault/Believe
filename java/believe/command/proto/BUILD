package(
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "command_proto",
    srcs = ["command.proto"],
)

java_proto_library(
    name = "command_java_proto",
    deps = [":command_proto"],
)

package(
    default_visibility = [
        "//:believe_all_pkgs",
    ],
)

proto_library(
    name = "dialogue_proto",
    srcs = ["dialogue.proto"],
    deps = ["//java/believe/command/proto:command_proto"],
)

java_proto_library(
    name = "dialogue_java_proto",
    deps = [":dialogue_proto"],
)

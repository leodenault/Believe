package(
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "state_machine_proto",
    srcs = ["state_machine.proto"],
)

java_proto_library(
    name = "state_machine_java_proto",
    deps = [":state_machine_proto"],
)

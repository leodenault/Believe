package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "fake_state_machine_proto",
    srcs = ["fake_state_machine.proto"],
    deps = ["//java/believe/statemachine/proto:state_machine_proto"],
)

java_proto_library(
    name = "fake_state_machine_java_proto",
    deps = [":fake_state_machine_proto"],
)

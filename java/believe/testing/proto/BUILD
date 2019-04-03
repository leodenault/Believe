package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "test_proto",
    srcs = ["test_proto.proto"],
)

java_proto_library(
    name = "test_java_proto",
    deps = [":test_proto"],
)

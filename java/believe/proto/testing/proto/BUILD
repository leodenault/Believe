package(
    default_testonly = True,
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "fake_proto",
    srcs = ["fake_proto.proto"],
)

java_proto_library(
    name = "fake_java_proto",
    deps = [":fake_proto"],
)

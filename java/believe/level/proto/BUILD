package(
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "level_proto",
    srcs = ["level.proto"],
    deps = [
        "//java/believe/command/proto:command_proto",
        "//java/believe/map/data/proto:map_metadata_proto",
    ],
)

java_proto_library(
    name = "level_java_proto",
    deps = [":level_proto"],
)

package(
    default_visibility = ["//:believe_all_pkgs"],
)

proto_library(
    name = "playable_character_movement_command_proto",
    srcs = ["playable_character_movement_command.proto"],
    deps = ["//java/believe/command/proto:command_proto"],
)

java_proto_library(
    name = "playable_character_movement_command_java_proto",
    deps = [":playable_character_movement_command_proto"],
)

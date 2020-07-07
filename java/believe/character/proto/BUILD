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

proto_library(
    name = "character_animations_proto",
    srcs = ["character_animations.proto"],
    deps = ["//java/believe/statemachine/proto:state_machine_proto"],
)

java_proto_library(
    name = "character_animations_java_proto",
    deps = [":character_animations_proto"],
)

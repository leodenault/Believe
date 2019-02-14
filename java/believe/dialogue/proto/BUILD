package(
    default_visibility = [
        "//:believe_test_pkgs",
        "//java/believe/dialogue:__subpackages__",
    ],
)

proto_library(
    name = "dialogue_proto",
    srcs = ["dialogue.proto"],
)

java_proto_library(
    name = "dialogue_java_proto",
    deps = [":dialogue_proto"],
)

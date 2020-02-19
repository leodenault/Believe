load("//bazel/bzl:rules.bzl", "textproto")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

textproto(
    name = "data",
    srcs = glob(["*.textproto"]),
    java_outer_class_name = "believe.level.proto.LevelProto",
    proto_message = "Level",
    deps = ["//java/believe/level/proto:level_java_proto"],
)

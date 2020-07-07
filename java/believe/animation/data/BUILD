load("//bazel/bzl:rules.bzl", "textproto")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

textproto(
    name = "data",
    srcs = glob(["*.textproto"]),
    java_outer_class_name = "believe.animation.proto.AnimationProto",
    proto_message = "SpriteSheet",
    deps = ["//java/believe/animation/proto:animation_java_proto"],
)

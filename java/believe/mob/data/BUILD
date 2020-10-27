load("//bazel/bzl:rules.bzl", "textproto")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

textproto(
    name = "data",
    srcs = glob(["*.textproto"]),
    java_outer_class_name = "believe.mob.proto.MobAnimationDataProto",
    proto_message = "MobAnimationData",
    deps = ["//java/believe/mob/proto:mob_animation_data_java_proto"],
)

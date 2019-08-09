load("//bzl:rules.bzl", "textproto")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

textproto(
    name = "dialogue_textproto",
    srcs = ["dialogue.textproto"],
    java_outer_class_name = "believe.dialogue.proto.DialogueProto",
    proto_message = "DialogueMap",
    deps = ["//java/believe/dialogue/proto:dialogue_java_proto"],
)

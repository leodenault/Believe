load("//bzl:rules.bzl", "textproto")

package(
    default_testonly = True,
    default_visibility = ["//javatests/believe/map/io:__subpackages__"],
)

filegroup(
    name = "data",
    srcs = [
        "fake_image.png",
        "test_map.tmx",
    ],
)

textproto(
    name = "test_map_textproto",
    srcs = ["test_map.textproto"],
    java_outer_class_name = "believe.map.data.proto.MapMetadataProto",
    proto_message = "MapMetadata",
    deps = ["//java/believe/map/data/proto:map_metadata_java_proto"],
)

load("//bzl:rules.bzl", "textproto")

filegroup(
    name = "maps",
    srcs = [
        ":map_files",
        ":map_files_textprotos",
        ":tilesets",
    ],
    visibility = [
        "//:__pkg__",
        "//res:__pkg__",
    ],
)

filegroup(
    name = "map_files",
    srcs = glob(["*.tmx"]),
)

filegroup(
    name = "tilesets",
    srcs = glob(["tilesets/*"]),
)

textproto(
    name = "map_files_textprotos",
    srcs = glob(["*.textproto"]),
    java_outer_class_name = "believe.map.data.proto.MapMetadataProto",
    proto_message = "MapMetadata",
    deps = ["//java/believe/map/data/proto:map_metadata_java_proto"],
)

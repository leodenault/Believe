filegroup(
    name = "maps",
    srcs = [
        ":map_files",
        ":object_templates",
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

filegroup(
    name = "object_templates",
    srcs = glob(["object_templates/*"]),
)

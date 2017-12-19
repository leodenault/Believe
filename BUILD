load("//rules:rules.bzl", "believe_binary")

java_library(
    name = "believe_lib",
    srcs = glob(["src/**/*.java"]),
    data = [
        "//customFlowFiles:custom_flow_files",
        "//customSongs:custom_songs",
    ],
    resources = [
        "//data",
        "//levelFlowFiles:level_flow_files",
        "//res",
    ],
    deps = [":libs"],
)

believe_binary(
    name = "Believe",
    dep = ":believe_lib",
    main_class = "musicGame.Main",
)

java_import(
    name = "libs",
    data = [
        ":natives",
        "//customFlowFiles:custom_flow_files",
        "//customSongs:custom_songs",
    ],
    jars = glob(["lib/*.jar"]),
)

filegroup(
    name = "natives",
    srcs = glob(["lib/native/*"]),
)

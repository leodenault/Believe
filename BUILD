java_binary(
    name = "Believe",
    srcs = glob(["src/**/*.java"]),
    data = [
        "//customFlowFiles:custom_flow_files",
        "//customSongs:custom_songs",
        "//data",
        "//levelFlowFiles:level_flow_files",
        "//res",
    ],
    main_class = "musicGame.Main",
    deps = [":libs"],
)

java_import(
    name = "libs",
    data = [":natives"],
    jars = glob(["lib/*.jar"]),
)

filegroup(
    name = "natives",
    srcs = glob(["lib/native/*"]),
)

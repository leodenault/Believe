load("//bazel/bzl:rules.bzl", "believe_binary", "textproto")

NATIVE_CONFIGS = {
    "linux_x86": {
        "openal_file_name": "libopenal.so",
        "native_targets": [
            "//third_party/lwjgl:linux_x86",
            "//third_party/openal:linux_x86",
            "//third_party/jinput:linux_x86",
        ],
    },
    "linux_x64": {
        "openal_file_name": "libopenal64.so",
        "native_targets": [
            "//third_party/lwjgl:linux_x64",
            "//third_party/openal:linux_x64",
            "//third_party/jinput:linux_x64",
        ],
    },
    "mac": {
        "openal_file_name": "openal.dylib",
        "native_targets": [
            "//third_party/lwjgl:mac",
            "//third_party/openal:mac",
            "//third_party/jinput:mac",
        ],
    },
    "windows_x86": {
        "openal_file_name": "OpenAL32.dll",
        "native_targets": [
            "//third_party/lwjgl:windows_x86",
            "//third_party/openal:windows_x86",
        ],
    },
    "windows_x64": {
        "openal_file_name": "OpenAL64.dll",
        "native_targets": [
            "//third_party/lwjgl:windows_x64",
            "//third_party/openal:windows_x64",
        ],
    },
}

package_group(
    name = "believe_src_pkgs",
    packages = ["//java/..."],
)

package_group(
    name = "believe_test_pkgs",
    packages = ["//javatests/..."],
)

package_group(
    name = "believe_all_pkgs",
    packages = ["//..."],
)

textproto(
    name = "game_options_textproto",
    srcs = ["game_options.textproto"],
    java_outer_class_name = "believe.app.proto.GameOptionsProto",
    proto_message = "GameOptions",
    deps = ["//java/believe/app/proto:game_options_java_proto"],
)

believe_binary(
    base_name = "Believe",
    data = [
        ":game_options_textproto",
        "//customFlowFiles:custom_flow_files",
        "//customSongs:custom_songs",
    ],
    main_class = "believe.app.game.Believe",
    native_configs = NATIVE_CONFIGS,
    resources = [
        "//data",
        "//levelFlowFiles:level_flow_files",
        "//res",
    ],
    runtime_deps = ["//java/believe/app/game"],
)

#believe_binary(
#    base_name = "Believe",
#    data = [
#        ":game_options_textproto",
#        "//customFlowFiles:custom_flow_files",
#        "//customSongs:custom_songs",
#    ],
#    main_class = "believe.app.game.BelieveV2",
#    native_configs = NATIVE_CONFIGS,
#    resources = [
#        "//data",
#        "//levelFlowFiles:level_flow_files",
#        "//res",
#    ],
#    runtime_deps = ["//java/believe/app/game:game_v2"],
#)

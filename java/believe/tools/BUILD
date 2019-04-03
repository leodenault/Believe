package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "proto_file_serializer",
    srcs = [
        "ProtoFileSerializer.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

java_binary(
    name = "ProtoFileSerializer",
    main_class = "believe.tools.ProtoFileSerializer",
    runtime_deps = [":proto_file_serializer"],
)

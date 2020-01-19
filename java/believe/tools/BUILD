load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "kt_jvm_library")

package(
    default_visibility = ["//:believe_all_pkgs"],
)

kt_jvm_library(
    name = "proto_file_serializer",
    srcs = [
        "ProtoFileSerializer.kt",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//java/believe/proto",
        "//java/believe/proto:dagger_component",
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

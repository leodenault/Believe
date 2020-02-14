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
        "//third_party/javax:annotations",
        "//third_party/proto",
        "//third_party/slick",
    ],
)

kt_jvm_library(
    name = "runner",
    srcs = ["ProtoFileSerializerRunner.kt"],
    deps = [":dagger_component"],
)

java_library(
    name = "dagger_module",
    srcs = ["ProtoFileSerializerDaggerModule.java"],
    deps = [
        "//java/believe/character/playable:dagger_module",
        "//java/believe/command:dagger_module",
        "//java/believe/dialogue:dagger_module",
        "//java/believe/proto:dagger_module",
        "//third_party/dagger",
    ],
)

java_library(
    name = "dagger_component",
    srcs = ["ProtoFileSerializerComponent.java"],
    deps = [
        ":dagger_module",
        ":proto_file_serializer",
        "//java/believe/character/playable:dagger_module",
        "//java/believe/command:dagger_module",
        "//java/believe/dialogue:dagger_module",
        "//java/believe/proto",
        "//java/believe/proto:dagger_module",
        "//third_party/dagger",
        "//third_party/proto",
    ],
)

java_binary(
    name = "ProtoFileSerializer",
    main_class = "believe.tools.ProtoFileSerializerRunner",
    runtime_deps = [":runner"],
)

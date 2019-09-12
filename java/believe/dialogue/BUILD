package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "dialogue",
    srcs = [
        "DialogueCommandCollisionHandler.java",
        "DialogueSupplier.java",
        "package-info.java",
    ],
    deps = [
        ":internal_qualifiers",
        "//java/believe/app/annotation",
        "//java/believe/character/playable",
        "//java/believe/datamodel",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/map/collidable/command",
        "//java/believe/map/tiled",
        "//java/believe/react:impl",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["DialogueDaggerModule.java"],
    deps = [
        ":dialogue",
        ":internal_qualifiers",
        "//java/believe/character/playable",
        "//java/believe/character/playable:dagger_module",
        "//java/believe/datamodel/protodata",
        "//java/believe/dialogue/proto:dialogue_java_proto",
        "//java/believe/map/collidable/command",
        "//java/believe/physics/collision",
        "//java/believe/react",
        "//java/believe/react:impl",
        "//third_party/dagger",
    ],
)

java_library(
    name = "internal_qualifiers",
    srcs = ["InternalQualifiers.java"],
    deps = [
        "//third_party/dagger",
        "//third_party/javax:annotations",
    ],
)

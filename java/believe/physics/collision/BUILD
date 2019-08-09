package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "collision",
    srcs = [
        "Collidable.java",
        "CollisionHandler.java",
        "CollisionHandlerExecutor.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//java/believe/character:faction",
        "//java/believe/geometry",
        "//java/believe/statemachine",
        "//java/believe/util",
        "//third_party/dagger",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "impl",
    srcs = ["CollisionHandlerExecutorImpl.java"],
    deps = [
        ":collision",
        "//third_party/dagger",
        "//third_party/javax:annotations",
    ],
)

java_library(
    name = "dagger_module",
    srcs = ["CollisionDaggerModule.java"],
    deps = [
        ":collision",
        ":impl",
        "//third_party/dagger",
    ],
)

package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "damage",
    srcs = [
        "DamageBox.java",
        "DamageBoxCollidable.java",
        "DamageBoxCollisionHandler.java",
        "package-info.java",
    ],
    deps = [
        "//java/believe/app/annotation",
        "//java/believe/character:faction",
        "//java/believe/geometry",
        "//java/believe/physics/collision",
        "//java/believe/physics/manager",
        "//java/believe/util",
        "//third_party/auto_factory",
        "//third_party/dagger",
        "//third_party/javax:annotations",
    ],
)

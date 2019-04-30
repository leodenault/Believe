package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "temporarystate",
    srcs = [
        "OverlayablePrecedingState.java",
        "PrecedingState.java",
        "TemporaryState.java",
    ],
    deps = [
        ":package-info",
        "//third_party/slick",
    ],
)

java_library(
    name = "impl",
    srcs = [
        "GameOverState.java",
        "GamePausedOverlay.java",
    ],
    deps = [
        ":package-info",
        ":temporarystate",
        "//java/believe/action",
        "//java/believe/gamestate",
        "//java/believe/graphicstransitions",
        "//java/believe/gui",
        "//third_party/javax:annotations",
        "//third_party/slick",
    ],
)

java_library(
    name = "package-info",
    srcs = ["package-info.java"],
    deps = [
        "//java/believe/app/annotation",
        "//third_party/javax:annotations",
    ],
)

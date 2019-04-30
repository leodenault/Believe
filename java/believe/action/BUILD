package(
    default_visibility = ["//:believe_all_pkgs"],
)

java_library(
    name = "action",
    srcs = [
        "ChangeStateAction.java",
        "ChangeToTemporaryStateAction.java",
        "ExitTemporaryStateAction.java",
        "ExternalLoadGameAction.java",
    ],
    deps = [
        "//java/believe/gamestate",
        "//java/believe/gamestate/temporarystate",
        "//java/believe/graphicstransitions",
        "//java/believe/levelFlow/parsing/exceptions",
        "//third_party/slick",
    ],
)

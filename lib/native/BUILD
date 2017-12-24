load("//bzl:enums.bzl", "ARCHITECTURE", "OS")
load("//bzl:util.bzl", "platform_name")

package(default_visibility = ["//:__pkg__"])

filegroup(
    name = platform_name(
        architecture = ARCHITECTURE.X86,
        os = OS.WINDOWS,
        prefix = "native",
    ),
    srcs = [
        "OpenAL32.dll",
        "lwjgl.dll",
    ],
)

filegroup(
    name = platform_name(
        architecture = ARCHITECTURE.X64,
        os = OS.WINDOWS,
        prefix = "native",
    ),
    srcs = [
        "OpenAL64.dll",
        "lwjgl64.dll",
    ],
)

filegroup(
    name = platform_name(
        architecture = ARCHITECTURE.X86,
        os = OS.LINUX,
        prefix = "native",
    ),
    srcs = [
        "libjinput-linux.so",
        "liblwjgl.so",
        "libopenal.so",
    ],
)

filegroup(
    name = platform_name(
        architecture = ARCHITECTURE.X64,
        os = OS.LINUX,
        prefix = "native",
    ),
    srcs = [
        "libjinput-linux64.so",
        "liblwjgl64.so",
        "libopenal64.so",
    ],
)

filegroup(
    name = platform_name(
        os = OS.MAC,
        prefix = "native",
    ),
    srcs = [
        "libjinput-osx.dylib",
        "liblwjgl.dylib",
        "openal.dylib",
    ],
)

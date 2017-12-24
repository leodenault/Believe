load(":enums.bzl", "ARCHITECTURE", "OS")
load(":util.bzl", "platform_name")

# Private constants
_MAX_CLASSPATH_LINE_LENGTH = 70

def _normalize_classpath(classpath):
  '''
  Takes a classpath string and chops it up into lines of length _MAX_CLASSPATH_LINE_LENGTH.
  '''
  classpath_length = len(classpath)
  if classpath_length <= _MAX_CLASSPATH_LINE_LENGTH:
    return classpath

  lines = [classpath[0:_MAX_CLASSPATH_LINE_LENGTH]]
  for i in range(_MAX_CLASSPATH_LINE_LENGTH, classpath_length, _MAX_CLASSPATH_LINE_LENGTH - 1):
    lines.append(classpath[i:i + _MAX_CLASSPATH_LINE_LENGTH - 1])

  return "\n ".join(lines)

def _believe_binary_impl(ctx):
  dep = ctx.attr.dep
  out_dir = ctx.outputs.out_dir
  jar_name = ctx.attr.jar_name if ctx.attr.jar_name else ctx.attr.name + "_app.jar"
  jar_path = out_dir.path + "/" + jar_name
  build_output = out_dir.path + ".output"
  _manifest_temp = ctx.outputs._manifest_temp
  manifest_out= build_output + "/META-INF/MANIFEST.MF"

  # First fetch the data files and filter out the JAR file.
  data = dep.data_runfiles.files.to_list()
  jar = [file for file in data
            if file.basename == "lib" + dep.label.name + ".jar"]
  if len(jar) == 0:
    fail(msg = "Expected a single jar, but found none.", attr="dep")
  elif len(jar) > 1:
    fail(msg = "Expected a single jar, but found multiple.", attr="dep")
  jar = jar[0]
  data.remove(jar)

  # Now filter out the openal natives. They need to be placed right next to the JAR due to a bug
  # which causes them not to be found otherwise.
  openal = [file for file in data if ("openal" in file.basename or "OpenAL" in file.basename)]
  for file in openal:
    data.remove(file)

  # Set up the manifest file contents.
  manifest_main_class = "Main-Class: " + ctx.attr.main_class
  manifest_classpath = "Class-Path: " + " ".join([file.short_path for file in data])
  manifest = manifest_main_class + "\n" + _normalize_classpath(manifest_classpath) + "\n"

  ctx.file_action(
      output = _manifest_temp,
      content = manifest,
      executable = False)

  # Get the jar created from the java_lib rule. We want to unzip it and replace its manifest with
  # ours. Then we want to re-JAR it.
  cmd = "mkdir " + build_output + "\n"
  cmd += "unzip -q " + jar.path + " -d " + build_output + "\n"
  cmd += "cp -f " + _manifest_temp.path + " " + manifest_out + "\n"
  cmd += "mkdir " + out_dir.path + "\n"
  cmd += "jar -cfM " + jar_path + " -C " + build_output + "/ ." + "\n"

  # Add all of the data files to the output directory.
  cmd += "\n".join(
      ["mkdir -p " + out_dir.path + "/" + file.dirname + "\n" +
       "cp " + file.short_path + " " + out_dir.path + "/" + file.short_path
       for file in data]) + "\n"
  cmd += "\n".join(
      ["cp " + file.short_path + " " + out_dir.path + "/" + file.basename for file in openal])

  ctx.actions.run_shell(
    inputs = [jar, _manifest_temp] + data + openal,
    outputs = [out_dir],
    command=cmd,
    use_default_shell_env=True)

  # This is needed so that the rule can be run with 'bazel run'.
  ctx.actions.write(
      output = ctx.outputs.executable,
      content = "cd " + out_dir.basename + ";java -jar " + jar_name,
      is_executable = True,
  )

  return [DefaultInfo(
      runfiles=ctx.runfiles(files=[out_dir]),
      files=depset([out_dir])
      )]

def _zip_impl(ctx):
  zip_file = ctx.outputs.zip_file
  files_to_zip = [file for dep in ctx.attr.deps for file in dep.files]
  file_string = ""
  single_dir = False
  if len(files_to_zip) == 1 and files_to_zip[0].extension == "":
    # If there's only one file and it's a directory, then only include its contents.
    file_string += "*"
    single_dir = True
  else:
    # Include all of the listed files in all dependencies.
    file_string += " ".join([file.path for file in files_to_zip])

  cmd = ""
  if single_dir:
    cmd += "current_dir=$(pwd)\n"
    cmd += "cd " + files_to_zip[0].path + "\n"
  cmd += "zip -qr " + ("$current_dir/" if single_dir else "") + zip_file.path + " " + file_string

  ctx.actions.run_shell(
      inputs = files_to_zip,
      outputs = [zip_file],
      command = cmd,
      use_default_shell_env = True,
  )

def _pkg_all_impl(ctx):
  out_dir = ctx.outputs.out_dir
  files = [file for dep in ctx.attr.deps for file in dep.files]
  cmd = "mkdir " + out_dir.path + "\n"
  cmd += "\n".join(["cp " + file.path + " " + out_dir.path for file in files])
  ctx.actions.run_shell(
      inputs = files,
      outputs = [out_dir],
      command = cmd,
      use_default_shell_env = True,
  )

believe_binary = rule(
    _believe_binary_impl,
    attrs = {
        "dep": attr.label(allow_files=False, mandatory=True),
        "main_class": attr.string(),
        "jar_name": attr.string(mandatory=False),
    },
    outputs = {
        "out_dir": "%{name}_bin",
        "_manifest_temp": "MANIFEST_%{name}.MF",
    },
    executable = True,
)

pkg_zip = rule(
    _zip_impl,
    attrs = {
        "deps": attr.label_list(allow_files=True),
    },
    outputs = {
        "zip_file": "%{name}.zip",
    },
)

pkg_all = rule(
    _pkg_all_impl,
    attrs = {
        "deps" : attr.label_list(allow_files=False)
    },
    outputs = {
        "out_dir": "%{name}",
    },
)

def pkg_for_platform(name, os, architecture=None):
  rule_name = platform_name(prefix = name, os = os, architecture = architecture)
  lib_name = platform_name(prefix = name, os = os, architecture = architecture, suffix = "lib")
  native_dep = platform_name(prefix = "native", os = os, architecture = architecture)
  pkg_name = platform_name(prefix = name, os = os, architecture = architecture, suffix = "pkg")

  native.java_library(
      name = lib_name,
      srcs = native.glob(["src/**/*.java"]),
      data = [
          "//customFlowFiles:custom_flow_files",
          "//customSongs:custom_songs",
          "//lib/native:" + native_dep
      ],
      resources = [
          "//data",
          "//levelFlowFiles:level_flow_files",
          "//res",
      ],
      deps = ["//lib"],
  )

  believe_binary(
    name = rule_name,
    dep = ":" + lib_name,
    main_class = "musicGame.Main",
    jar_name= name + ".jar",
  )

  pkg_zip(
      name = rule_name + "_pkg",
      deps = [":" + rule_name]
  )

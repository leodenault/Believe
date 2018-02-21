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

def _make_dir_and_copy_file(file, dest_dir, dest_file_name=None):
  '''
  Generates commands for making a directory for a file and copying the file into the directory.
  '''
  dest_name = dest_file_name if dest_file_name else file.basename
  cmd = "mkdir -p " + dest_dir + "\n"
  cmd += "cp " + file.path + " " + dest_dir + "/" + dest_name
  return cmd

def _make_dirs_and_copy_files(files, dest_dir, file_dir_override=None):
  '''
  Generates commands for making directories for a set of files and copying the files into the
  directories.
  '''
  return "\n".join([_make_dir_and_copy_file(file, dest_dir + "/" + file.dirname) for file in files])

def _believe_binary_impl(ctx):
  out_dir = ctx.outputs.out_dir
  build_dir = out_dir.path + ".build"
  jar_name = ctx.attr.jar_name if ctx.attr.jar_name else ctx.attr.name + "_app.jar"
  jar_path = out_dir.path + "/" + jar_name
  manifest_temp = ctx.outputs._manifest_temp

  generated_libs = []
  third_party_libs = []

  # Split up the jar files into third party and generated libraries.
  for dep in ctx.attr.runtime_deps:
    for file in dep.data_runfiles.files:
      if file.is_source:
        third_party_libs.append(file)
      else:
        generated_libs.append(file)

  # Gather the data files which will reside outside of the final jar file.
  data_files = []
  openal_files = []
  for dep in ctx.attr.data:
    for file in dep.files:
      if "openal" in file.basename.lower():
        openal_files.append(file)
      else:
        data_files.append(file)

  # Gather the resource files which will be put into the final jar file.
  resource_files = [file for dep in ctx.attr.resources for file in dep.files]

  # Set up the manifest file contents.
  manifest_main_class = "Main-Class: " + ctx.attr.main_class
  manifest_classpath = "Class-Path: " + " ".join([file.short_path for file in data_files + third_party_libs])
  manifest_content = manifest_main_class + "\n" + _normalize_classpath(manifest_classpath) + "\n"

  ctx.file_action(
      output = manifest_temp,
      content = manifest_content,
      executable = False)

  cmd = "mkdir " + build_dir + "\n"

  # Unzip the jar contents of the generated libraries and zip them up in the final jar file.
  cmd += "".join(["unzip -q " + jar.path + " -d " + build_dir + " -x META-INF/*\n"
                    for jar in generated_libs])

  # Then copy the resources and the manifest in the build directory to include them in the jar, too.
  cmd += _make_dirs_and_copy_files(resource_files, build_dir) + "\n"
  cmd += _make_dir_and_copy_file(manifest_temp, build_dir + "/META-INF", "MANIFEST.MF") + "\n"
  cmd += "mkdir " + out_dir.path + "\n"
  cmd += "jar -cfM " + jar_path  + " -C " + build_dir + "/ ." + "\n"

  # Copy the data to the output directory.
  cmd += _make_dirs_and_copy_files(data_files, out_dir.path) + "\n"
  cmd += _make_dirs_and_copy_files(third_party_libs, out_dir.path) + "\n"
  cmd += "\n".join([_make_dir_and_copy_file(file, out_dir.path) for file in openal_files])

  ctx.actions.run_shell(
    inputs = (
        generated_libs +
        third_party_libs +
        resource_files +
        data_files +
        openal_files +
        [manifest_temp]),
    outputs = [out_dir],
    command=cmd,
    use_default_shell_env=True)

  # This is needed so that the rule can be launch with 'bazel run'.
  ctx.actions.write(
      output = ctx.outputs.executable,
      content = "cd " + out_dir.basename + ";java -jar " + jar_name + " \"$@\"",
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
        "data": attr.label_list(allow_files=True),
        "main_class": attr.string(mandatory=True),
        "resources": attr.label_list(allow_files=False),
        "runtime_deps": attr.label_list(allow_files=False),
        "jar_name": attr.string(mandatory=False)
    },
    outputs = {
        "out_dir": "%{name}_bin",
        "_manifest_temp": "MANIFEST_%{name}.MF"
    },
    executable = True
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
        "out_dir": "%{name}_pkgs",
    },
)

def pkg_for_platform(base_name, os, architecture=None):
  rule_name = platform_name(prefix = base_name, os = os, architecture = architecture)
  native_dep = platform_name(prefix = "native", os = os, architecture = architecture)
  pkg_name = platform_name(prefix = base_name, os = os, architecture = architecture, suffix = "pkg")

  believe_binary(
    name = rule_name,
    main_class = "believe.app.game.Believe",
    jar_name = base_name + ".jar",
    data = [
        "//customFlowFiles:custom_flow_files",
        "//customSongs:custom_songs",
        "//lib/native:" + native_dep,
    ],
    resources = [
        "//data",
        "//levelFlowFiles:level_flow_files",
        "//res",
    ],
    runtime_deps = ["//src/believe/app/game"],
  )

  pkg_zip(
      name = pkg_name,
      deps = [":" + rule_name]
  )

MAX_CLASSPATH_LINE_LENGTH = 70

def normalize_classpath(classpath):
  classpath_length = len(classpath)
  if classpath_length <= MAX_CLASSPATH_LINE_LENGTH:
    return classpath

  lines = [classpath[0:MAX_CLASSPATH_LINE_LENGTH]]
  for i in range(MAX_CLASSPATH_LINE_LENGTH, classpath_length, MAX_CLASSPATH_LINE_LENGTH - 1):
    lines.append(classpath[i:i + MAX_CLASSPATH_LINE_LENGTH - 1])

  return "\n ".join(lines)

def _impl(ctx):
  dep = ctx.attr.dep
  out_dir = ctx.outputs.out_dir
  jar_name = ctx.attr.name + "_app.jar"
  jar_path = out_dir.path + "/" + jar_name
  build_output = out_dir.path + ".output"
  _manifest_temp = ctx.outputs._manifest_temp
  manifest_out= build_output + "/META-INF/MANIFEST.MF"

  data = dep.data_runfiles.files.to_list()
  jar = [file for file in data
            if file.basename == "lib" + dep.label.name + ".jar"]
  if len(jar) == 0:
    fail(msg = "Expected a single jar, but found none.", attr="dep")
  elif len(jar) > 1:
    fail(msg = "Expected a single jar, but found multiple.", attr="dep")
  jar = jar[0]
  data.remove(jar)
  openal = [file for file in data if ("openal" in file.basename or "OpenAL" in file.basename)]
  for file in openal:
    data.remove(file)

  manifest_main_class = "Main-Class: " + ctx.attr.main_class
  manifest_classpath = "Class-Path: " + " ".join([file.short_path for file in data])
  manifest = manifest_main_class + "\n" + normalize_classpath(manifest_classpath) + "\n"

  ctx.file_action(
      output = _manifest_temp,
      content = manifest,
      executable = False)

  cmd = "mkdir " + build_output + "\n"
  cmd += "unzip -q " + jar.path + " -d " + build_output + "\n"
  cmd += "cp -f " + _manifest_temp.path + " " + manifest_out + "\n"
  cmd += "mkdir " + out_dir.path + "\n"
  cmd += "jar -cfM " + jar_path + " -C " + build_output + "/ ." + "\n"
  cmd += "pwd\n"
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

  ctx.actions.write(
      output = ctx.outputs.executable,
      content = "cd " + out_dir.basename + ";java -jar " + jar_name,
      is_executable = True,
  )

  return [DefaultInfo(runfiles=ctx.runfiles(files=[out_dir]))]

believe_binary = rule(
    _impl,
    attrs = {
        "dep": attr.label(allow_files=False, mandatory=True),
        "main_class": attr.string(),
    },
    outputs = {
        "out_dir": "%{name}_bin",
        "_manifest_temp": "MANIFEST.MF",
    },
    executable = True,
)


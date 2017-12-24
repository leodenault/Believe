def _enum(values):
  '''
  Creates an enum implemented through a struct. Enum values are the values passed in as parameters. Enum names are the
  uppercase versions of the values. For example:
    MY_ENUM = _enum(["value_1", "value_2"])
  will result in the following accessible enum values:
    MY_ENUM.VALUE_1 // == "value_1"
    MY_ENUM.VALUE_2 // == "value_2"
  '''
  keyword_args = {value.upper(): value for value in values}
  return struct(**keyword_args)

# Public enums
OS = _enum(["windows", "linux", "mac"])
ARCHITECTURE = _enum(["x86", "x64"])
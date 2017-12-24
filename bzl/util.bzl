def platform_name(os, architecture=None, prefix=None, suffix=None):
  '''
  Generates a package name based on the given parameters.
  '''
  terms = []
  if prefix:
    terms.append(prefix)
  terms.append(os)
  if architecture:
    terms.append(architecture)
  if suffix:
    terms.append(suffix)
  return "_".join(terms)
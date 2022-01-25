def filter(data):
    data = unicodedata.normalize('NFKD',data)
    if len(data) > 1024:
        return False
    if re.search(r'__|\(|\)|datetime|sys|import',data):
        return False
    for k in builtins.__dict__.keys():
        if k in data:
            return False
    return True
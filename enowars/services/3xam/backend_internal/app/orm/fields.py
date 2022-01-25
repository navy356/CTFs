class Field:
    def __init__(self, colname=None):
        self.__value = None
        self.__colname = colname

    @property
    def colname(self):
        return self.__colname

    @colname.setter
    def colname(self, value):
        self.__colname = value

    @property
    def value(self):
        return self.__value

    @value.setter
    def value(self, value):
        self.__value = value

    def __str__(self):
        return 'None' if self.value is None else self.value

class IntegerField(Field):
    pass

class TextField(Field):
    pass

class ForeignKeyField(Field):
    def __init__(self, foreign_model):
        super(ForeignKeyField, self).__init__()
        self.foreign_model = foreign_model
        self.__value = None

    @property
    def value(self):
        return self.__value

    @value.setter
    def value(self, value):
        if type(value) is int:
            self.__value = self.foreign_model(id=value)
        else:
            self.__value = value


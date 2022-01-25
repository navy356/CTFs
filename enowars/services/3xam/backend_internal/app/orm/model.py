import copy
import types
from mysql.connector import errors
import re
from .fields import *
import logging


logger = logging.getLogger(__name__)

def to_snake(name):
    return re.sub(r'(?<!^)(?=[A-Z])', '_', name).lower()

class ModelType(type):
    def __new__(mcls, name, bases, attrs):
        result = {'fields': {}}
        for b in bases:
            result['fields'].update(vars(b)['fields'])
        for attr, val in attrs.items():
            if issubclass(type(val), Field):
                if val.colname is None:
                    if isinstance(val, ForeignKeyField):
                        val.colname = '%s_id' % attr 
                    else:
                        val.colname = attr

                fget = lambda self, k=attr: getattr(self, "__%s" % k).value
                fset = lambda self, value, k=attr: setattr(getattr(self, "__%s" % k), 'value', value)
                result[attr] = property(fget, fset)
                # result['fields'].append((attr, val))
                result['fields'][attr] = val
            else:
                result[attr] = val
        return super(ModelType, mcls).__new__(mcls, name, bases, result)

    def __init__(cls, name, bases, attrs):
        for attr, val in attrs.items():
            if issubclass(type(val), ForeignKeyField):
                fget = lambda self, M=cls, a=attr: M.find(**{a: self.id, 'limit': 200, 'order': 1})
                setattr(val.foreign_model, to_snake(name) + 's',
                        property(fget, None))

class Model(metaclass=ModelType):
    ORDER_ASC = 0
    ORDER_DESC = 1
    SEED = []
    connection = None
    id = IntegerField()

    def __init__(self, *args, **kwargs):
        for k, v in self.fields.items():
            val = copy.deepcopy(v)
            setattr(self, '__' + k, val)

        field_names = self.fields.keys()
        model_data = dict(zip(field_names, args))
        model_data.update(kwargs)
        for k,v in model_data.items():
            getattr(self, '__' + k).value = v

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        out = self.__class__.__name__ + '(%s)'
        return out % ', '.join(
                map(lambda f: '%s=%s' % (f,
                    getattr(self, f)), self.fields.keys()))

    def __eq__(self, other):
        if type(other) is not self.__class__: return False
        for f,v in self.fields.items():
            if getattr(self, f) != getattr(other, f):
                print('%s NO MATCH' % f)
                print(self)
                print(other)
                return False
        return True

    def __getstate__(self):
        return self.to_dict()

    def to_dict(self):
        out = map(lambda f: (f, getattr(self, f)) , self.fields.keys())
        out = filter(lambda f: f[1] is not None, out)
        out = map(lambda f:
                (f[0] + '_id', f[1].id) if issubclass(type(f[1]), Model) else
                f, out)
        return dict(out)

    @classmethod
    def tablename(cls):
        return to_snake(cls.__name__) + 's'

    @classmethod
    def conds_to_sql(cls, **kwargs):
        out_items = [] # to be anded
        out_values = []
        for k,v in kwargs.items():
            if type(v) in (list, tuple):
                if not len(v):
                    out_items.append('1 = 0')
                else:
                    out_values.extend(v)
                    placeholders = ['%s'] * len(v)
                    out_items.append('%s IN (%s)' % (k, ', '.join(placeholders)))
            else:
                out_values.append(v)
                out_items.append('%s = %%s' % k)
        return ' AND '.join(out_items), tuple(out_values)

    @classmethod
    def get(cls, fields=None, **kwargs):
        models = cls.find(fields, **kwargs)
        if len(models):
            return models[0]

    @classmethod
    def find(cls, fields=None, offset=0, limit=None, order=0, **kwargs):
        q_chunks = ('SELECT * FROM (',)
        q_chunks += ('SELECT {fields}',)
        q_chunks += ('FROM {tablename}',)
        q_chunks += ('WHERE id > 0', )
        q_chunks += ('AND ({conds})',)

        if order == cls.ORDER_DESC:
            q_chunks += ('ORDER BY id DESC',)

        if not limit:
            limit = 200
        elif limit == -1:
            limit = None

        if limit:
            q_chunks += ('LIMIT {offset},{limit}', )
        q_chunks += (')x',)

        q = ' '.join(q_chunks)

        if fields:
            assert type(fields) is list
        else:
            fields = cls.fields.keys()

        # get real col names for fields
        colnames = tuple(map(lambda f: '`%s`' % cls.fields[f].colname, fields))

        # update conditions kwargs with real field names:
        conds = dict(tuple(map(lambda item: ('`%s`' % cls.fields[item[0]].colname,
            item[1]),
            kwargs.items())))

        conds_str, values = cls.conds_to_sql(**conds)
        if not len(conds):
            conds_str = '1 = 1'

        q = q.format(fields=', '.join(colnames), tablename=cls.tablename(),
                conds=conds_str, limit=limit, offset=offset)
        val = tuple(
                map(lambda value: value.id
                        if issubclass(type(value), Model) else value, values))

        logger.debug(q % val)

        if cls.connection:
            cursor = cls.connection.cursor()
            cursor.execute(q, val)
            results = cursor.fetchall()
            cursor.close()

            return list(map(lambda result: 
                    cls(**dict(zip(fields, result))) ,results))
        else:
            return q, val

    @classmethod
    def count(cls, **kwargs):
        q_chunks = ('(',)
        q_chunks += ('SELECT COUNT(*)',)
        q_chunks += ('FROM {tablename}',)
        q_chunks += ('WHERE ({conds})',)
        q_chunks += (')',)
        q = ' '.join(q_chunks)

        # update conditions kwargs with real field names:
        conds = dict(tuple(map(lambda item: ('`%s`' % cls.fields[item[0]].colname,
            item[1]),
            kwargs.items())))

        conds_str, values = cls.conds_to_sql(**conds)
        if not len(conds):
            conds_str = '1 = 1'

        q = q.format(tablename=cls.tablename(), conds=conds_str)
        val = tuple(map(lambda value: value.id
                        if issubclass(type(value), Model) else value, values))

        logger.debug(q % val)

        if cls.connection:
            cursor = cls.connection.cursor()
            cursor.execute(q, val)
            result = cursor.fetchone()
            cursor.close()

            return result[0]

        else:
            return q, val

    def save(self):
        fields_dict = self.__class__.fields
        fields = fields_dict.keys()

        # collect all non None fields
        fields = tuple(filter(lambda f: getattr(self, f) is not None, fields))

        # collect values of those fields we collected
        values = tuple(map(lambda f: getattr(self, f) , fields))

        # if a value is a Model, then we replace with the model id
        values = tuple(map(lambda v: v.id if issubclass(type(v), Model) else v,
                values))

        # similarily, if a field is a forignkey, suffix with `_id` 
        fields = tuple(map(lambda f: f + '_id' if isinstance(fields_dict[f],
            ForeignKeyField) else f, fields))

        # wrap all fields names
        fields = tuple(map(lambda f: '`%s`' % f,  fields))

        if self.id and self.__class__.get(id=self.id):
            fields = list(fields)
            values = list(values)
            q = 'UPDATE {tablename} SET {update} WHERE `id` = %s'

            if 'id' in fields:
                values.pop(fields.index('id'))
                fields.remove('id')

            update = ['%s'] * len(fields)
            update = tuple(zip(fields, update))
            update = tuple(map(lambda x: ' = '.join(x), update))
            update = ', '.join(update)
            values = tuple(values) + (self.id,)
            q = q.format(
                    tablename=self.__class__.tablename(),
                    update=update
                    )
        else:
            q = 'INSERT INTO {tablename} ({fields}) VALUES ({values})'
            q = q.format(
                    tablename=self.__class__.tablename(),
                    fields=', '.join(fields),
                    values=', '.join(['%s'] * len(fields))
                    )

        logger.debug(q % values)

        if self.connection:
            cursor = self.connection.cursor()
            try:
                cursor.execute(q, values)
                self.connection.commit()
                if not self.id:
                    self.id = cursor.lastrowid
                cursor.close()
                return True
            except errors.IntegrityError as ex:
                #TODO log
                logger.error(ex)
                return False
        else:
            return q, values

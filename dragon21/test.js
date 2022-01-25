const { Pool } = require('pg');

function sqlEscape(value) {
    return "'" + String(value).replace(/[^\x20-\x7e]|[']/g, '') + "'";
}


function connect() {
    return new Pool({
        connectionString: 'postgresql://%2frun%2fpostgresql/webpwn'
    });
}


function prepare(query, params) {
    for (const key in params) {
        query = query.replaceAll(':' + key, sqlEscape(params[key]));
    }

    return query;
}

module.exports = {
    connect,
    prepare
};

connect();
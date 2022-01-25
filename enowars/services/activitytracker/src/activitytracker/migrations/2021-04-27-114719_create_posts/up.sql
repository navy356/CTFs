CREATE TABLE posts (
    id SERIAL PRIMARY KEY,
    body TEXT NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT 'f',
    visibility TEXT NOT NULL,
    image TEXT
)
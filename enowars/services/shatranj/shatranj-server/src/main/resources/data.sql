CREATE DATABASE IF NOT EXISTS db_example;
-- Creates the new database
CREATE USER IF NOT EXISTS 'springuser' @'%' identified by 'ThePassword';
-- Creates the user
GRANT ALL on db_example.* to 'springuser' @'%';
-- Gives all privileges to the new user on the newly created database
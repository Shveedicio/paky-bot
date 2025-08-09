\c

create user "paky-bot" with password 'paky-bot';

create database "paky-bot" with owner = postgres;

grant all privileges on database "paky-bot" to "paky-bot";

\c "paky-bot"

alter role "paky-bot" set search_path = paky_bot, public;

create schema paky_bot authorization "paky-bot";

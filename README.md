# guestbook-web

Application to create and manage guest entries.

## MySQL Database Setup

1. Run below command to create database ***'CREATE DATABASE IF NOT EXISTS guestbook;'***
2. Update MySQL password in ***src/main/resources/application.properties***
3. Creating and Inserting the data
   - Run the scripts available in ***src/main/resources/guestbook.sql***

## How to run steps:

1. ***mvn spring-boot:run***
2. http://localhost:8080/guestbook OR http://localhost:8080/guestbook/welcome

## Features

1. User Registration and Login
2. User 
   - Create an entry
4. Admin
   - View entry/entries
   - Edit entry/entries
   - Approve entry/entries
   - Delete entry/entried (This API marks the entry as deleted, does not delete the actual entry)

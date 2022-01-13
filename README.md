# guestbook-web

Application to create and manage guest entries.

## MySQL Database Setup

1. Run below command to create database ***'CREATE DATABASE IF NOT EXISTS guestbook;'***
2. If using Spring feature for DDL and DML
   - run this ***mvn spring-boot:run*** command and spring will create and insert data into table
3. If creating manually
   - Comment below two lines in ***src/main/resources/application.properties***
     - spring.jpa.hibernate.ddl-auto=none
     - spring.sql.init.mode=always
   - Run the scripts available in ***src/main/resources/guestbook.sql***

## How to run steps:

1. ***mvn spring-boot:run***
2. http://localhost:8080/ OR http://localhost:8080/welcome

## Features

1. User Registration and Login
2. User 
   - Create an entry
4. Admin
   - View entry/entries
   - Edit entry/entries
   - Approve entry/entries
   - Delete entry/entried (This API marks the entry as deleted, does not delete the actual entry)

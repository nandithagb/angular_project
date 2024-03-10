# 🍵 Lofi Cafe Management System

Lofi Cafe Management System is a retail management system (RMS) that handles basic cafe operations efficiently. Extends a project I build last year called Lofi-Cafe.

## Features/Functions
- Admin/user sign in system using JWT authentication
- Forgot password and user verification mailing system using Spring Mailer
- Dashboard: displays summary of the categories, products, and bills
- CRUD operations to manage categories, products, bills, and staff
- Create and edit categories and products
- Create customer orders and download them as a PDF bill
- Create, download, and delete bills

All data is submitted in the front-end via forms, sent to the backend through POST/GET requests, and saved to corresponding tables in the SQL database. Error handling is implemented in the backend and all unauthorized access will be redirected to the home page through an Angular routing guard and interceptor.

## Tech Stack
Front-end: Angular/Typescript with Material UI                                                                                                             
Backend: Spring Boot/Java                                                                                                                                   
Database: MySQL

https://user-images.githubusercontent.com/63011927/221094357-8145ea54-862a-4ce8-8bea-2bad14aa6a3d.mov



https://user-images.githubusercontent.com/63011927/221094360-8207ce87-d89e-430e-9648-b343aa2e7389.mov



https://user-images.githubusercontent.com/63011927/221094362-4c8d3ee9-a2ed-44ce-bf66-5d62cdccec50.mov



https://user-images.githubusercontent.com/63011927/221094363-e5fe264e-8ca3-40de-82f4-9292c7f7109f.mov



https://user-images.githubusercontent.com/63011927/221094364-1e718a06-5239-4a8e-83d1-db1cb36b36ec.mov

## Docker
There are 2 Dockerfiles to build the images for the frontend and backend:
- ```/frontend (angular)/Dockerfile```
- ```/backend (spring)/Dockerfile```

To build the Dockerfiles, navigate into the desired folder and run the corresponding command:
- ```docker build -t cafe-management/frontend:[version] .```
- ```docker build -t cafe-management/backend:[version] .```

To run the application, run ```docker-compose up``` in the root directory.

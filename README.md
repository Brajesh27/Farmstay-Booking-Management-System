# FarmStay Booking System

This is a Software Engineering project. It is a website where people can book farmstays. A farmstay here is booked as a whole villa, there are no separate rooms.

There are two types of users, guest and admin. Guests can search and book farmstays. The admin (owner) adds the farmstays and looks after all the bookings.

## What it can do

Guest:
- register and login (password needs 8 characters with a letter, number and special character, phone needs 10 digits)
- see all farmstays with photos and price on the home page
- search by location, price and number of guests
- open a farmstay to see photos, description, location and reviews
- book with check-in and check-out dates. Dates that are already booked are greyed out in the calendar
- see their bookings in My bookings and cancel before the check-in date
- give a rating (1 to 5 stars) and comment after the stay is over
- change name, phone and password in profile

Admin:
- add a farmstay with photos
- edit or delete a farmstay (deleting also removes its bookings)
- see all bookings with totals and filter them by farmstay, status or upcoming
- cancel any booking
- admin can't book a farmstay

## Technologies

Java 17, Spring Boot 3, Thymeleaf, MongoDB, Maven, HTML and CSS. The calendar uses flatpickr.

## OOP concepts

OOP concepts used in the project:

- Classes and objects: Farmstay, User, Booking and Review are the model classes.
- Encapsulation: fields are private and are used through getters and setters. A booking is cancelled only through the cancel() method.
- Abstraction: controllers just call service methods and don't know how the data is saved in MongoDB.
- Inheritance: the repositories extend MongoRepository so save, findAll, findById and delete come for free.
- Polymorphism: WebConfig overrides addResourceHandlers() from WebMvcConfigurer to show uploaded photos.
- Association: a Booking has a User and a Farmstay.

There is only one User class with an admin true/false field instead of separate Guest and Admin classes because both had the same fields.

## Folder structure

```
com.example.farmstay
  Model       - Farmstay, User, Booking, Review
  Repository  - one repository for each model
  Service     - FarmstayService, UserService, BookingService, ReviewService
  Controller  - Home, Auth, Farmstay, Booking, Profile and Review controllers
resources
  templates   - all the html pages
  static/css  - style.css
uploads       - photos uploaded by admin
```

Flow of a request: Browser -> Controller -> Service -> Repository -> MongoDB

## How to run

Tested on Mac.

1. Install Java 17, Maven and MongoDB

```
brew install maven
brew tap mongodb/brew
brew install mongodb-community
```

2. Start MongoDB

```
brew services start mongodb-community
```

3. In application.properties

```
spring.data.mongodb.uri=mongodb://localhost:27017/farmstay
```

4. Go to the project folder in terminal and run

```
mvn spring-boot:run
```

5. Open http://localhost:8080

Admin login: username a, password 123

New accounts from the register page are normal guests.

## Future improvements

- online payment
- email after booking
- password hashing
- map for farmstay location

Booking Trails
----
##### About
- Booking Trail is the application for managing bookings of trails.
- There are already some fixed trails with their scheules, restrictions and charges.
- Hikers can create/update/cancel booking for a particular trail.
- We can view all the trails for a particular date.

#### Assumptions
- There is no restrictions on the number of bookings that can be made daily
- The dates and times of hike bookings can overlap each other as many times as possible
- Hiker who made the booking will be the part of hike.
- To uniquely identify a booking we are using below mentioned combination of keys `user_id`,`booking_date`,`start_time`,`end_time`,`trail_id` .

#### Technology Used
- Java8
- SpringBoot 
- Gradle
- MySQL
- Docker 

#### Steps to Run
- Build Steps
   - `./gradlew clean build`
- MySQL should be up and running
   - `docker-compose-up -d` can be used if docker available, otherwise use MySQL application for database
- Entry Point
  - `com.element.insurance.bookings.ApplicationLauncher` is the entry point of the application
  - Swagger Endpoint `http://localhost:8080/swagger-ui/index.html`
  - REST enpoints
   - POST /v1/bookings for creating the bookings
   - GET /v1/bookings/{bookingDate} for fetching bookings for a particular date
   - PUT /v1/bookings for updating the bookings
   - PUT /v1/bookings/{bookingStatus} for updating the status of booking
   - GET /v1/bookings for fetching bookings as per bookedBy and bookingDate
    - Sample Request
        - ```  
              {
                "bookedBy": {
                  "dob": "1992-06-26",
                  "email": "ajay@gmail.com",
                  "name": "aj"
                },
                "bookingDate": "1992-06-26",
                "bookingStatus": "Booked",
                "endTime": "09:00:00",
                "hikers": [
                  {
                    "dob": "1992-06-26",
                    "email": "ajay@gmail.com",
                    "name": "aj"
                  }
                ],
                "startTime": "07:00:00",
                "trail": "Shire"
              }
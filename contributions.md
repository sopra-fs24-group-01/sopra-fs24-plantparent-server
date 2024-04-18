# Weekly contributions
Before every TA meeting add the resolved development tasks to this document.

During the meeting every member gives a 3 minute update and answers the following:
* What did I do last week?
* What will I do this week?
* What are the obstacles to progress?


<br/><br/>

# Sprint 1, M3
## Week 1, 23.03.2024 - 28.03.2024
### Focused User Stories
This week we focused on setting up everything and implementing the tasks for [U1: As a user, I want to be able to register an account, so that resources that I own or I can manage will be visible to me.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/1) 

### Lazaro
Last week, I focused on tasks corresponding to user story U1 [#1](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/1):
* I created the register GUI [#28](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/28).
* Furthermore, I created validation for the input of registration information (Mail-Address, Confirm Password) [#29](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/29)

Next week, I will be working on tasks corresponding to U2 [#2](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/2) and P1 [#4](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/4):
* Create login guard [#31](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/31)
* Create "create plant" GUI [#26](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/26)

### Daniel
This week I adapted the client repository from the individual assignment to our needs. I removed unnecessary files, adapted the configuration files, and added styled-components and redux [#27](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/27) to the project. 

For user story [#1](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/1) I have implemented the following tasks:
* I assisted in the creation of the register GUI by developing reusable styled components. [#28](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/28)
* I also set up the userSlice redux store, which will later be beneficial for user management[#27](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/27).
* The user model creation was also one of my tasks [#14](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/14).

Next week I will be working on tasks for user story [P1](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/4)
* Create a plant model [#24](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/24)
* Create the plant store (plantSlice) [#25](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/25)

And on a task for user story [P2](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/5)
* Create main page [#23](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/23)

### Michael
For user story [#1](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/1) I have implemented the following tasks:
* [Set up user service #35](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/35)
* [Create user entity #24](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/24)


### Nordin
For user story [#1](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/1) I have implemented the following tasks:
* [Set up user repository #30](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/30)
* [Set up user controller #31](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/31)
* [Set up DTO and Mapper for user #36](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/36)

Additionally I have bought and linked the url plantparent.ch to the frontend and api.plantparent.ch to the backend.
* [ Register and connect URL plantparent.ch to services #17 ](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-project-management/issues/17)
* I have set up this document.

Next week I will be working on tasks for user story [P1](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/4)
* [Create Plant DTO & mapper #46](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/46)
* [ Set up Plant repository, service & controller #45 ](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/45)
* [ Create plant entity #42 ](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/42)

There were no real obstacles this week.


## Week 2, 29.03.2024 - 05.04.2024
### Focused User Stories
This week we focused on the first user interaction with the page.
* [U2: As a user, I want to be able to log in to the application so that I can benefit from its features.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/2)
* [P1: As a plant owner, I want to be able to create plants, so that I can manage them in the application.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/4)

### Lazaro
Last week, I focused on tasks corresponding to user story U2 [#2](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/2) and P1 [#4](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/4):
* I created the login guard and furthermore implemented a authenticator guard for the login and register GUI [#31](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/31)
* I created the "create plant" GUI [#26](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/26)
* I setup the plant model [#24](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/24)
* I implemented the plant store (plantSlice) [#25](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/25)

Next week, I will be working on tasks corresponding to P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6):
* Create "edit schedule" GUI [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/21)
* (Create "edit plant" GUI) [#22](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/22)


### Daniel
The main focus of this week was focusing on the signUp and login functionality...
* Created the user service with login and sign up funcitonality. [#30](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/30)

... and assisiting in some plant stuff
* Create a plant model [#24](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/24)
* Create the plant store (plantSlice) [#25](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/25)

* I also started working on the creating the main page [#23](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/23)

### Michael
This week i implemented the backend for user Story [[#2](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/2)].

Specifically, the following tasks:
* [#40](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/40) Get user (with exception handling)
* [#41](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/41) Generate & return token
* [#60](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/60) Implement logic for login, logout

### Nordin
For the user Story [[#4](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/4)] I have implemented the following tasks:
* [#45](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/45) Set up Plant repository and Service
* [#42](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/42) Create Plant entity
* [#46](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/46) [Partially] Create Plant DTO, Create Plant Mapper. TODO: Controller

Next week I will be working on tasks for user story [#5](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/5) or [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6).



## Week 3, 05.04.2024 - 12.04.2024
### Focused User Stories
This week we focused on the basic user interaction with the plants. 
* [P2: As a user, I want to view every plant that I have access to.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/5)
* [P3: As a plant owner, I want to be able to create a caring schedule for a plant, so that the plant receives the care it needs.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6)

### Lazaro


### Daniel


### Michael
This week i implemented the backend for user Story [[#5](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/5)].

Specifically, the following tasks:
* [#47](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/47) Return all plants for user 
* [#48](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/48) Setup REST API 

### Nordin



## Week 4, 12.04.2024 - 19.04.2024
### Focused User Stories
This week we focused on implementing the functionality for user collaboration.
* [C1: As a user, I want to be able to share access to a space or individual plants with other users, so that they can also view the required tasks and status of plants.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/12)
* [C2: As a user, when I watered a plant I want to confirm this, so that this event is marked as done.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/13)
* [P3: As a plant owner, I want to be able to create a caring schedule for a plant, so that the plant receives the care it needs.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6)

### Lazaro
Last week, I focused on tasks corresponding to user story P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6):
* I created the "edit schedule" GUI [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/21)
* I created the "edit plant" GUI [#22](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/22)

Next week, I will be working on tasks corresponding to P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6):
* Create "edit schedule" GUI [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/21)
* (Create "edit plant" GUI) [#22](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/22)



### Daniel


### Michael


### Nordin



## Week 5, 19.04.2024 - 26.04.2024
### Focused User Stories
This week we focused on making the site more informative, adding notifications and thus finishing up the MVP.
* [N1: As a plant owner, I want to receive a notification if another user did not take care of their tasks, so that I can remind them or do it myself.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/15)
* [P4: As a user, I want to see all upcoming tasks, so I can plan ahead and don't forget.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/7)

### Lazaro


### Daniel


### Michael


### Nordin



# Sprint 2, M4
Milestone 4 will be about finishing up and polishing your application for the final presentation.
## Week 6, 26.04.2024 - 03.05.2024
### Focused User Stories
This week we focused on 

### Lazaro


### Daniel


### Michael


### Nordin


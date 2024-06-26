# Weekly contributions
Before every TA meeting add the resolved development tasks to this document.

During the meeting every member gives a 3 minute update and answers the following:
* What did I do last week?
* What will I do this week?
* What are the obstacles to progress?


<br/><br/>

# Sprint 1, M3
## Week 1, 23.03.2024 - 28.03.2024 (+Easter break Week 2, 29.03.2024 - 05.04.2024)
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


## Week 3, 05.04.2024 - 12.04.2024
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
* Created the user service with login and sign up functionality. [#30](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/30)

... and assisting in some plant stuff
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


## Week 4, 12.04.2024 - 19.04.2024
### Focused User Stories
This week we focused on the basic user interaction with the plants. 
* [P2: As a user, I want to view every plant that I have access to.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/5)
* [P3: As a plant owner, I want to be able to create a caring schedule for a plant, so that the plant receives the care it needs.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6)



### Lazaro
Last week, I focused on tasks corresponding to user story P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6):
* I created the "edit schedule" GUI [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/21)
* I created the "edit plant" GUI [#22](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/22)

Next week, I will be working on tasks corresponding to P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6):
* create plant view gui [#42](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/42)
* Search for email API [#55](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/55)


### Daniel
This week I focused on 
* the main page and the plant components [#23](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/23)
* the watering/caring indicators with a confirmation popup [#20](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/20)
* the connection of the frontend to the backend.
* and fixing numerous bugs and issues.


### Michael
This week i implemented the backend for user Story [[#5](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/5)].

Specifically, the following tasks:
* [#47](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/47) Return all plants for user 
* [#48](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/48) Setup REST API 

### Nordin
* This week I finished the plant controller for https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/46
* I implemented the REST Endpoint for editing of Plants https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/49
* I applied refactorings for inconsistencies we found https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/70
  * Of plant entity "name" to "plantName"
 
Next week I want to work on
* Fixing an issue with recursion of the Plant/User entities when returned via REST
  * Extend calculated fields with the User and Plant entities to add a list of ids.
* Calculate and update the watering fields for plants on watering events.
 
I struggeld with finding the right syntax on returing the calculated fields for #70.


## Week 5, 19.04.2024 - 26.04.2024
### Focused User Stories
This week we focused on making the site more informative, adding notifications and thus finishing up the MVP.
* [P3: As a plant owner, I want to be able to create a caring schedule for a plant, so that the plant receives the care it needs.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6)
* [P4: As a user, I want to see all upcoming tasks, so I can plan ahead and don't forget.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/7)

### Lazaro
Last week, I focused on tasks corresponding to user story N1 [#15](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/15):
* create plant view gui [#42](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/42)
* Search for email API [#55](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/55)

Next week, I will be working on tasks corresponding to U3 [#3](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/3):
* create user view gui [#66](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/66)
* create "edit user" gui [#49](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/49)

### Daniel
This week I had to refactor the frontend to match the backend changes. (Nested objects are returned from the backend instead of only the objects the frontend asks for with the IDs)
* Fixed some plant creation bugs [#26](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/26)
* Refactored the redux store and all the components accessing the store [#27](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/27)
* Fixed login check and added a 7 day login expiration [#31](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/31)
* Fixed some display issues on the main page [#25](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/25)
* Worked on sending caring/watering confirmation to backend silently [#18](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/18)
* Displayed the caretakers of a plant on the plant page [#16](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/16)
* Created plant sharing GUI [#15](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/15)
* Fixed the PlantDTOs and DTOMappers (they now include caring schedule related information as well) [#36](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/36)
* Prepared for the Milestone 3 presentation


Next week I want to focus on the 
* Planning the next sprint
* Fixing the remaining issues with the backend/frontend communication


### Michael
This week i implemented the backend for user Story [[#12](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/12)] and an issue for the user story [[#15](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/15)].

Specifically, the following tasks:
* [#51](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/51): Create REST endpoint for sharing plant
    * Which allows users to add and delete caretakers of their plants, which is crucial for the collaboration aspect of our application.
* [#54](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/54) Create trigger REST endpoint
    * This is the endpoint that gets triggered by the GCP cloud scheduler. This checks the watering status of all plants and if necessary creates a JSON response that will be used by the external email API.

 Additionally, i added some more dummy data when starting up the server. 




### Nordin
This week I did some clean up and implemented logic some logic for https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6
* Refactor according to Meeting https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/70
  * This also fixed the recursive return of `plant[owner[plant[owner...` objects
  * Created the blueprint return fields for spaces
* Create logic for setting caring/watering interval and last watering/caring date -> plant service https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/50
* Created the cloud scheduler that triggers the emails (no commit task) https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/53

Next week I want to
* Plan the next sprint
* Configure and verify that emails for plants that have not been watered will be sent.

This week some obstacles to progress were, that it was not able to correctly return entities with only the id's for their containing relations. This cost me around two days.


---
# Sprint 2, M4
Milestone 4 will be about finishing up and polishing your application for the final presentation.

## Week 6, 26.04.2024 - 03.05.2024
### Focused User Stories
This week we focused on 
* [C1: As a user, I want to be able to share access to a space or individual plants with other users, so that they can also view the required tasks and status of plants.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/12)
* [C2: As a user, when I watered a plant I want to confirm this, so that this event is marked as done.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/13)
* [N1: As a plant owner, I want to receive a notification if another user did not take care of their tasks, so that I can remind them or do it myself.](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/15)
* Sprint planning

### Lazaro
Last week, I focused on tasks corresponding to user story U3 [#3](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/3):
* create user view gui [#66](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/66)
* create "edit user" gui [#49](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/49)

Next week, I will be working on tasks corresponding to
P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6) and
P6 [#9](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/9):
* create "edit password" gui [#67](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/67)
* create "delete plant" button &
  popup [#71](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/71)


### Daniel
This week I assisted in the planning of the next sprint. I also worked on the following tasks:
* Added conditional editing to plants (only owner can edit plant or schedule) [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/21)[#22](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/21)
* Created QR code service and generator [#54](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/54)
* Added QR code download option with save as [#53](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/53)


Next week I want to focus on the
* Creating spaces and adding/removing plants to/from spaces
* Fixing the remaining issues with the backend/frontend communication

### Michael
This week we focused on finishing user story [[#15](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/15)] 
* and for that I adjusted the endpoint and related methods which get called by the external API [#113](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/113).

I also started with the user story [[#16](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/16)]
* where I added the methods for Space creation, deletion and editing in the service layer [#85](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/85)


### Nordin
This week I worked on:
* [#84](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/84) REST endpoints for space creation/editing/deletion
* [#56](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/56) Configure E-Mail API, Tests and integrate backend Service to Mailjet via POST
  * **Team members need to set environment variables with secret if they want to use this function**


## Week 7, 03.05.2024 - 10.05.2024
### Focused User Stories
* Fixing feedback from Beta-Testing
* 

### Lazaro

Last week, I focused on tasks corresponding to user story
P3 [#6](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/6) and
P6 [#9](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/9):

* create "edit password" gui [#67](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/67)
* create "delete plant" button & popup [#71](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/71)

Next week, I will be working on tasks corresponding to P9 [#125](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/125):

* Create image upload functionality in plant overview [#69](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/69)
* Resize and crop the image to be the appropriate shape/size for the frontend [#70](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/70)

### Daniel

This weed my main focus was to fix some of the bugs mentioned in the feedback for M3.

* I removed the bell icon, as it is not going to be used in our final
  application [#73](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/73)
* I fixed the reload issues. Now when a plant is updated (or watered/cared for), the entities in the redux store are
  fetched from the backend and updated automatically.
* I fixed the editUser and editPassword
  pages. [#49](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/49), [#67](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/67)
* Changed the user update controller in the backend to return the updated user instead of only a
  204. [#31](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/31)

Next week (after all space functionalities are implemented in the backend) I want to focus on the frontend
implementation.

### Michael

This week I completed the following tasks:

* [#87](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/87) Implemented the service layer
  methods for assigning plants to a space
* [#86](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/86) Implementing the corresponding
  controller methods for assinging plants to a space
* [#133](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/133) Implementing the functionality
  to retrieve all plants for a given space (Controller & Service layer)

### Nordin
This week I worked on:
* [#120](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/120) Implementing a secret loader that locally loads secrets from environment variables and on GAE loads secrets from Google Secrete Manager.
* [#123](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/123) Correctly applying SPF and DKIM DNS records for plantparent.ch, preventing our mails to be sent to quarantine.
* [#128](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/128) Research and configure Google
  Cloud Storage so we can use it as a image hosting platform.
* [#129](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/129) Create an image upload
  Endpoint, extend the existing configuration with the image url, create a GCP Storage Service so we can upload images
  and link them to plants automatically.
* [#130](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/130) Extend existing objects to incorporate the image information. 


## Week 8, 10.05.2024 - 17.05.2024
### Focused User Stories

### Lazaro

Last week, I focused on tasks corresponding to user story P9 [#125](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/125):

* create image upload functionality in plant overview [#69](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/69)
* resize and crop the image to be the appropriate shape/size for the frontend [#70](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/70)

Next week, I will be working on these tasks:

* Bug: Only enable 'signup' button if passwords match [#98](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/98)
* Create README.md for the frontend [#79](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/79)

### Daniel
This week my main focus was again on fixing the bugs mentioned in the feedback for M3.
I fixed
* error messages
* added tooltips to the form fields
* made sure only past days can be selected
* unlimited length names/descriptions by adding character limits
* the caring instruction form field by creating a text area for to display more text
* the negative number input for intervals (only positive possible)
* the -/+ prefix issue
all part of [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-project-management/issues/21)

I also added some collaborative features:
* plans are fetched periodically from the backend (every 5 seconds)
* Added caring & watering animations in case a plant has been watered/cared for by a different user
also part of [#21](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-project-management/issues/21)

Backend stuff
* I changed the spaces controller PUT method to return the updated space instead of a 204 [#88](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/88)
* I added the spaceMembers list to the SpaceGetDTO and mapper [#88](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/88)

Spaces:
* I created the space model and service functions for the spaces [#59](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/59)
* I created the space page [#57](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/57)
* I created the spaces components [#58](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/58)

Next week I want to focus on the completion of the space features
* CRUD space [#56](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/56) [#55](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/55)
* Adding removing plants to space [#64](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/64)
* Adding removing users to space [#65](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/65)


### Michael

This week I completed the following tasks:
* [#89](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/89) Implemented the service layer
  methods for adding users to spaces as members
* [#88](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/88) Implemented the corresponding controller methods for adding users to spaces
* [#116](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/116) Implemented the necessaray Many-To-Many relation between Spaces and Members
* [#132](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/132) Implemented the controller and service methods to retrieve all spaces for a given user
* [#142](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/142) Created an initial draft for the backend ReadMe. 
* Other things, such as refactoring the DatabaseLoader, adding spacemembership dummy data and adding tests to meet requirement that every REST endpoint has at least one test. 

### Nordin
* [#139](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/139) Fixed a bug where apps in GAE can only write to /tmp. Affected image upload.
* [#176](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/176) Upgrade Github Actions version and reconfigure it
* [#143](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/143) Implement Postgres as DB Backend only for Prod
* [#177](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/177) Correctly separating dev H2 db and prod postgres db
* [#178](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/178) Fix SonarCloud Quality Gate issues.


## Week 9, 17.05.2024 - 24.05.2024
### Focused User Stories

### Lazaro

Last week, I worked on these tasks:

* Bug: Only enable 'signup' button if passwords match [#98](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/98)
* Create README.md for the frontend [#79](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/79)


### Daniel
This week I focused on the completion of the space features and fixing of bugs:

#### Frontend

In relation to the spaces I worked on...
* ...editing a space [#56](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/56)
* ...deleting a space [#55](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/56)
* ...adding/removing plants to/from a space [#64](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/64)
* ...adding/removing users to/from a space [#65](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/65)

I also created a weather api which displays live location based weather information in the header [#99](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/99)
Creating a plant from a space now automatically adds the plant to the space and redirect to the space page [#94](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/94)

I also worked on numerous bugs:
* Spaces should not be created when cancel is clicked [#83](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/83)
* Already existing username error is now displayed correctly [#88](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/88)
* Increased the size of the selector component to clearly show that it is scrollable [#89](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/89)
* Fixed some incorrect tooltip issues [#95](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/95)
* Fixed incorrect last watering/caring date display in edit schedule [#92](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/92)
* Fixed bug related to the fetching of plants of incorrect space [#85](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/85)
* Fixed bug which crashes members space page if owner adds plants to the space [#96](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/96)
* Fixed and edit schedule issue [#101](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/101)
* Fixed error message display issue on the create space page [#191](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/191)

#### Backend

* I improved spaces service and controller to not require owners of plants. 
* I also added endpoints which add a plant to a space with the respective checks.
* I also made sure that a space is removed from plant if a space is deleted.
All part of [#88](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/88) & [#89](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/89)

Finally, I worked on many small improvements, bugs and deployment issues which were not part of a specific issue.

### Michael
This week I addressed a variety of minor things, that weren't all part of specific issues, such as:
* Fixing typos
* Adjusting and adding tests to account for changes made by others
* Adjusting the create plant endpoint to take in a optional parameter if the plant is created within a specific space, part of this issue [#94](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-client/issues/94) 
* Adjusted the methods for adding/deleting plants to/from a space, to ensure caretaker assignment is correct [#189](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/189)
* Added test for [#192](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/192)
* Fixed a bug where plant owners also get assigned as caretakers, when plant is created within a space [#200](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/200)

### Nordin
This week I focused a lot on testing and documentation.
* Opened a dozen bug and improvement tasks
* [#191](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/191), [#190](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/190) Researched the difference in exception handling and exception message passing in prod and dev environment.
* Updated architecture diagram.
* [#198](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/issues/198) Fixed problem where sometimes
  duplicate relations are returned.

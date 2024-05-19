[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs24-group-01_sopra-fs24-plantparent-server&metric=alert_status)](https://sonarcloud.io/summary/overall?id=sopra-fs24-group-01_sopra-fs24-plantparent-server) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs24-group-01_sopra-fs24-plantparent-server&metric=coverage)](https://sonarcloud.io/summary/overall?id=sopra-fs24-group-01_sopra-fs24-plantparent-server)

# PlantParent

## Introduction

**PlantParent** is a comprehensive houseplant management system designed to simplify and enhance the way users interact with their plants. Motivated by the challenges plant owners face in maintaining the health of their green companions, our application provides a user-friendly platform for users to create detailed profiles for each of their plants, schedule watering and caring tasks, and coordinate with other caretakers. The application allows users to organize their plants into customizable spaces, making it easier to manage plant care efficiently. Equipped with a notification system to alert users when a plant's care is overdue, **PlantParent** ensures that all plants receive the attention they need to thrive. Our goal is to foster a more engaged and informed community of plant enthusiasts, making plant care a seamless part of their daily lives.



## Technologies
**PlantParent** leverages a range of powerful technologies to ensure robust and scalable server-side functionality:

<img src="https://storage.googleapis.com/plant-profiles-b7f9f9f1-445b/arcitecture-sql.svg">

- **Java & Spring Boot**: Utilizes Java for backend development, with Spring Boot facilitating rapid application development, including integrated modules like Spring Data JPA and Hibernate for ORM.
- **Gradle**: Employed for automated building and dependency management.
- **Google Cloud Platform (GCP)**: Hosted on Google Cloud Platform
  - **Google App Engine**: Hosting the backend service. Spinning up a new session upon request.
  - **Secret Manager**: Securing API Tokens and only providing them to service users that have the correct access
    levels.
  - **Cloud IAM**: Managing the access permissions for all developers as well as service users to the respective
    services.
  - **Google Cloud Storage**: Providing a scalable and performant storage solution for plant images.
  - **Cloud Scheduler**: Providing regularly scheduled jobs that will execute mails to the clients.
  - **Cloud SQL**: Persistent data storage across different runs of GAE sessions.
- **MailJet API**: Integrated for sending email notifications, enhancing user interaction and engagement.
- **SonarCloud**: Used for continuous code quality checks and security scanning to maintain high standards of code health.
- **GitHub Actions**: Used for continuous integration and deployment, ensuring that every commit is built and tested automatically. This setup supports a robust development cycle and maintains high code quality, facilitating consistent and reliable updates to the application.

## High-level Components


**PlantParent** is architected around several high-level components that enable efficient plant and user management. Here's a breakdown of the key components:

1. **User Management**: Manages all aspects related to users, including registration, authentication, and user profile management. This component interacts closely with the plant and space management systems to coordinate caretaking responsibilities and space memberships.
   - Main class: [UserService](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/blob/e06ee0cdb0bd6eeb6256053d1a5b641a8903e42b/src/main/java/ch/uzh/ifi/hase/soprafs24/service/UserService.java)

2. **Plant Management**: Central to managing detailed plant information, schedules for watering and caring, and assignment of caretakers. It handles the logistics of plant care based on user interactions and predefined schedules.
   - Main class: [PlantService](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/blob/e06ee0cdb0bd6eeb6256053d1a5b641a8903e42b/src/main/java/ch/uzh/ifi/hase/soprafs24/service/PlantService.java) 

3. **Space Management**: Organizes plants into distinct spaces, enhancing the management of plant care by grouping them according to their location or environment requirements. This component is crucial for users who manage multiple plants across different physical locations.
   - Main class: [SpaceService](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/blob/e06ee0cdb0bd6eeb6256053d1a5b641a8903e42b/src/main/java/ch/uzh/ifi/hase/soprafs24/service/SpaceService.java)

4. **Notification System**: Sends automated notifications to users about plant care activities to ensure timely attention is given to each plant. This component uses the external service MailJet for email notifications.
   - Functionality embedded in the [PlantService](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server/blob/e06ee0cdb0bd6eeb6256053d1a5b641a8903e42b/src/main/java/ch/uzh/ifi/hase/soprafs24/service/PlantService.java). 

Each component is designed to interact seamlessly with others, providing a cohesive and intuitive user experience. The services mentioned are crucial in orchestrating the application logic and ensuring that data flows correctly across the system.

## Launch & Deployment
### Getting Started
To get started with **PlantParent-Server**, follow these steps to set up the project locally.

#### Prerequisites

Make sure you have installed:

- Your IDE, preferably IntelliJ
- Java JDK 17
- Gradle 7.6

As well have:

- Access to the [GitHub project](https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server) for the CI/CD
  Pipeline.
- Access to the GCP project `sopra-fs24-group-01-server`
- MailJet API credentials for sending notifications from the dev session.
- Installed and initialized the gcloud cli: https://cloud.google.com/sdk/docs/install-sdk

#### Setup Instructions


1. **Clone the Repository:**
   ```bash
   git clone https://github.com/sopra-fs24-group-01/sopra-fs24-plantparent-server.git

2. **Build**
    ```bash
    ./gradlew build
    ```

3. **Run**

    ```bash
    ./gradlew bootRun
    ```

You can verify that the server is running by visiting `localhost:8080` in your browser.

4. **Test**

    ```bash
    ./gradlew test
    ```
### Development
Developers are encouraged to push to feature branches and create pull requests for code reviews. Ensure that all merge conflicts are resolved and all tests pass before requesting a review. 

Please create feature branches from the `development` branch.

### Deployment
For deployment, the project is set up with GitHub Actions for continuous integration and continuous deployment (CI/CD) to Google Cloud Platform. To perform a release:
1. Merge your changes into the main branch via pull request.
2. Ensure all GitHub Actions checks pass.
3. Once merged, GitHub Actions will automatically deploy the new version to Google Cloud.

## Roadmap
Here are some of features we plan to implement in the future to enhance our application:
1. **History of Events**
   * **Description**: Implement a feature to record and display the history of care events for each plant, such as watering or fertilizing. This will help users gain a better understanding of a plant's care history and identify potential areas for improvement in their care routine.
2. **Feedback System**
   * **Description**: Allow users to post feedback directly on a plant's page or within a specific space. This feature will enable caretakers to share tips, provide feedback, and communicate effectively, fostering a collaborative environment for plant care.
3. **Subtasks for Plant Care**
    * **Description**: Enhance the flexibility of scheduling care activities by allowing users to define specific tasks and subtasks, such as fertilizing, pruning, or repotting. This detailed task management will help users tailor care routines to the unique needs of each plant, ensuring optimal health and growth. 

These upcoming features are designed to provide our users with more detailed insights, collaborative tools, and costumized care options, making plant management more interactive and precise.

## Authors and Acknowledgement
### Team Members:

* Nordin Dari - Back-End Development - [NorDar](https://github.com/NorDar)
* Daniel Gergely - Front-End Development - [Danielgergely](https://github.com/Danielgergely)
* Lazaro Nicolas Hofmann - Front-End Development - [geringproduktiv](https://github.com/geringproduktiv)
* Michael Sigg - Back-End Development - [M-Sigg](https://github.com/M-Sigg)

### Acknowledgements:
* Stefan Schuler: Special thanks to our TA for continuous guidance and feedback provided during development.
* Prof. Thomas Fritz and the TA Team: Appreciation for the course materials as well as the provided SoPra templates. 

## License

This project is licensed under the Apache License 2.0

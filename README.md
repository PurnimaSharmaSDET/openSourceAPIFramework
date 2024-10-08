# openSourceAPIFramework

An open-source API testing framework built using Java, Maven, TestNG, and a suite of powerful libraries such as Rest Assured, Jackson, Extent Reports, and Apache POI.

## Project URL

[openSourceAPIFramework Blog](https://testerbloghub.blogspot.com/)

## Project Information

- **Group ID**: com.opensourceFramework
- **Artifact ID**: openSourceAPIFramework
- **Version**: 1.0.0-SNAPSHOT
- **Java Version**: 17

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
3. [Dependencies](#dependencies)
4. [Plugins](#plugins)
5. [Usage](#usage)
6. [Reporting](#reporting)
7. [License](#license)

## Introduction

This framework is designed to automate API testing efficiently, using a structured approach leveraging Page Object Model (POM) and other Java libraries like Rest Assured for API calls and Apache POI for Excel file interactions. The framework also supports rich reporting using Extent Reports.

## Getting Started

### Prerequisites

- Java 17
- Maven 3.x
- IDE (e.g., IntelliJ IDEA or Eclipse)

### Setup

1. Clone the repository.
   ```bash
   git clone https://github.com/your-repo/openSourceAPIFramework.git

## Dependencies

The project uses several dependencies to enhance functionality and support various features:

1. **MySQL Connector (mysql-connector-java: 8.0.22)**  
   This dependency allows the framework to connect and interact with MySQL databases for any required database operations.

2. **Extent Reports (extentreports: 5.0.9)**  
   Used for generating detailed and visually appealing HTML reports, Extent Reports help track test execution results and create comprehensive reports.

3. **Klov Reporter (klov-reporter: 5.0.9)**  
   Klov Reporter integrates with Extent Reports to provide real-time reporting capabilities, making it easier to monitor test progress as it happens.

4. **Log4j API and Log4j Core (log4j-api & log4j-core: 3.0.0-beta2)**  
   These dependencies provide logging functionality, helping in tracking and logging application events and test information for easier debugging and monitoring.

5. **Apache POI (poi & poi-ooxml: 5.3.0)**  
   Apache POI is used for handling Excel files within the framework. It provides the ability to read from and write to Excel files, which is useful for data-driven testing.

6. **Jackson Libraries (jackson-databind & jackson-annotations: 2.18.0)**  
   Jackson libraries are used for JSON serialization and deserialization, allowing the framework to convert Java objects to JSON and vice versa. This is especially useful for handling REST API responses.

7. **TestNG (testng: 7.10.2)**  
   TestNG is the testing framework used for organizing, executing, and managing tests within the project. It supports annotations, parallel test execution, and reporting features.

8. **Rest Assured (rest-assured: 5.5.0)**  
   Rest Assured is a library specifically designed for testing REST APIs. It simplifies HTTP requests and response validation, making API testing efficient and effective.


## Plugins

The project is configured with the following Maven plugins, each serving a specific purpose:

1. **Maven Clean Plugin (maven-clean-plugin: 3.1.0)**  
   This plugin is responsible for cleaning up files generated during the build process. It ensures that the build starts with a clean state by removing temporary files, previous builds, and other generated resources.

2. **Maven Resources Plugin (maven-resources-plugin: 3.0.2)**  
   Manages project resources, such as files located in the `src/main/resources` directory. It copies resources from the source directory to the output directory, ensuring they are available during build and execution.

3. **Maven Compiler Plugin (maven-compiler-plugin: 3.8.0)**  
   This plugin is used to compile the source code of the project, targeting Java 17 as configured. It manages the compilation process, ensuring the Java code is correctly built.

4. **Maven Surefire Plugin (maven-surefire-plugin: 2.22.1)**  
   Executes the TestNG tests defined in the project. It handles test execution, logging results, and generating test reports, making it integral for continuous integration setups.

5. **Maven JAR Plugin (maven-jar-plugin: 3.0.2)**  
   Bundles the project into a JAR file, which is the standard packaging format for Java projects. This plugin assembles the compiled code and resources into a distributable format.

6. **Maven Install Plugin (maven-install-plugin: 2.5.2)**  
   Installs the built artifact (JAR file) into the local Maven repository. This allows other projects on the same machine to access and use the generated artifact.

7. **Maven Deploy Plugin (maven-deploy-plugin: 2.8.2)**  
   Deploys the built artifact to a remote repository, making it available for other developers and projects to use. This is crucial for sharing builds across teams and CI/CD setups.

8. **Maven Site Plugin (maven-site-plugin: 3.7.1)**  
   Generates project documentation and reports. It provides insights such as test coverage, project dependencies, and code analysis in an HTML format, enhancing project visibility.

9. **Maven Project Info Reports Plugin (maven-project-info-reports-plugin: 3.0.0)**  
   Provides detailed project information reports, including dependencies, plugins used, team information, and more. It helps in understanding the project's structure and configuration.

## cd openSourceAPIFramework

Build the project using Maven:

## mvn clean install
To run tests
## mvn clean test
To clean package or build
## mvn clean package
[![](https://jitpack.io/v/PurnimaSharmaSDET/openSourceAPIFramework.svg)](https://jitpack.io/#PurnimaSharmaSDET/openSourceAPIFramework)

This uses Rest Assured library

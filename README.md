# ForkExec

**Fork**: delicious menus for **Exec**utives

Distributed Systems 2018-2019, 2nd semester project

## Authors

Group A18

João Tavares 86443 joaobernardoct

Pedro Antunes 86493 pedroantunesf

André Leitão 87629 AFilipee


## Summary

The code's objective is to develop a system based on SOAP Web Services, for the management of a meal ordering platform. The system allows its users to order meals at different restaurants, as an online food ordering and delivery platform.
The platform is implemented in Java using JAX-WS (Java API for XML Web Services).
The ForkExec system consists of a hub that serves as an intermediate service between users and the autonomous services that make up the back-end of the ForkExec system. It receives surveys and orders, and it's responsible for delegating searches to restaurants.

By creating the aforementioned code, I could learn how to use technologies, namely, Web Services applied to distributed systems and guaranteeing non-functional requirements such as security, fault tolerance, scalability, and reconfigurability.
Taking this into account, I was able to program an application using a distributed name service, be able to analyze requirements, formulate policies, and use techniques to fulfill such requirements, as well as introduce procedures for ensuring fault tolerance in the applications.

________________________________________________________________

### How to run

Server jUDDI:  localHost

cd A18-ForkExec
cd rst-ws-cli
	mvn install -DskipTests
cd ../rst-ws
	mvn compile exec:java
	mvn exec:java -Dws.i=2
    mvn exec:java -Dws.i=3
cd ../pts-ws-cli
	mvn generate-sources
	mvn install -DskipTests
	mvn compile
cd ../pts-ws
	mvn generate-sources
	mvn install -DskipTests
	mvn compile exec:java
cd ..
	mvn install -DskipTests
	mvn generate-sources (opt)
	mvn install -DskipTests (opt)
cd ../hub-ws
	mvn generate-sources
	mvn compile exec:java
cd ../hub-ws-cli
	mvn generate-sources (opt)
	mvn compile exec:java
	mvn verify
cd ../rst-ws-cli
	mvn compile exec:java
	mvn verify
cd ../pts-ws-cli
	mvn exec:java
	mvn verify


### Code identification

In all the source files (including POMs), please replace __CXX__ with your Campus: A (Alameda) or T (Tagus); and your group number with two digits.

This is important for code dependency management 
i.e. making sure that your code runs using the correct components and not someone else's.


## Getting Started

The overall system is composed of multiple services and clients.
The main service is the _hub_ service that is aided by the _pts_ service. 
There are also multiple _rst_ services, one for each participating restaurant.

See the project statement for a full description of the domain and the system.


### Prerequisites

Java Developer Kit 8 is required running on Linux, Windows or Mac.
Maven 3 is also required.

To confirm that you have them installed, open a terminal and type:

```
javac -version

mvn -version
```


### Installing

To compile and install all modules:

```
mvn clean install -DskipTests
```

The tests are skipped because they require each server to be running.


## Built With

* [Maven](https://maven.apache.org/) - Build Tool and Dependency Management
* [JAX-WS](https://javaee.github.io/metro-jax-ws/) - SOAP Web Services implementation for Java

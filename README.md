# spring-security-oauth2-jwt

#### v0.0.1-SNAPSHOT

## 📰 Description

Service responsible for managing users, authentication and authorization

##  📔 Table of Contents
<!--ts-->
* [Pre-requirements](#✂️-pre-requirements)
* [How to use](#🎮-how-to-use)
	* [Setup](#setup)
	* [Run](#run)
* [Tests](#📌-tests)
* [Links](#🔗-links)
<!--ts-->

## ✂️ Pre-requirements

* [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-doc-downloads.html)
* [Maven](https://maven.apache.org/)
* [Spring Security](https://docs.spring.io/spring-security/reference/getting-spring-security.html)
* [OAuth2](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)

## 🎮 How to use

###  Setup

Clone repository

```shell
git clone https://github.com/annarafaeladev/spring-security-oauth2-jwt.git
```

Enter the project folder
````shell
cd ./spring-security-oauth2-jwt
````

### Run
command:
```shell
mvn spring-boot:run
```

## 📌 Generate RSA Key

Chose package example: 
```
cd ./src/main/resources
```
#### Generate private Key

Run this command to generate a 4096-bit private key and output it to the private.pem file. If you like, you may change the key length and/or output file.

```
openssl genrsa -out private.key 2048
```

#### Generate public  Key

Given a private key, you may derive its public key and output it to public.pem using this command. (You may also paste your OpenSSL-generated private key into the form above to get its public key.)

```
openssl rsa -in private.key -pubout -out public.key
```
Output it's OK: 

```
writing RSA key
```

## 📌 Tests
To execute all tests, run the following command:
```
mvn test
```

## 🔗 Links
* [LinkedIn](https://www.linkedin.com/in/annarafaeladev/)
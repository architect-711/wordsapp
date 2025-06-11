# WordsApp

## Description
When you learn new language you write down new words in some notebook,
but it quickly gets too hard to maintain it: lookup, insert, sorting is
immensely hard or impossible. This app acts as a replacement with
upgrades, you can do more than just save new words: saves words to groups,
repeat them with the Quiz mode.

## API
Search in `/docs/` folder for the Yaak json config.

## How to build and run app?

As of today the app is in development mode, so the only one way is to
<b>pull source code</b>. You need Java 23 and PostreSQL installed. Then visit 
`/src/main/resources/application.properties` file to configure database connection.

## Tech Stack
* Spring boot
* PostgreSQL

## Project Structure
* /controller - contains all controllers
* /model - contains the dto, db, mapper folders, for DTOs, @Entity's and their mappers respectively
* /repository - contains all repository interfaces
* /service - contains all services in their subdirectories, each service <b>MUST</b> has its own interface with javadoc
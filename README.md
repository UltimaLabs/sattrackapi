# Satellite Tracking API

SatTrackAPI provides a satellite tracking RESTful Web Service.

## Built With

* [Spring Boot 2](https://spring.io/projects/spring-boot/)
* [Orekit, a low level space dynamics library written in Java](https://www.orekit.org/)
* [Gradle Build Tool](https://gradle.org/)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* [JDK 1.8 or higher](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Installing

Checkout the code.

```
$ git clone https://github.com/UltimaLabs/sattrackapi.git
```

### Configuration

Application is configured by editing the `src/main/resources/application.yml` file.
Configuration is read at the application startup, so for the configuration changes to become active, you need to restart the application.

#### Orekit data folder

In order to run the application, you first have to download the [Orekit data files](https://gitlab.orekit.org/orekit/orekit-data/-/archive/master/orekit-data-master.zip).
Unzip the downloaded data to a folder on your local machine. Edit the `src/main/resources/application.yml` config file and adjust the value of the `orekitDataFolder` variable.
Please make sure the application has proper access rights for reading the data files.

#### TLE urls

`tleUrls` contains a list of one or more TLE data source URLs. TLE data is downloaded, parsed, and stored internally by the application during the startup and refreshed later (see `tleUpdateCron`).
The ones at `download.ultimalabs.com` are downloaded twice each day, at 09:36 and 21:17, from `www.celestrak.com`.
They are provided for convenience, so `www.celestrack.com` isn't accessed every time the application is started (which happens often during the development).

#### TLE update cron

`tleUpdateCron` specifies a time (local, not UTC) at which TLE data should be refreshed, i.e. retrieved from the URLs specified in the `tleUrls` list.
Please note that TLEs should be refreshed on a daily basis, because maximum accuracy is guaranteed in a 24h range period before and after the provided TLE epoch.

### Building and running the Spring Boot application

The application must be able to access the URLs specified in the `tleUrls` list using the HTTP protocol.
To run the application, execute the following Gradle command:

```
./gradlew bootRun
```

This command will run the Spring Boot application on the default port 8080 directly. After a successful startup you can open your browser and access the application at `http://localhost:8080`.

To build the executable jar you can execute the following command:

```
./gradlew bootJar
```

The executable jar is located in the build/libs directory and you can run it by executing the following command:

```
java -jar build/libs/sattrackapi-0.0.1-SNAPSHOT.jar
```

By default, the application log is output to `stdout`.

### Working with the application

By default, you can access the application at `http://localhost:8080`. You can use a browser, or the free [Postman app](https://www.getpostman.com/downloads/).
There are several application "modules" you can access. All the API endpoints are accessed using the HTTP GET method.
You can use either the "Satellite Catalog Number" (NORAD ID) or the "International Designator" (COSPAR Designator) as a satellite identifier ("98067A", "1998-067A" and "25544" are all valid identifiers for the ISS).

#### Retrieve a satellite TLE data

Retrieve the TLE data for a satellite (ISS - 25544):

http://localhost:8080/api/v1/tles/25544

#### Calculate the nadir

Calculate the nadir at the current time (time of the request) for the given satellite:

http://localhost:8080/api/v1/positions/25544

#### Retrieve next pass data

Retrieves the data for the next pass, with or without the pass details.

The request parameters are:

* satellite identifier
* `lat` - observer latitude (degrees)
* `lon` - observer longitude (degrees)
* `alt` - observer altitude (meters)
* `minEl` - minimum elevation threshold; passes with maximum elevation below this limit will be omitted (meters)
* `step` - step size for the event details data; when omitted, no details are returned (seconds)

The pass data includes:
* `tle` - TLE used for prediction
* `now` - the current time
* `wait` - a number of seconds between `now` and satellite `rise`
* `rise` - time when the satellite rises above the `minEl`
* `midpoint` - pass midpoint, as an event details data point (see below)
* `set` - time when the satellite sets below the `minEl`
* `duration` - event duration
* `eventDetails` - event details (if requested), see below

Event details data includes:
* `t` - data point timestamp
* `az` - azimuth
* `el` - elevation
* `dst` - distance from the observer to the satellite (meters)
* `dop` - Doppler shift (Hz)

There are a couple of important implementation details: 
* **all returned timestamps are UTC**; requesting client app is responsible for converting timestamps to local time, if needed
* the app will return an empty result if no pass above `minEl` occurs within the next 48 hours  

Several request examples:

* http://localhost:8080/api/v1/passes/98067A/lat/46.1613/lon/15.7534/alt/200/minEl/20/step/1/ (observer: Ultima)
* http://localhost:8080/api/v1/passes/98067A/lat/64.1333/lon/-21.9333/alt/61/minEl/20/step/5/ (Reykjavik, 5s step)
* http://localhost:8080/api/v1/passes/98067A/lat/-36.8405/lon/174.7400/alt/6/minEl/30/step/0.1/ (Auckland, 30 degrees minimum elevation, 0.1 second step)
* http://localhost:8080/api/v1/passes/98067A/lat/-22.9083/lon/-43.1964/alt/0/minEl/15/ (Rio de Janeiro, without the details)


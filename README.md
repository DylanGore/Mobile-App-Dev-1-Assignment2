# Mobile App Development 1: Assignment 2

This is a Kotlin-based weather app for Android.

[View Releases](https://github.com/DylanGore/Mobile-App-Dev-1-Assignment2/releases)

## Features

-   Display weather warnings from [Met Éireann](https://met.ie)
-   Display most recent conditions reported by [Met Éireann](https://met.ie)'s 25 weather observation stations
-   Display a weather forecast for a specified location using data from the [Meteorologisk institutt (met.no)](https://met.no)
-   Save and load a list of locations to/from a JSON file

## Development

In order to setup a development environment, you must add the following Gradle properties. These can be added to either the project gradle.properties file or the global gradle.properties file.

```groovy
MAPBOX_DOWNLOADS_TOKEN=sk.xxxxxxxxxx
MAPBOX_ACCESS_TOKEN="pk.xxxxxxxxxx";
```

For more details on where to get the required tokens, see the [MapBox Android Installation Guide](https://docs.mapbox.com/help/tutorials/first-steps-android-sdk/).

## Branches

-   [main](https://github.com/DylanGore/Mobile-App-Dev-1-Assignment2/tree/main) - Main branch
-   [development](https://github.com/DylanGore/Mobile-App-Dev-1-Assignment2/tree/development) - Development branch

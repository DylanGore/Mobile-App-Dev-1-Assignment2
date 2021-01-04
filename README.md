# Mobile App Development 1: Assignment 2

![Build Status](https://github.com/DylanGore/Mobile-App-Dev-1-Assignment2/workflows/Test,%20Build%20and%20Create%20Release/badge.svg)

This is a Kotlin-based weather app for Android.

[View Releases](https://github.com/DylanGore/Mobile-App-Dev-1-Assignment2/releases)

## Notable Features

-   Display weather warnings from [Met Éireann](https://met.ie)
-   Display most recent conditions reported by [Met Éireann](https://met.ie)'s 25 weather observation stations
-   Display a weather forecast for a specified location using data from the [Meteorologisk institutt (met.no)](https://met.no)
-   Save and load a list of locations to/from a JSON file
-   [MapBox](https://mapbox.com) maps to view and choose locations (User location permission can be granted if desired)
-   The option to add observation stations as saved locations or just view a weather forecast for that area
-   Automated testing, linting and APK building using GitHub actions

## Android / Material Design Notable Features

-   Use of multiple Material design components / Material Design-themed standard components
    -   MaterialTextView
    -   Switch
    -   ActionBar
    -   SnackBar
    -   SwipeRefreshLayout
    -   FloatingActionButton
    -   MaterialButton
-   Use of Material Design icons
-   Theme, fonts and colors are all based on the Material Design guilelines
-   Dark mode is supported
-   Launcher icon is adaptive and will work with any configured icon shape
-   Background intents / services are used to request data from the various APIs

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

## Attribution

-   [Material Design Icons](https://materialdesignicons.com/)

## References

-   [Android Developer Documentation](https://developer.android.com/docs)
-   [Material Design Documentation](https://material.io/)
-   [MapBox Android SDK Documentation](https://docs.mapbox.com/android/maps/guides/)
-   [Migrating the deprecated Kotlin Android Extensions compiler plugin](https://proandroiddev.com/migrating-the-deprecated-kotlin-android-extensions-compiler-plugin-to-viewbinding-d234c691dec7)
-   [How to know when you’re using dark mode programmatically](https://medium.com/@saishaddai/how-to-know-when-youre-using-dark-mode-programmatically-9be83fded4b0)
-   [davidmoten/geo](https://github.com/davidmoten/geo)
-   [Pull to Refresh with RecyclerView in Android with Example](https://www.geeksforgeeks.org/pull-to-refresh-with-recyclerview-in-android-with-example/)
-   [Kotlin Android Broadcast Intents and Broadcast Receivers](https://www.techotopia.com/index.php/Kotlin_Android_Broadcast_Intents_and_Broadcast_Receivers#An_Overview_of_Broadcast_Intent.EF.BB.BFs)
-   [Android BroadcastReceiver Example Tutorial](https://www.journaldev.com/10356/android-broadcastreceiver-example-tutorial#sending-broadcast-intents-from-the-activity)
-   [IntentService is deprecated, how do I replace it with JobIntentService?](https://stackoverflow.com/questions/62138507/intentservice-is-deprecated-how-do-i-replace-it-with-jobintentservice)
-   [Setting Up the Search Interface](https://developer.android.com/training/search/setup)
-   [Make Search Bar Filter Work Using Fragment & ListView In Android](https://medium.com/@royanimesh2211/make-search-bar-filter-work-using-fragment-listview-in-android-bc4ee921450d)

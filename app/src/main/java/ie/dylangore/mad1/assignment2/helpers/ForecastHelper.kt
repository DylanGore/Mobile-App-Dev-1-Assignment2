package ie.dylangore.mad1.assignment2.helpers

import ie.dylangore.mad1.assignment2.R

object ForecastHelper {

    /**
     * Convert the weather symbol property included in the weather forecast to an icon
     *
     * @param condition the weather condition
     * @return the icon
     */
    @Suppress("SpellCheckingInspection")
    fun getWeatherIcon(condition: String?):Int{
        return when(condition){
            "clearsky", "clearsky_day" -> R.drawable.weather_sunny
            "clearsky_night" -> R.drawable.weather_night
            "cloudy", -> R.drawable.weather_cloudy
            "partlycloudy", "partlycloudy_day", "fair", "fair_day" -> R.drawable.weather_partly_cloudy
            "partlycloudy_night", "fair_night" -> R.drawable.weather_night_partly_cloudy
            "fog" -> R.drawable.weather_fog
            "lightrain", "lightrainshowers_night", "rainshowers_night" -> R.drawable.weather_rainy
            "lightrainshowers_day", "rainshowers_day" -> R.drawable.weather_partly_rainy
            "heavyrain", "heavyrainshowers", "heavyrainshowers_day", "heavyrainshowers_night"-> R.drawable.weather_pouring
            "sleet", "lightsleet", "heavysleet" -> R.drawable.weather_snowy_rainy
            "snow", "lightsnow" -> R.drawable.weather_snowy
            "heavysnow" -> R.drawable.weather_snowy_heavy
            else -> R.drawable.cloud_question
        }
    }

    fun getReadableCondition(condition: String?): String{
        if (condition != null){
            // Replace underscores with spaces
            return condition.replace("_", " ")
        }

        return "unknown"
    }

}
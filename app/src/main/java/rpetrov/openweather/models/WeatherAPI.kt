package rpetrov.openweather.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rpetrov.openweather.models.forecast.Forecast

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun cardWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Call<Weather?>?


    // https://api.openweathermap.org/data/2.5/forecast?lat=50.5&lon=50.5&appid=3f59ab8914d4991b70c19816f7f36892
    @GET("data/2.5/forecast")
    fun forecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Call<Forecast?>?

    companion object {
        private const val KEY = "3f59ab8914d4991b70c19816f7f36892"
    }
}
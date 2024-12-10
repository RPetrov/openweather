package rpetrov.openweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rpetrov.openweather.dto.WeatherForecast

// https://api.openweathermap.org/data/2.5/forecast?lat=30.5&lon=100.5&appid=3f59ab8914d4991b70c19816f7f36892&units=metric

interface ForecastService {

    @GET("data/2.5/forecast")
    fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") id: String = API_KEY,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Call<WeatherForecast>

    companion object {
        private const val API_KEY = "3f59ab8914d4991b70c19816f7f36892"
    }
}
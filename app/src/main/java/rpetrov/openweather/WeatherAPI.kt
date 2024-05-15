package rpetrov.openweather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("data/2.5/weather")
    fun cardWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String = "3f59ab8914d4991b70c19816f7f36892",
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Call<Weather?>?
}
package rpetrov.openweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rpetrov.openweather.dto.WeatherForecast

/**
 * - Запросить местоположение
 *
 * - Получить прогноз
 * - построить список
 *
 *
 * with, let, apply, also, run
 */
class MainActivity : AppCompatActivity() {

    private val retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val forecastService = retrofit.create(ForecastService::class.java)

    lateinit var locationProvider: FusedLocationProviderClient

    private val adapter = ForecastAdapter()

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.forecast_list)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        adapter.onItemClickListener = {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }

        locationProvider =
            LocationServices.getFusedLocationProviderClient(this)
        locationProvider.lastLocation.addOnSuccessListener {

            if (it == null) {
                Toast.makeText(this, "location is null", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            } else {
                Toast.makeText(this, "location is ${it}", Toast.LENGTH_LONG).show()
                forecastService.getForecast(it.latitude, it.longitude).enqueue(object : Callback<WeatherForecast?> {
                    override fun onResponse(p0: Call<WeatherForecast?>, response: Response<WeatherForecast?>) {
                        if (response.isSuccessful) {
                            response.body()?.list?.let {
                                adapter.data = it
                                adapter.notifyDataSetChanged()
                            } ?: run {
                                Log.w("MA", "list is null")
                            }

                            Log.w("MainActivity", response.body().toString())
                        } else {
                            Toast.makeText(this@MainActivity, "error", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(p0: Call<WeatherForecast?>, throwable: Throwable) {
                        Log.e("MainActivity", throwable.message, throwable)
                        Toast.makeText(this@MainActivity, "error: $throwable", Toast.LENGTH_LONG).show()
                    }
                })

            }
        }
    }
}
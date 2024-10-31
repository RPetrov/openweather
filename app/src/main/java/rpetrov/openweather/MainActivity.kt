package rpetrov.openweather

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.prominence.openweathermap.api.OpenWeatherMapClient
import com.github.prominence.openweathermap.api.enums.Language
import com.github.prominence.openweathermap.api.enums.UnitSystem
import com.github.prominence.openweathermap.api.model.Coordinate
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * - Запросить местоположение
 *
 * - Получить прогноз (библиотека)
 * - построить список
 */
class MainActivity : AppCompatActivity() {

    lateinit var locationProvider: FusedLocationProviderClient

    private val adapter = ForecastAdapter()

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.forecasts_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.onItemCliclListener = { weatherForecast ->
            Log.w("MA", weatherForecast.toString())
        }

        locationProvider =
            LocationServices.getFusedLocationProviderClient(this)
        locationProvider.lastLocation.addOnSuccessListener {
            if (it == null) {
                Toast.makeText(this, "location is null", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
            Toast.makeText(this, "location is ${it}", Toast.LENGTH_LONG).show()

            val openWeatherClient = OpenWeatherMapClient("3f59ab8914d4991b70c19816f7f36892")
            val weatherForecast = openWeatherClient
                .forecast5Day3HourStep()
                .byCoordinate(Coordinate.of(it.latitude, it.longitude))
                .language(Language.RUSSIAN)
                .unitSystem(UnitSystem.METRIC)
                .retrieveAsync()
                .asJava()
                .get()
            //  Log.w("MA", weatherForecast.toString())
            adapter.data = weatherForecast.weatherForecasts
            adapter.notifyDataSetChanged()
        }

    }
}
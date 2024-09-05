package rpetrov.openweather

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
 * - Получить текущую погоду и отобразить её
 * - Получить получить прогноз
 * - построить список
 */
class MainActivity : AppCompatActivity() {

    lateinit var locationProvider: FusedLocationProviderClient

    val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.forecast_list)
    }

    val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    val adapter = ForecastAdapter()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.isVisible = false
        progress.isVisible = true
        adapter.onItemClickListener = { weatherForecast ->
            Toast.makeText(this, weatherForecast.toString(), Toast.LENGTH_LONG).show()
        }

        adapter.onImageClickListener = { weatherForecast ->
            // todo
        }


        locationProvider =
            LocationServices.getFusedLocationProviderClient(this)
        locationProvider.lastLocation.addOnSuccessListener {
            val openWeatherClient = OpenWeatherMapClient("3f59ab8914d4991b70c19816f7f36892")

            val forecast = openWeatherClient
                .forecast5Day3HourStep()
                .byCoordinate(Coordinate.of(it.latitude, it.longitude))
                .language(Language.RUSSIAN)
                .unitSystem(UnitSystem.METRIC)
                .retrieveAsync()
                .asJava()
                .get()

            adapter.data = forecast.weatherForecasts
            adapter.notifyDataSetChanged()
            recyclerView.isVisible = true
            progress.isVisible = false

        }
    }
}
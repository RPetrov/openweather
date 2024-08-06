package rpetrov.openweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
 *
 * - Получить прогноз (библиотека)
 * - построить список
 */
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val adapter = ForecastAdapter(emptyList())

    @SuppressLint("MissingPermission")
    val registerForResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val location = it
                    val lat = location.latitude
                    val lon = location.longitude
                    getAndShowForecast(lat, lon)
                }
            } else {
                Toast.makeText(this, "Разрешено не выдано", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Разрешено не выдано", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progress_bar)
        recyclerView = findViewById(R.id.weather_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        registerForResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun getAndShowForecast(lat: Double, lon: Double) {
        val openWeatherClient = OpenWeatherMapClient("USE_KEY_FROM OpenWeatherMap")
        val weather = openWeatherClient
            .forecast5Day3HourStep()
            .byCoordinate(Coordinate.of(lat, lon))
            .language(Language.RUSSIAN)
            .unitSystem(UnitSystem.METRIC)
            .retrieveAsync()
            .asJava()
            .get()
        Log.i("WEATHER", weather.toString())
        adapter.listWeatherForecasts = weather.weatherForecasts
        adapter.notifyDataSetChanged()
        recyclerView.isVisible = true
        progressBar.isVisible = false
    }

}
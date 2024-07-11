package rpetrov.openweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
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

    val getsh = getSharedPreferences("feef", MODE_PRIVATE)

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerview)
    }

    private val progress by lazy {
        findViewById<ProgressBar>(R.id.progress)
    }

    @SuppressLint("MissingPermission")
    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            setUpLocationProviderAndMakeRequest()
        } else {
            Toast.makeText(this, "Разрешено не выдано", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            setUpLocationProviderAndMakeRequest()
        } else {
            launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    fun setUpLocationProviderAndMakeRequest() {
        locationProvider =
            LocationServices.getFusedLocationProviderClient(this)
        locationProvider.lastLocation.addOnSuccessListener {
            if (it == null) {
                Toast.makeText(this, "location is null", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }

            val client = OpenWeatherMapClient("3f59ab8914d4991b70c19816f7f36892")
            val forecast = client
                .forecast5Day3HourStep()
                .byCoordinate(Coordinate.of(it.latitude, it.longitude))
                .language(Language.RUSSIAN)
                .unitSystem(UnitSystem.METRIC)
                //.count(15)
                .retrieveAsync()
                .asJava()
                .get()


            recyclerView.isVisible = true
            progress.isGone = true
            recyclerView.adapter = WeatherAdapter(forecast.weatherForecasts)
        }
    }
}
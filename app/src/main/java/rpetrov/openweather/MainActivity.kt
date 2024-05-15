package rpetrov.openweather

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * - Запросить местоположение
 * - Получить текущую погоду и отобразить её
 * - Получить получить прогноз
 * - построить список
 */
class MainActivity : AppCompatActivity() {

    var retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addConverterFactory(
            GsonConverterFactory
                .create()
        )
        .build()

    val weatherApi = retrofit.create(WeatherAPI::class.java)

    private lateinit var locationProvider: FusedLocationProviderClient

    val registerForActivityResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { hasPermission ->

        if (!hasPermission) {
            Toast.makeText(this@MainActivity, "No permission", Toast.LENGTH_SHORT).show()
        } else {
            getAndShowCurrentWeather()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationProvider = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            registerForActivityResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        } else {
            getAndShowCurrentWeather()
        }
    }

    fun getAndShowCurrentWeather() {
        locationProvider.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                weatherApi.cardWeather(location.latitude, location.longitude)?.enqueue(object : Callback<Weather?> {
                        override fun onResponse(p0: Call<Weather?>, p1: Response<Weather?>) {
                            val temp = findViewById<TextView>(R.id.temp)
                            val hidro = findViewById<TextView>(R.id.hidro)
                            val description = findViewById<TextView>(R.id.description)

                            temp.text = p1.body()?.main?.temp.toString()
                            hidro.text = p1.body()?.main?.humidity.toString()
                            description.text = p1.body()?.weather?.firstOrNull()?.description
                        }

                        override fun onFailure(p0: Call<Weather?>, p1: Throwable) {
                            Log.e("main_activity", p1.message, p1)
                        }
                    })
            }
        }.addOnFailureListener {
            Toast.makeText(this@MainActivity, "error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
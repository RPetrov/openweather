package rpetrov.openweather

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rpetrov.openweather.models.curentwheather.CurrentWheather

/**
 * - Запросить местоположение
 * - Получить текущую погоду и отобразить её
 * - Получить получить прогноз
 * - построить список
 */
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherServise = retrofit.create(WeatherApi::class.java)
    var tempTextView: TextView? = null
    var humidityTextView: TextView? = null
    var discriptionTextView: TextView? = null


    val registerForResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    val location = it
                    val lat = location.latitude
                    val lon = location.longitude
                    weatherServise.getCurrentWeather(lat, lon)
                        .enqueue(object : Callback<CurrentWheather> {
                            override fun onResponse(
                                p0: Call<CurrentWheather>,
                                p1: Response<CurrentWheather>
                            ) {
                                if (p1.isSuccessful) {
                                    val temperature = p1.body()?.main?.temp
                                    tempTextView?.text = temperature.toString()
                                }
                            }

                            override fun onFailure(p0: Call<CurrentWheather>, p1: Throwable) {
                                Log.d(TAG, "$p1", p1)
                            }
                        })
                }
            }

        } else {
            Toast.makeText(this, "Разрешения НЕ выдано", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerForResult.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

    private fun initViews() {
        tempTextView = findViewById(R.id.temp)
        humidityTextView = findViewById(R.id.hum)
        discriptionTextView = findViewById(R.id.desc)
    }

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        const val TAG = "MainActivity"
    }
}
package rpetrov.openweather

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * - Запросить местоположение
 * - Получить текущую погоду и отобразить её
 * - Получить получить прогноз
 * - построить список
 */
class MainActivity : AppCompatActivity() {

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
            Toast.makeText(this@MainActivity, "location: ${location}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this@MainActivity, "error ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
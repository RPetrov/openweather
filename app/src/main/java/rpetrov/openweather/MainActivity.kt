package rpetrov.openweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * - Запросить местоположение
 * - Получить текущую погоду и отобразить её
 * - Получить получить прогноз
 * - построить список
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
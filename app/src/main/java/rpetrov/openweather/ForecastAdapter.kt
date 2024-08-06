package rpetrov.openweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast
import java.time.format.DateTimeFormatter

sealed class WeatherItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val time = view.findViewById<TextView>(R.id.time)

    open fun bind(weatherForecast: WeatherForecast) {
        time.text = weatherForecast.forecastTime.format(DateTimeFormatter.ISO_DATE_TIME)
    }

    class SmallViewHolder(view: View) : WeatherItemViewHolder(view) {

        private val desc = view.findViewById<TextView>(R.id.desc)

        override fun bind(weatherForecast: WeatherForecast) {
            super.bind(weatherForecast)
            desc.text =
                "T: ${weatherForecast.temperature.value}, В: ${weatherForecast.humidity.value}"
        }
    }

    class BigViewHolder(view: View) : WeatherItemViewHolder(view) {

        private val image = view.findViewById<ImageView>(R.id.image)

        override fun bind(weatherForecast: WeatherForecast) {
            super.bind(weatherForecast)
            image.setImageResource(R.drawable.sunny_day_16458)
        }
    }
}

class ForecastAdapter(var listWeatherForecasts: List<WeatherForecast>) :
    RecyclerView.Adapter<WeatherItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        return if (viewType == BIG_ITEM) {
            WeatherItemViewHolder.BigViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.big_item, parent, false)
            )
        } else {
            WeatherItemViewHolder.SmallViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.small_item, parent, false)
            )

        }
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(listWeatherForecasts[position])
    }

    override fun getItemCount(): Int = listWeatherForecasts.size

    override fun getItemViewType(position: Int): Int {
        val weatherForecast = listWeatherForecasts[position]
        return if (weatherForecast.forecastTime.hour == 15) {
            BIG_ITEM
        } else {
            SMALL_ITEM
        }
    }

    companion object {
        private const val BIG_ITEM = 0
        private const val SMALL_ITEM = 1
    }
}
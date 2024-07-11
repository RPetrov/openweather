package rpetrov.openweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast


abstract class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(forecast: WeatherForecast)
}

class SmallItemViewHolder(parent: ViewGroup) : WeatherViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.small_item, parent, false)
) {

    private val time = itemView.findViewById<TextView>(R.id.time)
    private val desc = itemView.findViewById<TextView>(R.id.desc)
    private val temp = itemView.findViewById<TextView>(R.id.temp)

    override fun bind(forecast: WeatherForecast) {
        time.text = forecast.forecastTime.toString()
        desc.text = forecast.weatherState.description
        temp.text = forecast.temperature.maxTemperature.toString()
    }
}

class BigItemViewHolder(parent: ViewGroup) : WeatherViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.big_layout, parent, false)
) {

    private val time = itemView.findViewById<TextView>(R.id.time)
    private val image = itemView.findViewById<ImageView>(R.id.image)

    override fun bind(forecast: WeatherForecast) {

        time.text = forecast.forecastTime.toString()
        image.setImageResource(R.drawable.sunny_day_16458)
    }
}

class WeatherAdapter(val data: List<WeatherForecast>) : RecyclerView.Adapter<WeatherViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return if (viewType == BIG_TYPE) {
            BigItemViewHolder(parent)
        } else {
            SmallItemViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].forecastTime.hour == 15) {
            BIG_TYPE
        } else {
            SMALL_TYPE
        }
    }

    override fun getItemCount(): Int = data.size

    companion object {
        private const val SMALL_TYPE = 0
        private const val BIG_TYPE = 1
    }
}
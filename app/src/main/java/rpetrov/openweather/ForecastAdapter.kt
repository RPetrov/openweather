package rpetrov.openweather

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView.OnCloseListener
import androidx.recyclerview.widget.RecyclerView
import com.github.prominence.openweathermap.api.enums.WeatherCondition
import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast
import java.time.format.DateTimeFormatter

abstract class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(forecast: WeatherForecast)
}

class SmallForecastViewHolder(parent: ViewGroup) :
    ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.small_forecast_item, parent, false)) {

    private val datetimeTextView = itemView.findViewById<TextView>(R.id.datetime)
    private val temp = itemView.findViewById<TextView>(R.id.temp)

    override fun bind(forecast: WeatherForecast) {

        datetimeTextView.text =
            forecast.forecastTime.format(DateTimeFormatter.ofPattern("dd.MM hh:mm"))
        temp.text = forecast.temperature.value.toString()
    }
}

class BigForecastViewHolder(parent: ViewGroup) :
    ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.big_forecast_item, parent, false)) {

    private val image = itemView.findViewById<ImageView>(R.id.image_view)
    private val temp = itemView.findViewById<TextView>(R.id.temp)

    override fun bind(forecast: WeatherForecast) {
        val imageRes = when (forecast.weatherState.weatherConditionEnum) {
            WeatherCondition.SNOW -> R.drawable.snowy_weather_16472
            WeatherCondition.CLEAR -> R.drawable.sunny_day_16458
            else -> R.drawable.sunny_day_16458
        }
        image.setImageResource(imageRes)
        temp.text = forecast.temperature.value.toString()
    }
}


class ForecastAdapter : RecyclerView.Adapter<ForecastViewHolder>() {
    var data: List<WeatherForecast> = emptyList()

    var onItemCliclListener: ((WeatherForecast) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return when (viewType) {
            SMALL_ITEM -> SmallForecastViewHolder(parent)
            BIG_ITEM -> BigForecastViewHolder(parent)
            else -> throw IllegalArgumentException("неизвестный viewType")
        }
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = data[position]
        holder.bind(forecast)
        onItemCliclListener?.let {
            holder.itemView.setOnClickListener {
                onItemCliclListener?.invoke(data[position])
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].forecastTime.hour != 15) SMALL_ITEM else BIG_ITEM
    }

    override fun getItemCount(): Int = data.size

    companion object {
        private const val SMALL_ITEM = 1
        private const val BIG_ITEM = 2
    }
}
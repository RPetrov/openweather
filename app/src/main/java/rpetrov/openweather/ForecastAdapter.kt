package rpetrov.openweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.prominence.openweathermap.api.enums.WeatherCondition
import com.github.prominence.openweathermap.api.model.forecast.WeatherForecast

abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: WeatherForecast)
}

class SmallItemViewHolder(parent: ViewGroup) :
    ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.small_item, parent, false)) {

    val time = itemView.findViewById<TextView>(R.id.time)
    val temp = itemView.findViewById<TextView>(R.id.temp)

    override fun bind(item: WeatherForecast) {
        time.text = item.forecastTime.toString()
        temp.text = item.temperature.value.toString()
    }
}

class BigItemViewHolder(parent: ViewGroup) : ItemViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.big_item, parent, false)
) {
    var onImageClickListener: ((WeatherForecast) -> Unit)? = null


    val time = itemView.findViewById<TextView>(R.id.time)
    val image = itemView.findViewById<ImageView>(R.id.image)

    override fun bind(item: WeatherForecast) {
        time.text = item.forecastTime.toString()
        val imageResource = when (item.weatherState.weatherConditionEnum) {
            WeatherCondition.CLEAR -> R.drawable.sunny_day_16458
            WeatherCondition.SNOW -> R.drawable.snowy_weather_16472
            else -> R.drawable.snowy_weather_16472
        }
        image.setImageResource(imageResource)
        image.setOnClickListener {
            onImageClickListener?.invoke(item)
        }
    }

}

class ForecastAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    var data: List<WeatherForecast> = emptyList()

    var onImageClickListener: ((WeatherForecast) -> Unit)? = null

    var onItemClickListener: ((WeatherForecast) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if (viewType == SMALL_ITEM_VIEW_TYPE) {
            return SmallItemViewHolder(parent)
        } else {
            return BigItemViewHolder(parent).also {
                it.onImageClickListener = onImageClickListener
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].forecastTime.hour == 15) {
            BIG_ITEM_VIEW_TYPE
        } else {
            SMALL_ITEM_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int = data.size

    companion object {
        private const val SMALL_ITEM_VIEW_TYPE = 5
        private const val BIG_ITEM_VIEW_TYPE = 10
    }
}
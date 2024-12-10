package rpetrov.openweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentViewHolder
import rpetrov.openweather.dto.ForecastItem

class ForecastHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)) {

    val time: TextView = itemView.findViewById(R.id.time)
    val temp: TextView = itemView.findViewById(R.id.temp)


    fun bind(item: ForecastItem) {
        time.text = item.dtTxt
        if ((item.main?.temp ?: 0.0) >= 0) {
            temp.text = item.main?.temp.toString()
        } else {
            temp.text = ""
        }
    }
}

class ForecastAdapter : RecyclerView.Adapter<ForecastHolder>() {

    var data: List<ForecastItem> = emptyList()

    var onItemClickListener: ((ForecastItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder = ForecastHolder(parent)

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        holder.bind(data[position])
        onItemClickListener?.let { onItemClickListener ->
            holder.itemView.setOnClickListener {
                onItemClickListener.invoke(data[position])
            }
        }
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
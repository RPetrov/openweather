package rpetrov.openweather.dto


import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int?
)
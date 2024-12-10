package rpetrov.openweather.dto


import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String?
)
import com.google.gson.annotations.SerializedName

data class DoctorUsers(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("role")
    val role: String,

    @SerializedName("Fname")
    val Fname: String,

    @SerializedName("Lname")
    val Lname: String,

    @SerializedName("specialty")
    val specialty: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("hn")
    val hn: String,

    @SerializedName("barangay")
    val barangay: String,

    @SerializedName("municipality")
    val municipality: String,

    @SerializedName("province")
    val province: String,

    @SerializedName("consultPrice")
    val consultPrice: String,

    @SerializedName("status")
    val status: String
)

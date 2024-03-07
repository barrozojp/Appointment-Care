import com.codeofduty.appointcare.models.ImageData
import com.google.gson.annotations.SerializedName

data class DoctorUsers(
    @SerializedName("_id")
    val _id: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("Fname")
    val Fname: String? = null,

    @SerializedName("Lname")
    val Lname: String? = null,

    @SerializedName("specialty")
    val specialty: String? = null,

    @SerializedName("md")
    val md: String? = null,

    @SerializedName("number")
    val number: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("hn")
    val hn: String? = null,

    @SerializedName("barangay")
    val barangay: String? = null,

    @SerializedName("municipality")
    val municipality: String? = null,

    @SerializedName("province")
    val province: String? = null,

    @SerializedName("consultPrice")
    val consultPrice: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("f2f")
    val f2f: Boolean? = null,

    @SerializedName("online")
    val online: Boolean? = null,

    @SerializedName("imageData")
    val imageData: String? = null
)

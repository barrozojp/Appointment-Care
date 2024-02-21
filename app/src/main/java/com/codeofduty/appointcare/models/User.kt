import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("role")
    val role: String,

    @SerializedName("Fname")
    val Fname: String,

    @SerializedName("Lname")
    val Lname: String,

    @SerializedName("number")
    val number: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("imageData")
    val imageData: String? = null,

    @SerializedName("hn")
    val hn: Int? = null,

    @SerializedName("barangay")
    val barangay: String? = null,

    @SerializedName("municipality")
    val municipality: String? = null,

    @SerializedName("province")
    val province: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("specialty")
    val specialty: String? = null,

    @SerializedName("md")
    val md: Int? = null,

    @SerializedName("consultPrice")
    val consultPrice: Int? = null,

    @SerializedName("f2f")
    val f2f: Boolean? = null,

    @SerializedName("online")
    val online: Boolean? = null,

    @SerializedName("__v")
    val version: Int? = null,
)

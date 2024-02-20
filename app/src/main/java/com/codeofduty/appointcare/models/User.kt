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

    @SerializedName("__v")
    val version: Int? = null,

    @SerializedName("imageData")
    val imageData: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("specialty")
    val specialty: String? = null,

    @SerializedName("md")
    val md: String? = null,

    @SerializedName("consultPrice")
    val consultPrice: Int? = null,

    @SerializedName("f2f")
    val f2f: Boolean? = null,

    @SerializedName("online")
    val online: Boolean? = null
)

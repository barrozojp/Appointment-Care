import com.google.gson.annotations.SerializedName

data class patientRegister(

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

    @SerializedName("consultation")
    val consultation: String,

    @SerializedName("__v")
    val version: Int? = null,
)

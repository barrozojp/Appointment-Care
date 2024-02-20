
import org.json.JSONException
import org.json.JSONObject

object JwtUtils {

    fun decodeJWT(token: String): Map<String, Any>? {
        val parts = token.split('.')
        if (parts.size != 3) {
            return null
        }

        val decodedPayload = android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT)
        val payloadString = String(decodedPayload, charset("UTF-8"))

        return try {
            val payloadJson = JSONObject(payloadString)
            payloadJson.toMap()
        } catch (e: JSONException) {
            null
        }
    }

    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = this.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = this.get(key)
            map[key] = value
        }
        return map
    }
}

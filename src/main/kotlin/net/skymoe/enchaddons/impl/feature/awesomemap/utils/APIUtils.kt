package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import com.google.gson.JsonParser
import net.skymoe.enchaddons.util.scope.noexcept
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import java.util.UUID

object APIUtils {
    fun fetch(uri: String): String? {
        HttpClients.createMinimal().use {
            return noexcept {
                val httpGet = HttpGet(uri)
                return EntityUtils.toString(it.execute(httpGet).entity)
            }
        }
    }

    fun getSecrets(uuid: UUID): Int {
        repeat(3) {
            val result = fetch("https://api.tenios.dev/secrets/$uuid")?.toIntOrNull()
            if (result != null) return result
        }

        return 0
    }

    fun hasBonusPaulScore(): Boolean {
        val response = fetch("https://api.hypixel.net/resources/skyblock/election") ?: return false
        val jsonObject = JsonParser().parse(response).toJsonObject() ?: return false
        if (jsonObject.getJsonPrimitive("success")?.asBoolean == true) {
            val mayor = jsonObject.getJsonObject("mayor") ?: return false
            val name = mayor.getJsonPrimitive("name")?.asString
            if (name == "Paul") {
                return mayor.getJsonArray("perks")?.any {
                    it.toJsonObject()?.getJsonPrimitive("name")?.asString == "EZPZ"
                } ?: false
            }
        }
        return false
    }
}

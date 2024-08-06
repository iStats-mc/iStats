package net.radstevee.istats

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import net.radstevee.istats.util.UID
import java.util.UUID

@Serializable
data class Player(
    val uuid: UID,
    val username: String?,
    val ranks: List<Rank>,
    val crownLevel: CrownLevel,
    val status: Status?,
    val collections: Collections?,
    val social: Social?,
)

@Serializable
enum class Rank {
    CHAMP,
    GRAND_CHAMP,
    GRAND_CHAMP_ROYALE,
    CREATOR,
    CONTESTANT,
    MODERATOR,
    NOXCREW,
}

@Serializable
data class CrownLevel(
    val level: Int,
    val nextEvolutionLevel: Int?,
    val nextLevelProgress: ProgressionData?,
    val trophies: TrophyData,
)

@Serializable
data class ProgressionData(
    val obtained: Int,
    val obtainable: Int,
)

@Serializable
data class TrophyData(
    val obtained: Int,
    val obtainable: Int,
    val bonus: Int,
)

@Serializable
data class Status(
    val online: Boolean,
    val server: Server?,
    val firstJoin: Instant?,
    val lastJoin: Instant?,
)

@Serializable
data class Server(
    val category: ServerCategory,
    val subType: String,
    val associatedGame: Game?,
)

@Serializable
enum class ServerCategory {
    LOBBY,
    GAME,
    LIMBO,
    QUEUE,
}

@Serializable
enum class Game {
    HOLE_IN_THE_WALL,
    TGTTOS,
    BATTLE_BOX,
    SKY_BATTLE,
    PARKOUR_WARRIOR,
    DYNABALL,
    ROCKET_SPLEEF,
}

@Serializable
data class Collections(
    val currency: Currency,
)

@Serializable
data class Currency(
    val coins: Int,
    val gems: Int,
    val royalReputation: Int,
    val silver: Int,
    val materialDust: Int,
)

@Serializable
data class Social(
    val friends: List<MinimalPlayer>,
    val party: Party,
)

@Serializable
data class Party(
    val active: Boolean,
    val leader: MinimalPlayer?,
    val members: List<MinimalPlayer>?,
)

@Serializable
data class MinimalPlayer(
    val uuid: UID,
    val username: String?,
)

private val client =
    HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

private const val API_BASE_URL = "http://localhost:8080/player"

suspend fun getPlayer(player: UUID) = getPlayer(player.toString())

suspend fun getPlayer(player: String): Player? {
    val response = client.get("$API_BASE_URL/$player")
    if (response.status != HttpStatusCode.OK) return null

    return response.body<Player>()
}

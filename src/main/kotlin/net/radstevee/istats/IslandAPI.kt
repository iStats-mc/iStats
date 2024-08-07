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
import net.minecraft.util.Identifier
import net.radstevee.istats.util.UID
import java.util.UUID

@Serializable
data class IslandPlayer(
    val uuid: UID,
    val username: String?,
    val ranks: List<Rank>,
    val crownLevel: CrownLevel,
    val status: Status?,
    val collections: Collections?,
    val social: Social?,
) {
    val primaryRank = ranks.maxByOrNull { it.ordinal } ?: Rank.DEFAULT
}

@Serializable
enum class Rank {
    DEFAULT,
    CHAMP,
    GRAND_CHAMP,
    GRAND_CHAMP_ROYALE,
    CREATOR,
    CONTESTANT,
    MODERATOR,
    NOXCREW,
    ;

    val texture = Identifier.of("istats", "textures/rank/${name.lowercase()}.png")
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
enum class Game(
    val displayName: String,
) {
    HOLE_IN_THE_WALL("Hole in the Wall"),
    TGTTOS("TGTTOS"),
    BATTLE_BOX("Battle Box"),
    SKY_BATTLE("Sky Battle"),
    PARKOUR_WARRIOR("Parkour Warrior"),
    DYNABALL("Dynaball"),
    ROCKET_SPLEEF("Rocket Spleef Rush"),
    ;

    val texture = Identifier.of("istats", "textures/game/${name.lowercase()}.png")
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

suspend fun getPlayer(player: String): Result<IslandPlayer> {
    val response =
        runCatching { client.get("$API_BASE_URL/$player") }
            .onFailure { exception ->
                return Result.failure(exception)
            }.getOrThrow()
    if (response.status != HttpStatusCode.OK) return Result.failure(IllegalArgumentException("Invalid player"))

    return runCatching { response.body<IslandPlayer>() }
}

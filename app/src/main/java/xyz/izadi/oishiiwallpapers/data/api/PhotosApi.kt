package xyz.izadi.oishiiwallpapers.data.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

data class UnsplashQueryOptions(
    var query: String = "food",
    var orderBy: UnsplashOrderBy = UnsplashOrderBy.RELEVANT,
    var color: UnsplashColor? = UnsplashColor.ANY,
    var orientation: UnsplashOrientation? = UnsplashOrientation.ANY
)

enum class UnsplashOrderBy(val value: String) {
    RELEVANT("relevant"),
    LATEST("latest")
}

enum class UnsplashColor(val value: String?) {
    ANY(null),
    BLACKANDWHITE("black_and_white"),
    BLACK("black"),
    WHITE("white"),
    YELLOW("yellow"),
    ORANGE("orange"),
    RED("red"),
    PURPLE("purple"),
    MAGENTA("magenta"),
    GREEN("green"),
    TEAL("teal"),
    BLUE("blue")
}

enum class UnsplashOrientation(val value: String?) {
    ANY(null),
    PORTRAIT("portrait"),
    LANDSCAPE("landscape"),
    SQUARISH("squarish")
}

@Parcelize
data class UnsplashUser(
    val id: String,
    val name: String?,
    val instagram_username: String?,
    val twitter_username: String?
): Parcelable

@Parcelize
data class UnsplashUrls(
    val raw: String,
    val regular: String,
    val small: String
): Parcelable

@Parcelize
data class UnsplashLinks(
    val self: String,
    val download: String
) : Parcelable

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val description: String?,
    val width: Int,
    val height: Int,
    val user: UnsplashUser,
    val urls: UnsplashUrls,
    val links: UnsplashLinks
) : Parcelable

// Data Model for the Response
data class UnsplashResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashPhoto>
)

//A retrofit Network Interface for the Api
interface UnsplashApi {
    @GET("/search/photos")
    suspend fun getPics(
        @Query("query") searchQuery: String = "food",
        @Query("page") currentPage: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("color") color: String?
    ): UnsplashResponse
}
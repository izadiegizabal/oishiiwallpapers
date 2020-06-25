package xyz.izadi.oishiiwallpapers.data.api

import retrofit2.http.GET
import retrofit2.http.Query

data class UnsplashQueryOptions(
    var query: String = "food",
    var orderBy: UnsplashOrderBy = UnsplashOrderBy.RELEVANT,
    var color: UnsplashColor = UnsplashColor.ANY,
    var orientation: UnsplashOrientation = UnsplashOrientation.ANY
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

data class UnsplashUser(
    val id: String,
    val name: String,
    val instagram_username: String,
    val twitter_username: String
)

data class UnsplashUrls(
    val raw: String,
    val regular: String,
    val small: String
)

data class UnsplashLinks(
    val self: String,
    val download: String
)

data class UnsplashPhoto(
    val id: String,
    val description: String,
    val overview: String,
    val user: UnsplashUser,
    val urls: UnsplashUrls,
    val links: UnsplashLinks
)

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
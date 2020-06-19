package com.bambulabs.fdnetwork

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface FDNetworkAPI {

    @POST("devices")
    fun authentication(@Body authenticationRequest: String): Observable<String>

    @GET("shops/{venueId}")
    fun getShop(@Path("venueId") venueId: String, @Query("select") select: String, @Query("populate") populate: String): Observable<String>

    @GET("tables")
    fun getTables(@Query("limit") limit: String): Observable<String>

    @GET("tickets?sort=-updated_at")
    fun getTickets(
        @Query("where") where: String, @Query("offset") offset: String, @Query("limit") limit: String, @Query(
            "populate"
        ) populate: String
    ): Observable<String>

    @POST("devices/{deviceID}/assign-table")
    fun assignDevice(@Path("deviceID") deviceID: String, @Body tableTopRequest: String): Observable<String>

    @GET("tickets")
    fun findOpenTable(@Query("where") where: String, @Query("limit") limit: String): Observable<String>

    @POST("shops/{shopID}/ticket/create")
    fun createTicket(@Path("shopID") shopID: String, @Body createTicket: RequestBody): Observable<String>

    @POST("shops/{shopID}/ticket/add")
    fun addTicket(@Path("shopID") shopID: String, @Body createTicket: String): Observable<String>

    @GET("employees")
    fun getEmployees(@Query("limit") limit: String): Observable<String>

    @GET("tickets/{ticketID}/calculate")
    fun calculateCheckout(@Path("ticketID") ticketID: String): Observable<String>

    @GET("modifiergroups")
    fun getModifierGroups(@Query("limit") limit: String): Observable<String>

    @GET("ads")
    fun getAds(): Observable<String>

    @GET("feedbackforms")
    fun getFeedbackForms(): Observable<String>

    @POST("feedbacks")
    suspend fun sendFeedbackForm(@Body feedbackFormRequest: String): Observable<String>

    @POST("feedbacks/bulk")
    suspend fun sendFeedbackFormOffline(@Body feedbackFormRequest: String): Response<String>

    @GET("entities")
    fun getMenus(@Query("where") where: String, @Query("limit") limit: String): Observable<String>

    @GET("entities/{menuID}/flat-list")
    suspend fun getMenuFlatList(@Path("menuID") menuID: String): Response<String>

    @GET("entities")
    suspend fun getEntitiesWithResponse(
        @Query("where") where: String, @Query("fill") fill: String, @Query(
            "offset"
        ) offset: String, @Query("limit") limit: String
    ): Response<String>

}
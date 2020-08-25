package com.bambulabs.fdnetwork

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface FDNetworkAPI {

    @POST("devices")
    suspend fun authentication(@Body authenticationRequest: RequestBody): Response<String>

    @GET("shops/{venueId}")
    suspend fun getShop(@Path("venueId") venueId: String, @Query("select") select: String, @Query("populate") populate: String): Response<String>

    @GET("tables")
    suspend fun getTables(@Query("limit") limit: String): Response<String>

    @GET("ads")
    suspend fun getAds(): Response<String>

    @GET("employees")
    suspend fun getEmployees(@Query("limit") limit: String): Response<String>

    @GET("feedbackforms")
    suspend fun getFeedbackForms(): Response<String>

    @GET("modifiergroups")
    suspend fun getModifierGroups(@Query("limit") limit: String): Response<String>

    @GET("entities")
    suspend fun getMenus(@Query("where") where: String, @Query("limit") limit: String): Response<String>

    @GET("tickets?sort=-updated_at")
    suspend fun getTickets(
        @Query("where") where: String, @Query("offset") offset: String, @Query("limit") limit: String, @Query(
            "populate"
        ) populate: String
    ): Response<String>

    @POST("shops/{shopID}/ticket/create")
    suspend fun createTicket(@Path("shopID") shopID: String, @Body createTicket: RequestBody): Response<String>

    @POST("devices/{deviceID}/assign-table")
    suspend fun assignDevice(@Path("deviceID") deviceID: String, @Body tableTopRequest: RequestBody): Response<String>

    @GET("tickets")
    suspend fun findOpenTable(@Query("where") where: String, @Query("limit") limit: String): Response<String>

    @POST("shops/{shopID}/ticket/add")
    suspend fun addTicket(@Path("shopID") shopID: String, @Body createTicket: String): Response<String>

    @GET("tickets/{ticketID}/calculate")
    suspend fun calculateCheckout(@Path("ticketID") ticketID: String): Response<String>

    @POST("tickets/preview")
    suspend fun calculateCheckoutForKiosk(@Body request: RequestBody): Response<String>

    @POST("feedbacks")
    suspend fun sendFeedbackForm(@Body feedbackFormRequest: RequestBody): Response<String>

    @POST("feedbacks/bulk")
    suspend fun sendFeedbackFormOffline(@Body feedbackFormRequest: RequestBody): Response<String>

    @GET("entities/{menuID}/flat-list")
    suspend fun getMenuFlatList(@Path("menuID") menuID: String): Response<String>

    @GET("entities")
    suspend fun getEntitiesWithResponse(
        @Query("where") where: String, @Query("fill") fill: String, @Query(
            "offset"
        ) offset: String, @Query("limit") limit: String
    ): Response<String>

    @GET("devices/{deviceID}/version")
    suspend fun getDeviceVersion(@Path("deviceID") deviceID: String): Response<String>

    @GET("shops/{shopID}/status")
    suspend fun getShopStatus(@Path("shopID") shopID: String): Response<String>

}
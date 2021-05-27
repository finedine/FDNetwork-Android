package com.bambulabs.fdnetwork

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface FDNetworkAPI {

    /*@POST("devices")
    fun authentication(@Body authenticationRequest: String): Observable<String>

    @GET("shops/{venueId}")
    fun getShop(@Path("venueId") venueId: String, @Query("select") select: String, @Query("populate") populate: String): Observable<String>

    @GET("tables")
    fun getTables(@Query("limit") limit: String): Observable<String>

    @GET("ads")
    fun getAds(): Observable<String>

    @GET("employees")
    fun getEmployees(@Query("limit") limit: String): Observable<String>

    @GET("feedbackforms")
    fun getFeedbackForms(): Observable<String>

    @GET("modifiergroups")
    fun getModifierGroups(@Query("limit") limit: String): Observable<String>

    @GET("entities")
    fun getMenus(@Query("where") where: String, @Query("limit") limit: String): Observable<String> */

    @POST("v1/devices")
    suspend fun authentication(@Body authenticationRequest: RequestBody): Response<String>

    @GET("v1/shops/{venueId}")
    suspend fun getShop(@Path("venueId") venueId: String, @Query("select") select: String, @Query("populate") populate: String): Response<String>

    @GET("v1/tables")
    suspend fun getTables(@Query("limit") limit: String): Response<String>

    @GET("v1/ads")
    suspend fun getAds(): Response<String>

    @GET("v1/employees")
    suspend fun getEmployees(@Query("limit") limit: String): Response<String>

    @GET("v1/feedbackforms")
    suspend fun getFeedbackForms(): Response<String>

    @GET("v1/modifiergroups")
    suspend fun getModifierGroups(@Query("limit") limit: String): Response<String>

    @GET("v1/entities")
    suspend fun getMenus(@Query("where") where: String, @Query("limit") limit: String): Response<String>

    @GET("v1/tickets?sort=-updated_at")
    suspend fun getTickets(
        @Query("where") where: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String,
        @Query("populate") populate: String
    ): Response<String>

    @GET("v2/orders?sort=-updated_at")
    suspend fun getOrders(
        @Query("where") where: String,
        @Query("offset") offset: String,
        @Query("limit") limit: String
    ): Response<String>

    @GET("v2/orders/{orderID}")
    suspend fun getAnOrder(
        @Path("orderID") orderID: String,
        @Query("populate") populate: String
    ): Response<String>

    @GET("v2/order-items/{orderItemID}")
    suspend fun getAnOrderItem(
        @Path("orderItemID") orderID: String,
        @Query("populate") populate: String
    ): Response<String>

    @POST("v2/orders/calculate")
    suspend fun calculateAnOrder(@Body payload: RequestBody): Response<String>

    @POST("v2/orders/create")
    suspend fun createAnOrder(@Body payload: RequestBody): Response<String>

    @POST("v2/orders/add")
    suspend fun createAndAddOrder(@Body payload: RequestBody,
                                  @Query("populate") populate: String): Response<String>

    @POST("v2/orders/payment/{method}/init")
    suspend fun paymentRequest(@Path("method") method: String, @Body payload: RequestBody): Response<String>

    @POST("v1/shops/{shopID}/ticket/create")
    suspend fun createTicket(@Path("shopID") shopID: String, @Body createTicket: RequestBody): Response<String>

    @POST("v1/devices/{deviceID}/assign-table")
    suspend fun assignDevice(@Path("deviceID") deviceID: String, @Body tableTopRequest: RequestBody): Response<String>

    @GET("v1/tickets")
    suspend fun findOpenTable(@Query("where") where: String, @Query("limit") limit: String): Response<String>

    @POST("v1/shops/{shopID}/ticket/add")
    suspend fun addTicket(@Path("shopID") shopID: String, @Body createTicket: RequestBody): Response<String>

    @POST("v1/shops/{shopID}/ticket/createAndAdd")
    suspend fun createAndAdd(@Path("shopID") shopID: String, @Body createTicket: RequestBody): Response<String>

    @POST("v1/tickets/kiosk")
    suspend fun sendOrderForKiosk(@Body createTicket: RequestBody): Response<String>

    @GET("v1/tickets/{ticketID}/calculate")
    suspend fun calculateCheckout(@Path("ticketID") ticketID: String): Response<String>

    @POST("v1/tickets/preview")
    suspend fun calculateCheckoutForKiosk(@Body request: RequestBody): Response<String>

    @POST("v1/feedbacks")
    suspend fun sendFeedbackForm(@Body feedbackFormRequest: RequestBody): Response<String>

    @POST("v1/feedbacks/bulk")
    suspend fun sendFeedbackFormOffline(@Body feedbackFormRequest: RequestBody): Response<String>

    @GET("v1/entities/{menuID}/flat-list")
    suspend fun getMenuFlatList(@Path("menuID") menuID: String): Response<String>

    @GET("v1/entities")
    suspend fun getEntitiesWithResponse(
        @Query("where") where: String, @Query("fill") fill: String, @Query(
            "offset"
        ) offset: String, @Query("limit") limit: String
    ): Response<String>

    @GET("v1/devices/{deviceID}/version")
    suspend fun getDeviceVersion(@Path("deviceID") deviceID: String): Response<String>

    @GET("v1/shops/{shopID}/status")
    suspend fun getShopStatus(@Path("shopID") shopID: String): Response<String>

    @POST("v2/orders/status")
    suspend fun setOrderStatus(@Body payload: RequestBody): Response<String>

}
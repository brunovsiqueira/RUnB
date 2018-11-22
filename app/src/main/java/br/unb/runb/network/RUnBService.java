package br.unb.runb.network;

import android.content.Context;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

public class RUnBService {

    public static final String BASE_URL  = "https://apisandbox.cieloecommerce.cielo.com.br";
    public static final String BASE_QUERY_URL  = "https://apiquerysandbox.cieloecommerce.cielo.com.br";
    public static final String MERCHANT_ID = "448fcb7c-6670-4493-995d-1863671899ee";
    public static final String MERCHANT_KEY = "ZCMHARNFVOTJNWILJADFAZJUCOQBBBKVRSNRWCQU";


    public static void get(String url, String token, Context context, FutureCallback<JsonObject> jsonObjectCallback) {

        Ion.with(context).load(url).setHeader("Authorization", "Bearer "+token).asJsonObject().setCallback(jsonObjectCallback);
    }

    public static void get(String url, Context context, FutureCallback<JsonObject> jsonObjectCallback) {

        Ion.with(context).load(url).asJsonObject().setCallback(jsonObjectCallback);
    }

    public static void getArray(String url, String token, Context context, FutureCallback<JsonArray> jsonObjectCallback) {

        Ion.with(context).load(url).setHeader("Authorization", "Bearer "+token).asJsonArray().setCallback(jsonObjectCallback);
    }

    public static void getArray(String url, Context context, FutureCallback<JsonArray> jsonObjectCallback) {

        Ion.with(context).load(url).asJsonArray().setCallback(jsonObjectCallback);
    }

    //POST payment
    public static void postPayment(String url, JsonObject json, String token, Context context, FutureCallback<JsonObject> futureCallback) {

        Ion.with(context).load(url).setHeader("MerchantId", MERCHANT_ID).setHeader("merchantKey", MERCHANT_KEY).setJsonObjectBody(json).asJsonObject().setCallback(futureCallback);
    }

    public static void post(String url, JsonObject json, Context context, FutureCallback<JsonObject> futureCallback) {

        Ion.with(context).load(url).setJsonObjectBody(json).asJsonObject().setCallback(futureCallback);
    }

    public static void postResponse(String url, JsonObject json, Context context, FutureCallback<Response<JsonObject>> futureCallback) {

        Ion.with(context).load(url).setJsonObjectBody(json).asJsonObject().withResponse().setCallback(futureCallback);
    }

    public static void post(String url, JsonObject json, String token, Context context, FutureCallback<JsonObject> futureCallback) {

        Ion.with(context).load(url).setHeader("Authorization", "Bearer "+token).setJsonObjectBody(json).asJsonObject().setCallback(futureCallback);
    }

    public static void put(String url, JsonObject json, String token, Context context, FutureCallback<Response<JsonObject>> futureCallback) {

        Ion.with(context).load("PUT",url).setHeader("Authorization", "Bearer "+token).setJsonObjectBody(json).asJsonObject().withResponse().setCallback(futureCallback);
    }

    public static void put(String url, JsonObject json, Context context, FutureCallback<Response<JsonObject>> futureCallback) {

        Ion.with(context).load("PUT",url).setJsonObjectBody(json).asJsonObject().withResponse().setCallback(futureCallback);
    }

    public static class RUnBPath{

        public static final String SIGN_IN = BASE_URL + "/jwt/sign_in.json";
        public static final String REFRESH = BASE_URL + "/jwt/refresh.json";
        public static final String USER = BASE_URL + "/user.json";
        public static final String AUTHORIZE = BASE_URL + "/user/authorize.json";
        public static final String SALES = BASE_URL + "1/sales.json";


        public static String getOrderProductsByOrderId(String orderId) {
            return BASE_URL + "/order_products.json?order_id=" + orderId;
        }

        public static String getProducts(String id) {
            return BASE_URL + "/events/" + id + "/products.json";
        }

        public static String getAccesses(String id) {
            return BASE_URL + "/event_dates/" + id + "/accesses.json";
        }

        public static String getFavoritesAfterStarDate(String startDate) {
            return BASE_URL + "/user/favorite_events.json?start_date=" + startDate;
        }

        public static String getCities(String id){
            return BASE_URL + "/states/" + id + "/cities.json";
        }


        public static String deleteFavorites(String id){
            return BASE_URL + "/user/favorite_events/" + id + ".json";
        }

        public static String getPromotersByProducerId(String producerId) {
            return BASE_URL + "/users/" + producerId + "/promoters.json";
        }

        public static String getPointOfSalesByProducerId(String producerId) {
            return BASE_URL + "/users/" + producerId + "/point_of_sales.json";
        }

        public static String postPaymentWithOrderId(String orderId) {
            return BASE_URL + "/orders/" + orderId + "/payments.json";
        }

        public static String putOrdersById(String orderId) {
            return BASE_URL + "/orders/" + orderId + ".json";
        }

        public static String deleteFriend(String friendId) {
            return BASE_URL + "/user/friends/" + friendId + ".json";
        }


    }
}

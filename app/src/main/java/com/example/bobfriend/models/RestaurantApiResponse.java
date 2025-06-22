package com.example.bobfriend.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RestaurantApiResponse {
    @SerializedName("PlaceThatDoATasteyFoodSt")
    public List<ResponseWrapper> responseList;

    public static class ResponseWrapper {
        @SerializedName("head")
        public List<Head> head;

        @SerializedName("row")
        public List<RestaurantRaw> rows;
    }

    public static class Head {
        @SerializedName("list_total_count")
        public int listTotalCount;

        @SerializedName("RESULT")
        public Result result;
    }

    public static class Result {
        @SerializedName("CODE")
        public String code;

        @SerializedName("MESSAGE")
        public String message;
    }

    public static class RestaurantRaw {
        @SerializedName("SIGUN_NM")
        public String sigunNm;

        @SerializedName("SIGUN_CD")
        public String sigunCd;

        @SerializedName("RESTRT_NM")
        public String name;

        @SerializedName("REPRSNT_FOOD_NM")
        public String category;

        @SerializedName("REFINE_ROADNM_ADDR")
        public String address;

        @SerializedName("TASTFDPLC_TELNO")
        public String phone;

        @SerializedName("REFINE_WGS84_LAT")
        public String latitude;

        @SerializedName("REFINE_WGS84_LOGT")
        public String longitude;

        @SerializedName("RM_MATR")
        public String remarks;
    }
}
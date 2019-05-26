package com.zedled.app.service.net;

import com.zedled.app.service.model.PopularTag;
import com.zedled.app.service.model.Question;
import com.zedled.app.service.model.ResponseList;
import com.zedled.app.service.model.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface DataService {

    @GET("questions")
    Call<ResponseList<Question>> trendingQuestion(@QueryMap Map<String,String> options);

    @GET("me")
    Call<ResponseList<User>> getUserInfo(@QueryMap Map<String, String> options);

    @GET("tags")
    Call<ResponseList<PopularTag>> getPopularTag(@QueryMap Map<String, String> options);

}

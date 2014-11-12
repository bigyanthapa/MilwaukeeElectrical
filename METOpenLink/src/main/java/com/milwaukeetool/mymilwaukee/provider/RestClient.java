package com.milwaukeetool.mymilwaukee.provider;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by scott.hopfensperger on 11/11/2014.
 */
public class RestClient {
        private static Api REST_CLIENT;
        private static String ROOT =
                "http://openlink.qa12.centaredc.com/api/v1";

        static {
            setupRestClient();
        }

        private RestClient() {}

        public static Api get() {
            return REST_CLIENT;
        }

        private static void setupRestClient() {
            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(ROOT)
                    .setConverter(new GsonConverter(gson))
                    .setClient(new OkClient(new OkHttpClient()))
                    .setLogLevel(RestAdapter.LogLevel.FULL);

            RestAdapter restAdapter = builder.build();
            REST_CLIENT = restAdapter.create(Api.class);
        }
}

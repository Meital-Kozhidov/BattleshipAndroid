/* By : Meital Kozhidov
 * April 21
 * To: Tamar Benaya
 */
package com.example.battleship;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*API CLIENT CLASS- this class generates an implementation of the API interface. */

class ApiClient {

    //base url of the api
    public static final String BASE_URL = "http://192.168.14.143/battleship/api/";

    private static Retrofit retrofit = null;
    private static Gson gson;

    public static Retrofit getClient() {

        if (retrofit == null) {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //converts the gson response received from the server to a retrofit response
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}

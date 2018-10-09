package com.brandhype.medixfora.NetworkIOConstants;

/**
 * Created by USER on 11-07-2017.
 */

public interface NetworkIOConstant {

    public interface HttpRequestType {
        int HTTP_GET_REQUEST = 0;
        int HTTP_POST_REQUEST = 1;
        int HTTP_PUT_REQUEST = 2;
        int HTTP_DELETE_REQUEST = 3;
    }

    public interface CS_Type {
        String device_type="1";
    }

    public interface CS_APIUrls {
        // -----------------------LIVE----------------------------

        //String BASE_URL = "http://brandhypedigital.in/demo/medixfora/restapi/";
        String BASE_URL = "http://medixfora.com.md-in-64.webhostbox.net/restapi/";
        //-------------------------TEST ------------------------------
        //String BASE_URL = "http://brandhypedigital.in/demo/medixfora/restapi/";
    }

    public interface CS_Token {
        String TOKEN="1edc0ae98198866510bce219d5115b72";
    }
}


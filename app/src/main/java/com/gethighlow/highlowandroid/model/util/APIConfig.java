package com.gethighlow.highlowandroid.model.util;

public interface APIConfig {
    //To connect to the development server, the base url should be "http://10.0.2.2:5050"
    //For production, it should be "https://api.gethighlow.com"
    String base_url = "https://api.gethighlow.com";
    String json_content_type = "application/json";
}

package com.mycompany.geocoding3;

/**
 * Created by de-weikung on 3/4/15.
 */

import org.json.simple.JSONArray;
        import org.json.simple.JSONObject;
        import org.json.simple.parser.JSONParser;
        import org.json.simple.parser.ParseException;
        import org.scribe.builder.ServiceBuilder;
        import org.scribe.model.OAuthRequest;
        import org.scribe.model.Response;
        import org.scribe.model.Token;
        import org.scribe.model.Verb;
        import org.scribe.oauth.OAuthService;

        import com.beust.jcommander.JCommander;
        import com.beust.jcommander.Parameter;

/**
 * Code sample for accessing the Yelp API V2.
 *
 * This program demonstrates the capability of the Yelp API version 2.0 by using the Search API to
 * query for businesses by a search term and location, and the Business API to query additional
 * information about the top result from the search query.
 *
 * <p>
 * See <a href="http://www.yelp.com/developers/documentation">Yelp Documentation</a> for more info.
 *
 */
public class YelpAPI {

    private String API_HOST = "api.yelp.com";
    private String search_term;
    //private static final String DEFAULT_LOCATION = "San Francisco, CA";
    private String SEARCH_PATH = "/v2/search";
    private String BUSINESS_PATH = "/v2/business";

    //private String search_location;
    private double    latitude;
    private double    longitude;
    private int    search_radius;
    private int    search_num;

    /*
     * Update OAuth credentials below from the Yelp Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */

    OAuthService service;
    Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public YelpAPI(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service =
                new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(consumerKey)
                        .apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
        this.search_term = "restaurant"; // change the search term here
    }

    /**
     * Creates and sends a request to the Search API by term and location.
     * <p>
     * See <a href="http://www.yelp.com/developers/documentation/v2/search_api">Yelp Search API V2</a>
     * for more info.
     *
    */
    public String searchForBusinessesByLocation() {//String term, String location
        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", this.search_term);
        request.addQuerystringParameter("ll", this.latitude + "," + this.longitude);//request.addQuerystringParameter("location", this.search_location);
        request.addQuerystringParameter("radius_filter", Integer.toString(this.search_radius));
        request.addQuerystringParameter("limit", String.valueOf(this.search_num));
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and sends a request to the Business API by business ID.
     * <p>
     * See <a href="http://www.yelp.com/developers/documentation/v2/business">Yelp Business API V2</a>
     * for more info.
     *
     * @param businessID <tt>String</tt> business ID of the requested business
     * @return <tt>String</tt> JSON Response
     */
    public String searchByBusinessId(String businessID) {
        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint specified.
     *
     * @param path API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    private OAuthRequest createOAuthRequest(String path) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://" + API_HOST + path);
        return request;
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     *
     * @param request {@link OAuthRequest} corresponding to the API request
     * @return <tt>String</tt> body of API response
     */
    private String sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        System.out.println("response.getBody() = "+response.getBody());
        return response.getBody();
    }

    private String queryAPI() {
        String searchResponseJSON =
                this.searchForBusinessesByLocation();//this.search_term, this.search_location
        return searchResponseJSON;
    }


    public String search(double lat, double lng){
        return this.search(lat, lng, 1609 * 5, 10);
    } // change search parameter here

    public String search(double lat, double lng, int distance, int number) {
        this.latitude = lat;
        this.longitude = lng;
        this.search_radius = distance;
        this.search_num = number;

        return queryAPI();
    }

}
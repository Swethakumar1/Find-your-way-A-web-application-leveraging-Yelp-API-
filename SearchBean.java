package com.process;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import org.scribe.oauth.OAuthService;

import org.scribe.builder.ServiceBuilder;

import org.scribe.model.Response;
import org.scribe.model.Token;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;
import org.json.JSONArray;
//import org.json.JSONException;
import org.json.JSONObject;

/**
 * Session Bean implementation class Search
 */
@Stateless
public class SearchBean implements SearchBeanLocal {
	  private static final String CONSUMER_KEY = "4PUcjqTDZWFTuuWdv4RBzA";
	  private static final String CONSUMER_SECRET = "Y2IJ-KMknmnJD5NOhoxdHUHkTiY";
	  private static final String TOKEN = "NlB_rSjZv87Eo3Z5iWYrj7kddTIWmHMy";
	  private static final String TOKEN_SECRET = "2QpMK2wLyGevkGHLy5Fe2Gppr7E";
	  private static final String limit = "1";
	  
	 // OAuthService service;
	 // Token accessToken;
	  
    /**
     * Default constructor. 
     */
    public SearchBean() {
        // TODO Auto-generated constructor stub  	
    }
        
    public List<PlaceOfInterest> getLocation(String find, String location, String sortBy, String distance){
    	OAuthService service = new ServiceBuilder().provider(TwoStepAuthorization.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
    	Token accessToken = new Token(TOKEN, TOKEN_SECRET);
    	String jsonObject = getResponse(service, accessToken, find, location, sortBy, distance);
    	return parseResponseJSON(jsonObject);   	
    }
    
    /*
     * Constructs the request and returns the response
     */   
    private static String getResponse(OAuthService service, Token accessToken, String find, String location, String sortBy, String distance){
		 OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.yelp.com/v2/search");
		 request.addQuerystringParameter("term", find);
		 request.addQuerystringParameter("location", location);
		 request.addQuerystringParameter("sort", sortBy);
		 if(distance != null && !distance.isEmpty())
		 {
			 request.addQuerystringParameter("radius_filter", distance);
		 }
		 request.addQuerystringParameter("limit", limit);
		 service.signRequest(accessToken, request);
		 Response response = request.send();
		 System.out.println("JSON Response from Yelp Api: " + response.getBody());
		 return response.getBody();
    }
    
    /*
     *  Parses the JSON object and returns the model
     */ 
    private static List<PlaceOfInterest> parseResponseJSON(String json){
    	List<PlaceOfInterest> places = new ArrayList<PlaceOfInterest>();
    	StringBuffer sb;
    	try{
        	JSONObject jsonObject = new JSONObject(json);
        	if(jsonObject.has("businesses")){
	        	JSONArray placeData = jsonObject.getJSONArray("businesses");
	        	for(int i = 0; i < placeData.length(); i++){
	        		JSONObject placeJson = placeData.getJSONObject(i);
	        		PlaceOfInterest place = new PlaceOfInterest();
	        		place.setName(placeJson.getString("name"));        
	        		if(placeJson.has("phone")){
	        			place.setPhoneNumber(placeJson.getString("phone"));
	        		}
	        		if(placeJson.has("rating")){
	            		place.setRating(placeJson.getDouble("rating"));
	        		}
	        		if(placeJson.has("review_count")){
	        			place.setReviewCount(placeJson.getInt("review_count"));
	        		}
	        		JSONArray address = placeJson.getJSONObject("location").getJSONArray("display_address");
	        		sb = new StringBuffer();
	        		for(int j = 0; j < address.length(); j++){
	        			sb.append(address.getString(j));
	        			sb.append(" ");
	        		}
	        		place.setAddress(sb.toString());
	        		place.setCoordinates(placeJson.getJSONObject("location").getJSONObject("coordinate").getString("latitude").concat(", ").concat(placeJson.getJSONObject("location").getJSONObject("coordinate").getString("longitude")));
	        		places.add(place);
	        	}
        	}
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    	
    	return places;
    }
   
}

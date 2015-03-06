package com.mycompany.geocoding3;

/**
 * Created by de-weikung on 3/4/15.
 */

import android.util.Log;

        import java.io.IOException;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Date;

        import javax.xml.xpath.XPath;
        import javax.xml.xpath.XPathConstants;
        import javax.xml.xpath.XPathExpressionException;
        import javax.xml.xpath.XPathFactory;

        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;
        import org.xml.sax.InputSource;


public class WeatherQuery {

    private HttpURLConnection connection;
    private String httpRequest;
    private XPath xpath;
    private InputSource inputXml;

    public WeatherQuery() {
        connection = null;
        httpRequest = "http://api.wunderground.com/api/e26758698e45a10c/";
        xpath = XPathFactory.newInstance().newXPath();
        inputXml = null;
    }


    // adding private to query API
    public Weather getWeather(String cn) throws XPathExpressionException, IOException{

//        Log.v("Weather", cn);
        FetchAPI fapi = new FetchAPI("forecast/q/" + cn + ".xml");
        fapi.start();
        try {
            fapi.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return fapi.getResult();
    }

    public class FetchAPI extends Thread {
        private String request;
        private Weather weather;

        public FetchAPI(String r){
            request = r;
        }

        public void run() {
            try {
                weather = getAPIResponse(request);
            }catch(XPathExpressionException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public Weather getResult(){
            return weather;
        }
    }
    private Weather getAPIResponse(String request) throws IOException, XPathExpressionException{

        URL url = new URL(httpRequest + request);

        connection = (HttpURLConnection) url.openConnection();
        inputXml = new InputSource(connection.getInputStream());
        NodeList titleNodes = (NodeList) xpath.compile("/response/forecast/txt_forecast/forecastdays/forecastday/title").evaluate(inputXml, XPathConstants.NODESET);

        connection = (HttpURLConnection) url.openConnection();
        inputXml = new InputSource(connection.getInputStream());
        NodeList forcastNodes = (NodeList) xpath.compile("/response/forecast/txt_forecast/forecastdays/forecastday/fcttext").evaluate(inputXml, XPathConstants.NODESET);



//		for (int i = 0, n = iconNodes.getLength(); i < n; i++) {
//			Node node = iconNodes.item(i).getFirstChild();
//			System.out.println(node.getNodeName() + ": " + node.getNodeValue());
//			System.out.println(node.toString());
//
//	    }
        String summary = "";
        for (int i=0; i<titleNodes.getLength(); i++){
            summary += ("------------------------------------------------\n"
                    + titleNodes.item(i).getFirstChild().getNodeValue() + ": " + forcastNodes.item(i).getFirstChild().getNodeValue()+"\n");

        }

        return new Weather(summary);
    }
}
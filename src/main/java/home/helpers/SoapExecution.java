package home.helpers;

import io.qameta.allure.Step;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class SoapExecution {
    private  final Logger logger = Logger.getLogger(SoapExecution.class);

    @Step("Отправка запроса \n Эндпоинт: {0} \n Метод: {1} \n Запрос: {2}")
    public String sendPost(String endpoint, String method, String request) throws Exception {

        String resp="";
        logger.info("Отправка POST запроса Эндпоинт: "+endpoint+" \n Метод: "+method+" \n Запрос: "+request);
        try (CloseableHttpClient client = HttpClientBuilder.create().build())
        {
            HttpPost req = new HttpPost(endpoint);
            req.setEntity(new StringEntity(request));
            HttpResponse response = client.execute(req);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            resp=builder.toString();
            logger.info("Получен ответ от web-service: \n"+resp);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
        return resp;
    }

    @Step("Отправка POST запроса \n Эндпоинт: {0} \n URL-параметры: {1} \n Запрос: {2}")
    public String executePost(String targetURL, String urlParameters, String request) {
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(targetURL);
            logger.info("Отправка POST запроса Эндпоинт: "+targetURL+" \n URL-параметры: "+urlParameters+" \n Запрос: "+request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.writeBytes(request);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            logger.info("Получен ответ от web-service: \n"+response.toString());
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Step("Отправка GET запроса \n Эндпоинт: {0} \n Authorization: {1}")
    public String executeGet(String targetURL, String authorization) throws Exception {
        int errorCode;
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(targetURL);

            if(authorization != null){
                httpGet.addHeader("Authorization", "Bearer " + authorization);
            }
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } else {
                    throw new Exception("status code: "+Integer.toString(statusCode));
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                e.getMessage();

            } catch (IOException e) {
                e.printStackTrace();
                e.getMessage();
            }
            logger.info("Получен ответ от web-service: \n"+builder.toString());
            return builder.toString();
        }

}

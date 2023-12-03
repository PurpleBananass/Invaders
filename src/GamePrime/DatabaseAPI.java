package GamePrime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

public class DatabaseAPI {
    @Test
    public static JSONArray GetRank(String p) {
        try {
            // Google.com에 연결할 URL 생성
            URL url = new URL("http://146.56.180.210:3200/rank/"+p);

            // HttpURLConnection 객체 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 메소드 설정 (GET 방식으로 요청)
            connection.setRequestMethod("GET");

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            System.out.println("HTTP 응답 코드: " + responseCode);

            // 응답 내용 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            
            reader.close();
            String rankData = response.toString();
            JSONArray jobj = new JSONArray();
            if (!response.toString().isEmpty()) {
                JSONParser parser = new JSONParser();
                try {
                    jobj = (JSONArray) parser.parse(rankData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return jobj;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

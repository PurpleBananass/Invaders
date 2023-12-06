package GamePrime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DatabaseAPI {
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
    public static void PostRank(String jsonInputString) {
        try {
            // 목표 URL 설정
            URL url = new URL("http://146.56.180.210:3200/rank/");

            // HttpURLConnection 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 메서드 설정
            connection.setRequestMethod("POST");

            // 요청 헤더 설정 (Content-Type을 application/json으로 설정)
            connection.setRequestProperty("Content-Type", "application/json");

            // 요청 본문 작성
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // HTTP 응답 코드 확인
            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);

            // 응답 본문 읽기 (생략 가능)
            // ...

            // 연결 종료
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
package EnginePrime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileManager {

    public BufferedImage GetImage(String name) {
        try {
            File inputFile = new File(name); // 읽을 PNG 파일 경로
            return ImageIO.read(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void SaveString(String name, String s , boolean overwrite) {
        String path = "res" + File.separator + name;
        try (FileWriter fileWriter = new FileWriter(path, Charset.forName("UTF-8"),!overwrite)) {
            fileWriter.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject LoadJsonObject(String name){
        String jsonString = LoadString(name);
        JSONObject jobj = new JSONObject();
        if(!jsonString.isEmpty()){
            JSONParser parser = new JSONParser();
            try {
                jobj = (JSONObject) parser.parse(jsonString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return jobj;
    }

    public String LoadString(String name) {
        String path = "res" + File.separator + name;
        StringBuilder output = new StringBuilder(); 
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, Charset.forName("UTF-8")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line); // StringBuilder에 누적
            }
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // 읽은 문자열을 반환
    }    
}

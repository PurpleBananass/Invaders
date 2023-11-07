package EnginePrime;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileManager {

    public void SaveString(String name, String s) {
        String path = "res" + File.separator + name;
        try (FileWriter fileWriter = new FileWriter(path, Charset.forName("UTF-8"))) {
            fileWriter.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String LoadString(String name) {
        String path = "res" + File.separator + name;
        StringBuilder output = new StringBuilder(); 
    
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, Charset.forName("UTF-8")))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                output.append(line); // StringBuilder에 누적
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString(); // 읽은 문자열을 반환
    }    
}

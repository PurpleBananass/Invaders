package GamePrime.ETC;

import org.json.simple.JSONObject;

public class Score implements Comparable<Score> {
    public String name;
    public int value;
    public int rank;

    public Score(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Score other) {
        return this.value - other.value;
    }

    public static Score toScore(JSONObject obj) {
        return new Score((String) obj.get("username"), ((Number) obj.get("score")).intValue());
    }

    public String toJSONString(String mode) {
        JSONObject json = new JSONObject();
        json.put("username", this.name);
        json.put("score", this.value);
        json.put("mode", mode);
        return json.toString();
    }
}

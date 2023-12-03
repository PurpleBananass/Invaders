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
        return new Score((String) obj.get("name"), ((Number) obj.get("value")).intValue());
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("value", this.value);
        json.put("rank", this.rank);
        return json;
    }
}

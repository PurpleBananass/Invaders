package GamePrime;

public class Score implements Comparable<Score>{
    public String name;
    public int value;
    public Score(String name, int value){
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(Score other) {
        return this.value - other.value;
    }
}

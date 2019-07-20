
package ripe.ripe.Utils;

public class PersonDataModel {

    int rank;
    String name;
    int score;

    public PersonDataModel(int rank, String name, int score) {
        this.rank = rank;
        this.name = name;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getScore() { return score; }

}

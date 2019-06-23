
package ripe.ripe;

public class PersonDataModel {

    int rank;
    String name;
    String email;
    int image;

    public PersonDataModel(int rank, String name, String email, int image) {
        this.rank = rank;
        this.name = name;
        this.email = email;
        this.image=image;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getImage() {
        return image;
    }

}

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Resept {

    @Getter
    private List<Legemiddel> legemidler = new ArrayList<>();

    @Getter @Setter
    private Person reseptmottaker;

    public double getReseptbeløp() {
        double sum = 0;
        for (Legemiddel legemiddel : legemidler) {
            sum += legemiddel.getUtsalgspris();
        }
        return sum;
    }

    public void addLegemiddel(Legemiddel legemiddel) {
        legemidler.add(legemiddel);
    }

}

import lombok.Getter;
import lombok.Setter;

public class Legemiddel {

    @Getter @Setter
    private double utsalgspris;

    @Getter @Setter
    private Double trinnpris;

    double getRefusjonsgrunnlag() {
        if (getTrinnpris() != null && getTrinnpris() < getUtsalgspris()) {
            return getTrinnpris();
        } else {
            return getUtsalgspris();
        }
    }

    public double getUdekketbeløp() {
        if (getTrinnpris() != null && getTrinnpris() < getUtsalgspris()) {
            return getUtsalgspris() - getTrinnpris();
        } else {
            return 0;
        }
    }

}

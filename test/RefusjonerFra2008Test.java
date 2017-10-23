import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

// https://lovdata.no/dokument/LTI/forskrift/2008-12-17-1406
public class RefusjonerFra2008Test {

    private static final Refusjonsberegning refusjonsberegning = Refusjonsberegning.forTidspunkt(LocalDate.of(2008, 12, 30));

    @Test
    public void egenandelEr36prosent() {
        Resept resept = createReseptMedNormalMottaker();

        Legemiddel legemiddel = createLegemiddel(200);
        resept.addLegemiddel(legemiddel);

        assertEquals(0.36 * resept.getReseptbeløp(), refusjonsberegning.getEgenandel(resept),
            0.1);
    }

    private Legemiddel createLegemiddel(double pris) {
        Legemiddel legemiddel = new Legemiddel();
        legemiddel.setUtsalgspris(pris);
        return legemiddel;
    }

    private Resept createReseptMedNormalMottaker() {
        Resept resept = new Resept();
        Person reseptmottaker = new Person();
        reseptmottaker.setFødselsdato(LocalDate.now().minusYears(17));
        resept.setReseptmottaker(reseptmottaker);
        return resept;
    }
}

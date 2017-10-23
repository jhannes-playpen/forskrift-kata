import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

// https://lovdata.no/forskrift/2007-06-28-814/§8
public class RefusjonerFra2009Test {

    private static final Refusjonsberegning refusjonsberegning = Refusjonsberegning.forTidspunkt(LocalDate.of(2009, 3, 12));

    // Medlemmet skal betale en egenandel på 39 prosent av reseptbeløpet,
    @Test
    public void egenandelEr39prosent() {
        Resept resept = createReseptMedNormalMottaker();

        Legemiddel legemiddel = createLegemiddel(200);
        resept.addLegemiddel(legemiddel);

        assertEquals(resept.getReseptbeløp(), legemiddel.getUtsalgspris(), 0.1);
        assertEquals(0.39 * resept.getReseptbeløp(), refusjonsberegning.getEgenandel(resept),
            0.1);

        assertEquals(0.61 * resept.getReseptbeløp(), refusjonsberegning.getRefusjon(resept), 0.1);
    }

    @Test
    public void egenandelSkalIkkeOverstige520() {
        Resept resept = createReseptMedNormalMottaker();

        resept.addLegemiddel(createLegemiddel(1350));

        assertEquals(520, refusjonsberegning.getEgenandel(resept),
            0.1);
    }

    // En resept = alle legemidler med samme forskriver og forskrivningsdato
    @Test
    public void reseptMedFlereLegemidler() {
        Resept resept = createReseptMedNormalMottaker();

        resept.addLegemiddel(createLegemiddel(650));
        resept.addLegemiddel(createLegemiddel(700));

        assertEquals(520, refusjonsberegning.getEgenandel(resept),
            0.1);
    }

    @Test
    public void barnSkalIkkeBetaleEgenandel() {
        Resept resept = new Resept();
        Person reseptmottaker = new Person();
        reseptmottaker.setFødselsdato(LocalDate.now().minusYears(15));
        resept.setReseptmottaker(reseptmottaker);

        resept.addLegemiddel(createLegemiddel(200));

        assertEquals(0.0, refusjonsberegning.getEgenandel(resept), 0.1);
    }


    @Test
    public void minstepensjonisterSkalIkkeBetaleEgenandel() {
        Resept resept = new Resept();
        Person reseptmottaker = new Person();
        reseptmottaker.setFødselsdato(LocalDate.now().minusYears(70));
        reseptmottaker.setMinstepensjonist(true);
        resept.setReseptmottaker(reseptmottaker);

        resept.addLegemiddel(createLegemiddel(200));

        assertEquals(0.0, refusjonsberegning.getEgenandel(resept), 0.1);
    }


    @Test
    public void pasientsEgenandelBeregnesAvTrinnspris() {
        Resept resept = createReseptMedNormalMottaker();

        Legemiddel legemiddel = createLegemiddel(200);
        legemiddel.setTrinnpris(100.0);
        resept.addLegemiddel(legemiddel);

        assertEquals(0.39 * legemiddel.getTrinnpris(), refusjonsberegning.getEgenandel(resept),
            0.1);
    }

    @Test
    public void apoteketsRefusjonBeregnesAvTrinnpris() {
        Resept resept = createReseptMedNormalMottaker();

        Legemiddel legemiddel = createLegemiddel(200);
        legemiddel.setTrinnpris(100.0);
        resept.addLegemiddel(legemiddel);

        assertEquals(0.61 * legemiddel.getTrinnpris(), refusjonsberegning.getRefusjon(resept),
            0.1);
    }

    @Test
    public void apoteketKanKreveMellomleggetMellomTrinnprisOgUtsalgspris() {
        Resept resept = createReseptMedNormalMottaker();

        Legemiddel legemiddel = createLegemiddel(200);
        legemiddel.setTrinnpris(100.0);
        resept.addLegemiddel(legemiddel);

        assertEquals(legemiddel.getUtsalgspris() - legemiddel.getTrinnpris(), refusjonsberegning.getMellomlegg(resept),
            0.1);
    }


    @Test
    public void pasientsEgenandelBeregnesAvTrinnsprisKunHvisUtsalgsprisHøyest() {
        Resept resept = createReseptMedNormalMottaker();

        Legemiddel legemiddel = createLegemiddel(90);
        legemiddel.setTrinnpris(100.0);
        resept.addLegemiddel(legemiddel);

        assertEquals(0.39 * legemiddel.getUtsalgspris(), refusjonsberegning.getEgenandel(resept),
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

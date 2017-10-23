import java.time.LocalDate;


public abstract class Refusjonsberegning {

    public static Refusjonsberegning forTidspunkt(LocalDate forskrivningsdato) {
        if (forskrivningsdato.isBefore(LocalDate.of(2009, 1, 1))) {
            return new RefusjonsberegningFor2008();
        }
        return new RefusjonsberegningFor2009();
    }


    public static class RefusjonsberegningFor2008 extends Refusjonsberegning {
        private static final int EGENANDEL_GRENSE = 520;
        private static final double EGENANDEL_SATS = 0.36;

        @Override
        public double getEgenandel(Resept resept) {
            if (isEgenandelFritak(resept)) {
                return 0;
            }
            double egenandel = getRefusjonsgrunnlag(resept) * EGENANDEL_SATS;
            return Math.min(egenandel, EGENANDEL_GRENSE);
        }

        private boolean isEgenandelFritak(Resept resept) {
            return resept.getReseptmottaker().erYngreEnn(16) || resept.getReseptmottaker().isMinstepensjonist();
        }

        @Override
        public double getRefusjon(Resept resept) {
            return resept.getReseptbeløp() - getEgenandel(resept);
        }

        @Override
        public double getMellomlegg(Resept resept) {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    private static class RefusjonsberegningFor2009 extends Refusjonsberegning {
        private static final int EGENANDEL_GRENSE = 520;
        private static final double EGENANDEL_SATS = 0.39;

        private boolean isEgenandelFritak(Resept resept) {
            return resept.getReseptmottaker().erYngreEnn(16) || resept.getReseptmottaker().isMinstepensjonist();
        }

        @Override
        public double getEgenandel(Resept resept) {
            if (isEgenandelFritak(resept)) {
                return 0;
            }
            double egenandel = getRefusjonsgrunnlag(resept) * EGENANDEL_SATS;
            return Math.min(egenandel, EGENANDEL_GRENSE);
        }

        @Override
        public double getRefusjon(Resept resept) {
            return getUtsalgspris(resept) - getEgenandel(resept) - getMellomlegg(resept);
        }

        @Override
        public double getMellomlegg(Resept resept) {
            double mellomlegg = 0;
            for (Legemiddel legemiddel : resept.getLegemidler()) {
                mellomlegg += legemiddel.getUdekketbeløp();
            }
            return mellomlegg;
        }
    }

    private static double getRefusjonsgrunnlag(Resept resept) {
        double egenandel = 0;
        for (Legemiddel legemiddel : resept.getLegemidler()) {
            egenandel += legemiddel.getRefusjonsgrunnlag();
        }
        return egenandel;
    }

    public double getUtsalgspris(Resept resept) {
        double utsalgspris = 0;
        for (Legemiddel legemiddel : resept.getLegemidler()) {
            utsalgspris += legemiddel.getUtsalgspris();
        }
        return utsalgspris;
    }

    public abstract double getEgenandel(Resept resept);

    public abstract double getRefusjon(Resept resept);

    public abstract double getMellomlegg(Resept resept);

}

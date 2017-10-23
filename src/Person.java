import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class Person {

    @Getter @Setter
    private LocalDate fødselsdato;

    @Getter @Setter
    private boolean minstepensjonist;

    public boolean erYngreEnn(int years) {
        return fødselsdato.plusYears(years).isAfter(LocalDate.now());
    }

}

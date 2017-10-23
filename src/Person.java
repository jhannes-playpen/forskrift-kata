import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

public class Person {

    @Getter @Setter
    private LocalDate f�dselsdato;

    @Getter @Setter
    private boolean minstepensjonist;

    public boolean erYngreEnn(int years) {
        return f�dselsdato.plusYears(years).isAfter(LocalDate.now());
    }

}

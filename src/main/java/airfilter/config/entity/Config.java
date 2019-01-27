package airfilter.config.entity;

import java.util.List;
import java.util.Objects;

public class Config implements Cloneable {

    private List<Absence> absences;

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        return Objects.hash(absences);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Config) {
            final Config other = (Config) obj;
            return Objects.equals(absences, other.absences);
        } else {
            return false;
        }
    }

}

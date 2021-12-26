package zagurskiy.fit.bstu.todolist;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Task implements Serializable {

    private String description;
    private String category;
    private LocalDate date;
    private boolean done;


    public boolean isDisplayed(LocalDate selectedDate) {
        if (selectedDate.equals(date)) return true;
        return selectedDate.equals(LocalDate.now()) && date.isBefore(selectedDate) && !done;
    }
}

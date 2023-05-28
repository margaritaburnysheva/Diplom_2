package order;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredients {
    private List<String> ingredients;
}

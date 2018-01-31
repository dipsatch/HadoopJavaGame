import org.junit.Test;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import com.redomar.game.lib.Name;


public class NamingTest {
    @Test
    public void namingWorks() {
        Name name = new Name();
        assertThat(name.setName("John"), containsString("John"));
    }
}

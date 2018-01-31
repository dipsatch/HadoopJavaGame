import org.junit.Test;
import static org.junit.Assert.*;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.entities.Dummy;
import com.redomar.game.entities.efx.Swim;

public class DummyPlayerTest {
    @Test
    public void dummyWorks() {
        LevelHandler lh = new LevelHandler("test");
        Dummy d = new Dummy(lh, "NameTest", 0, 0, 0, 0);
        Swim s = new Swim(lh, 0, 0);
        d.setSwim(s);
        assertTrue(s == d.getSwim());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.entities.Vendor;
import com.redomar.game.entities.efx.Swim;

public class VendorTest {
    @Test
    public void vendorWorks() {
        LevelHandler lh = new LevelHandler("test");
        Vendor v = new Vendor(lh, "NameTest", 0, 0, 0, 0);
        Swim s = new Swim(lh, 0, 0);
        v.setSwim(s);
        assertTrue(s == v.getSwim());
    }
}

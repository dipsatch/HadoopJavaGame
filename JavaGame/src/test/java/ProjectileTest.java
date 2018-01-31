import org.junit.Test;
import static org.junit.Assert.*;

import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.entities.projectiles.Projectile;

public class ProjectileTest {
    @Test
    public void projectileCollision() {
        LevelHandler lh = new LevelHandler("test");
        Projectile p = new Projectile(lh, 0, 0, 0) {
            @Override
            protected void move() {}
            @Override
            public void tick() {}
            @Override
            public void render(Screen screen) {}
        };
        assertFalse(p.tileCollision(0,0,0,0));
    }
}

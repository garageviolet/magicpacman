package gremlins;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    @Test
    public void constructor() {
        assertNotNull(new Player(60, 20,null, null, null, null, 3, null, null));
    }

    
}

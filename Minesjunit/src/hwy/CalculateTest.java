package hwy;
import static org.junit.Assert.*;
import org.junit.Test;

public class CalculateTest {
@Test
	public void add(){
		assertEquals(8,new Calculator().add(3,5));
	}
}

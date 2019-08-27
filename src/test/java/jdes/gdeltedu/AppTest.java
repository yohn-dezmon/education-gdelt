package jdes.gdeltedu;
import jdes.gdeltedu.DemoUDFs;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    
    @Test 
    public void testingSubstr2() {
    	
    	
    	assertEquals("NV", DemoUDFs.substr2("USNV"));
    	
    }
}

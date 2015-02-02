package org.chatable;

import org.junit.*;

/**
 * Created by henrystevens on 2/02/15.
 * For Summer Research Scholarship
 */
public class MainTest {

    Main a;

    @Before
    public void setUp() throws Exception {
        a = new Main();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void basicTest() {
        System.out.println("test");
    }

}

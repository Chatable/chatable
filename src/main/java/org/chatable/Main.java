package org.chatable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by henrystevens on 2/02/15.
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public Main() {
        System.out.println("this is a big test");
    }


    public boolean thisAlwaysReturnsTrue() {
        return true;
    }

}

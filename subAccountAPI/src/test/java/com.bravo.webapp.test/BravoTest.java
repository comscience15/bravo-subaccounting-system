package com.bravo.webapp.test;

import com.bravo.webapp.util.Encryption;
import com.bravo.webapp.util.Global;
import com.bravo.webapp.util.LocalStringManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 2/24/14
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(JUnit4.class)
public class BravoTest {
    private static final Logger logger = Logger.getLogger(BravoTest.class.getName());
    @Test
    public void getLocalStringTest() {
        logger.info(LocalStringManager.getLocalString("success.login", "test"));
        logger.info(MessageFormat.format(Global.SUCCESS_RESPONSE, LocalStringManager.getLocalString("success.login")));
    }

}

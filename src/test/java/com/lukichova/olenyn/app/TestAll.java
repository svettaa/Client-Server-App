package com.lukichova.olenyn.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                TestAES.class,
                TestPacket.class,
                TestMessage.class,
                TestProcessor.class,
                TestClient.class

        }
)
public class TestAll {


}
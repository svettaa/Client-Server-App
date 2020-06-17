package com.lukichova.olenyn.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                TestAES.class,
                TestPacket.class,
                TestMessage.class

        }
)
public class TestAll {


}
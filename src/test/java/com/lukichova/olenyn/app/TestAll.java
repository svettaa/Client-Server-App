package com.lukichova.olenyn.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                TestAES.class,
                TestPacket.class,
                TestMessage.class,
                ClientTest.class,
                ProcessorTest.class,
                TCPNetworkTest.class
        }
)
public class TestAll {


}
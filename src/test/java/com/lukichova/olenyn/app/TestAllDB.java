package com.lukichova.olenyn.app;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                TestDB.class,
                TestDBExceptions.class,
                TestHttpServer.class

        }
)
public class TestAllDB {

}
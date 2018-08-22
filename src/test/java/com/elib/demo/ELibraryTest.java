package com.elib.demo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ELibraryTest {

    private GoldenMaster goldenMaster;

    @Before
    public void setup() throws Exception{
        goldenMaster = new GoldenMaster();
    }
/*
    @Test
    public void should_generate_golden_master() throws Exception{
        goldenMaster.generateGoldenMaster();
    }
*/
    @Test
    public void should_check_golden_master() throws Exception{
        String expected = goldenMaster.readGoldenMaster().replaceAll(System.getProperty("line.separator"),"\n");
        String actual = goldenMaster.runResult().replaceAll(System.getProperty("line.separator"),"\n");

        assertEquals(expected, actual);
    }
}

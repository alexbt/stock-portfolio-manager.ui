package com.proserus.stocks.ui.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

public class AbstractUIUnit extends AbstractUnit{

	@Before
    @After
    public void clean() throws IOException {
        File dbFile = new File("src/test/resources/TestUIdb/data/db.script");
        dbFile.delete();
        FileUtils.copyFile(new File("src/test/resources/TestUIdb/data/untoucheddb.script"), dbFile, true);
        new File("src/test/resources/TestUIdb/data/db.properties").delete();
    }
}

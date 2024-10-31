package com.avr.mediastreamingserver.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

@SpringBootTest
public class HashToMediaLocMapTest {

    @Autowired
    HashToMediaLocMap hashToMediaLocMap;

    @Test
    void testGetFileLocFromHash() {
        Assertions.assertFalse(ObjectUtils.isEmpty(hashToMediaLocMap));
    }

    @Test
    void testInsertInMap() {

    }
}

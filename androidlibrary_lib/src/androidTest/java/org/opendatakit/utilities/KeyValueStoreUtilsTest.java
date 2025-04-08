package org.opendatakit.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opendatakit.database.data.KeyValueStoreEntry;
import org.opendatakit.aggregate.odktables.rest.ElementDataType;
import org.opendatakit.database.utilities.KeyValueStoreUtils;

import java.util.ArrayList;

public class KeyValueStoreUtilsTest {

    private static final String TABLE_ID = "testTable";
    private static final String PARTITION = "partition";
    private static final String ASPECT = "aspect";
    private static final String KEY = "key";
    private static final ElementDataType TYPE_NUMBER = ElementDataType.number;
    private static final ElementDataType TYPE_INTEGER = ElementDataType.integer;
    private static final ElementDataType TYPE_BOOL = ElementDataType.bool;
    private static final ElementDataType TYPE_STRING = ElementDataType.string;
    private static final ElementDataType TYPE_ARRAY = ElementDataType.array;
    private static final ElementDataType TYPE_OBJECT = ElementDataType.object;
    private static final String VALUE_NUMBER = "404.22";
    private static final String VALUE_INTEGER = "404";
    private static final String VALUE_BOOL_TRUE = "true";
    private static final String VALUE_BOOL_FALSE = "false";
    private static final String VALUE_BOOL_ZERO = "0";
    private static final String VALUE_BOOL_ONE = "1";
    private static final String VALUE_STRING = "testString";
    private static final String VALUE_MISMATCH = "wrongValue";
    private static final String VALUE_ARRAY = "[\"testItem1\", \"testItem2\"]";
    private static final String VALUE_OBJECT = "{\"key\":\"value\"}";
    private static final String TEST_APP_NAME = "testAppName";


    @Test
    public void testBuildEntry() {
        KeyValueStoreEntry entry = KeyValueStoreUtils.buildEntry(TABLE_ID, PARTITION, ASPECT, KEY, TYPE_INTEGER, VALUE_INTEGER);
        assertEquals(TABLE_ID, entry.tableId);
        assertEquals(PARTITION, entry.partition);
        assertEquals(ASPECT, entry.aspect);
        assertEquals(KEY, entry.key);
        assertEquals(TYPE_INTEGER.name(), entry.type);
        assertEquals(VALUE_INTEGER, entry.value);
    }

    @Test
    public void testGetNumber_NullEntry() {
        assertNull(KeyValueStoreUtils.getNumber(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumber_TypeMismatch() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_INTEGER.name();
        entry.value = VALUE_NUMBER;
        entry.key = KEY;

        KeyValueStoreUtils.getNumber(entry);
    }

    @Test
    public void testGetNumber_Success() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_NUMBER.name();
        entry.value = VALUE_NUMBER;

        assertEquals(Double.parseDouble(VALUE_NUMBER), KeyValueStoreUtils.getNumber(entry), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNumber_InvalidNumber() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_INTEGER.name();
        entry.value = VALUE_MISMATCH;
        entry.key = KEY;

        KeyValueStoreUtils.getNumber(entry);
    }

    @Test
    public void testGetInteger_NullEntry() {
        KeyValueStoreUtils.getNumber(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInteger_TypeMismatch() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_NUMBER.name();
        entry.value = VALUE_NUMBER;
        entry.key = KEY;

        KeyValueStoreUtils.getInteger(entry);
    }

    @Test
    public void testGetInteger_Success() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_INTEGER.name();
        entry.value = VALUE_INTEGER;
        entry.key = KEY;

        assertEquals(Integer.valueOf(VALUE_INTEGER), KeyValueStoreUtils.getInteger(entry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInteger_InvalidNumber() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_INTEGER.name();
        entry.value = VALUE_MISMATCH;
        entry.key = KEY;

        KeyValueStoreUtils.getInteger(entry);
    }

    @Test
    public void testGetBoolean_NullEntry() {
        KeyValueStoreUtils.getBoolean(null);
    }
    @Test
    public void testGetBoolean_NullValue() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_BOOL.name();
        entry.value = null;
        entry.key = KEY;

        assertNull(KeyValueStoreUtils.getBoolean(entry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoolean_TypeMismatch() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_INTEGER.name();
        entry.value = VALUE_MISMATCH;
        entry.key = KEY;

        KeyValueStoreUtils.getBoolean(entry);
    }

    // Ensuring expected behaviour is observed
    // with regular boolean values
    @Test
    public void testGetBoolean_SuccessTrue() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_BOOL.name();
        entry.value = VALUE_BOOL_TRUE;
        entry.key = KEY;

        assertTrue(KeyValueStoreUtils.getBoolean(entry));
    }

    // Ensuring expected behaviour is observed
    // with regular boolean values
    @Test
    public void testGetBoolean_SuccessFalse() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_BOOL.name();
        entry.value = VALUE_BOOL_FALSE;
        entry.key = KEY;

        assertFalse(KeyValueStoreUtils.getBoolean(entry));
    }

    // In a key-value store, booleans
    // are stored as 0 and 1
    @Test
    public void testGetBoolean_SuccessOne() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_BOOL.name();
        entry.value = VALUE_BOOL_ONE;
        entry.key = KEY;

        assertTrue(KeyValueStoreUtils.getBoolean(entry));
    }

    // In a key-value store, booleans
    // are stored as 0 and 1
    @Test
    public void testGetBoolean_SuccessZero() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_BOOL.name();
        entry.value = VALUE_BOOL_ZERO;
        entry.key = KEY;

        assertFalse(KeyValueStoreUtils.getBoolean(entry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetBoolean_InvalidBoolean() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_BOOL.name();
        entry.value = VALUE_MISMATCH;
        entry.key = KEY;

        assertTrue(KeyValueStoreUtils.getBoolean(entry));
    }

    @Test
    public void testGetString_NullEntry() {
        assertNull(KeyValueStoreUtils.getString(null));
    }

    @Test
    public void testGetString_NullValue() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_STRING.name();
        entry.value = null;
        entry.key = KEY;

        assertNull(KeyValueStoreUtils.getString(entry));
    }

    @Test
    public void testGetString_Success() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_STRING.name();
        entry.value = VALUE_STRING;
        entry.key = KEY;

        assertEquals(VALUE_STRING, KeyValueStoreUtils.getString(entry));
    }

    @Test
    public void testGetArray_NullEntry() {
        assertNull(KeyValueStoreUtils.getArray(TEST_APP_NAME, null, String.class));
    }

    @Test
    public void testGetArray_NullOrEmptyValue() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_ARRAY.name();

        // null value
        entry.value = null;
        assertNull(KeyValueStoreUtils.getArray(TEST_APP_NAME, entry, String.class));

        // empty value
        entry.value = "";
        assertNull(KeyValueStoreUtils.getArray(TEST_APP_NAME, entry, String.class));

    }

    @Test
    public void testGetArray_Success() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_ARRAY.name();
        entry.value = VALUE_ARRAY;
        entry.key = KEY;

        ArrayList<String> result = KeyValueStoreUtils.getArray(TEST_APP_NAME, entry, String.class);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testItem1", result.get(0));
        assertEquals("testItem2", result.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetArray_TypeMismatch() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_STRING.name();
        entry.value = VALUE_ARRAY;
        entry.key = KEY;

        KeyValueStoreUtils.getArray(TEST_APP_NAME, entry, String.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGetArray_InvalidJson() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_ARRAY.name();
        entry.value = VALUE_MISMATCH;
        entry.key = KEY;

        KeyValueStoreUtils.getArray(TEST_APP_NAME, entry, String.class);
    }

    @Test
    public void testGetObject_NullEntry() {
        assertNull(KeyValueStoreUtils.getObject(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetObject_TypeMismatch() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_INTEGER.name();
        entry.value = VALUE_OBJECT;
        entry.key = KEY;

        KeyValueStoreUtils.getObject(entry);
    }

    @Test
    public void testGetObject_Success() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_OBJECT.name();
        entry.value = VALUE_OBJECT;
        entry.key = KEY;

        assertEquals(VALUE_OBJECT, KeyValueStoreUtils.getObject(entry));
    }

    @Test
    public void testGetObject_SupportsArray() {
        KeyValueStoreEntry entry = new KeyValueStoreEntry();
        entry.type = TYPE_ARRAY.name();
        entry.value = VALUE_ARRAY;
        entry.key = KEY;

        assertEquals(VALUE_ARRAY, KeyValueStoreUtils.getObject(entry));
    }
}


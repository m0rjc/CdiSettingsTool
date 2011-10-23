package uk.me.m0rjc.cdiSettingsTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test the behaviour of primitive types in Java.
 * Test {@link FieldInjector} and {@link MethodInjector} with these types.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
public class TestPrimitiveTypeInjection
{
    /** A primitive target. */
    public int m_primitive;
    /** A wrapper target. */
    public Integer m_wrapper;

    /** @param value Set the primitive value. */
    public void setPrimitive(final int value)
    {
        m_primitive = value;
    }

    /** @param value set the wrapper value. */
    public void setWrapper(final Integer value)
    {
        m_wrapper = value;
    }


    /** Prove that the primitive and the wrapper are not the same. */
    @Test
    public void testBooleanNotEqualsboolean()
    {
        Type primitive = Boolean.TYPE;
        Type wrapper = Boolean.class;
        Assert.assertFalse(primitive.equals(wrapper));
    }

    /** Test I can look up the primitive in a map. */
    @Test
    public void testPrimitiveInMap()
    {
        Map<Type, String> testMap = new HashMap<Type, String>();
        testMap.put(Boolean.class, "theWrapper");
        testMap.put(Boolean.TYPE, "thePrimitive");

        String result = testMap.get(Boolean.TYPE);
        Assert.assertEquals("thePrimitive", result);
    }

    /** Test I can look up the wrapper in a map. */
    @Test
    public void testWrapperInMap()
    {
        Map<Type, String> testMap = new HashMap<Type, String>();
        testMap.put(Boolean.class, "theWrapper");
        testMap.put(Boolean.TYPE, "thePrimitive");

        String result = testMap.get(Boolean.class);
        Assert.assertEquals("theWrapper", result);
    }

    /**
     * Test I can set the primitive field by reflection.
     * @throws Exception on error.
     */
    @Test
    public void testSetPrimitiveFieldByReflection() throws Exception
    {
        final int meaningOfLife = 42;

        Field theField = getClass().getField("m_primitive");
        FieldInjector<TestPrimitiveTypeInjection, Integer> injector = new FieldInjector<TestPrimitiveTypeInjection, Integer>(theField);
        injector.inject(this, meaningOfLife);
        Assert.assertEquals(meaningOfLife, m_primitive);
    }

    /**
     * Test I can set the wrapper field by reflection.
     * @throws Exception on error
     */
    @Test
    public void testSetWrapperFieldByReflection() throws Exception
    {
        final int luckyForSome = 13;
        Field theField = getClass().getField("m_wrapper");
        FieldInjector<TestPrimitiveTypeInjection, Integer> injector = new FieldInjector<TestPrimitiveTypeInjection, Integer>(theField);
        injector.inject(this, luckyForSome);
        Assert.assertEquals(Integer.valueOf(luckyForSome), m_wrapper);
    }

    /**
     * Test I can call the primitive method by reflection.
     * @throws Exception on error.
     */
    @Test
    public void testCallPrimitiveMethodByReflection() throws Exception
    {
        final int sidesOnADie = 6;
        Method theMethod = getClass().getMethod("setPrimitive", Integer.TYPE);
        MethodInjector<TestPrimitiveTypeInjection, Integer> injector = new MethodInjector<TestPrimitiveTypeInjection, Integer>(theMethod);
        injector.inject(this, sidesOnADie);
        Assert.assertEquals(sidesOnADie, m_primitive);
    }

    /**
     * Test I can call the wrapped method by reflection.
     * @throws Exception on error.
     */
    @Test
    public void testCallWrapperMethodByReflection() throws Exception
    {
        final int expectedValue = 12;
        Method theMethod = getClass().getMethod("setWrapper", Integer.class);
        MethodInjector<TestPrimitiveTypeInjection, Integer> injector = new MethodInjector<TestPrimitiveTypeInjection, Integer>(theMethod);
        injector.inject(this, expectedValue);
        Assert.assertEquals(Integer.valueOf(expectedValue), m_wrapper);
    }

}

package ice.bricks.reflection.tests;

import ice.bricks.reflection.FieldAccessException;
import ice.bricks.reflection.InstanceCreationException;
import ice.bricks.reflection.MethodCallException;
import ice.bricks.reflection.ReflectionUtils;
import ice.bricks.reflection.tests.fixtures.TestPojo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ReflectionUtilsTest {

    @Test
    void shouldGenerateNewInstance() {
        String result = ReflectionUtils.generateNewInstance(String.class);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldFailIfGenerationIsNotPossible() {
        assertThatExceptionOfType(InstanceCreationException.class)
                .isThrownBy(() -> ReflectionUtils.generateNewInstance(Runnable.class))
                .withMessage("Unable to generate new instance of 'Runnable'");
    }

    @Test
    void shouldGenerateNewInstanceWithParams() {
        Integer result = ReflectionUtils.generateNewInstance(
                Integer.class, new Class<?>[] {int.class}, new Object[] {42});
        assertThat(result).isEqualTo(42);
    }

    @Test
    void shouldFailIfGenerationWithParamsIsNotPossible() {
        assertThatExceptionOfType(InstanceCreationException.class)
                .isThrownBy(() -> ReflectionUtils.generateNewInstance(
                        Integer.class, new Class<?>[] {String.class}, new Object[] {"A"}))
                .withMessage("Unable to generate new instance of 'Integer'");
    }

    @Test
    void shouldReadFields() {
        TestPojo testObject = new TestPojo(42);
        assertThat(ReflectionUtils.<String>readField(testObject, "strValue")).isEqualTo("test");
        assertThat(ReflectionUtils.<Integer>readField(testObject, "intValue")).isEqualTo(42);
    }

    @Test
    void shouldFailIfFieldIsNotReadable() {
        TestPojo testObject = new TestPojo(42);
        assertThatExceptionOfType(FieldAccessException.class)
                .isThrownBy(() -> ReflectionUtils.<Long>readField(testObject, "longValue"))
                .withMessage("Unable to read field TestPojo#longValue");
    }

    @Test
    void shouldWriteFields() {
        TestPojo testObject = new TestPojo(41);
        ReflectionUtils.writeField(testObject, "strValue", "another test");
        ReflectionUtils.writeField(testObject, "intValue", 42);
        assertThat(ReflectionUtils.<String>readField(testObject, "strValue")).isEqualTo("another test");
        assertThat(ReflectionUtils.<Integer>readField(testObject, "intValue")).isEqualTo(42);
    }

    @Test
    void shouldFailIfFieldIsNotWritable() {
        TestPojo testObject = new TestPojo(42);
        assertThatExceptionOfType(FieldAccessException.class)
                .isThrownBy(() -> ReflectionUtils.writeField(testObject, "longValue", 123456789L))
                .withMessage("Unable to write field TestPojo#longValue");
    }

    @Test
    void shouldCallGetterMethod() {
        TestPojo testObject = new TestPojo(42);
        assertThat(ReflectionUtils.<String>invokeMethod(testObject, "getStrValue")).isEqualTo("test");
        assertThat(ReflectionUtils.<Integer>invokeMethod(testObject, "getIntValue")).isEqualTo(42);
    }

    @Test
    void shouldFailIfGetterMethodIsNotAccessible() {
        TestPojo testObject = new TestPojo(42);
        assertThatExceptionOfType(MethodCallException.class)
                .isThrownBy(() -> ReflectionUtils.<Long>invokeMethod(testObject, "getLongValue"))
                .withMessage("Unable to call TestPojo#getLongValue");
    }

    @Test
    void shouldCallSetterMethod() {
        TestPojo testObject = new TestPojo(41);
        ReflectionUtils.<String>invokeMethod(testObject, "setIntValue", new Class<?>[] {int.class}, new Object[]{42});
        assertThat(testObject.getIntValue()).isEqualTo(42);
    }

    @Test
    void shouldFailIfSetterMethodIsNotAccessible() {
        TestPojo testObject = new TestPojo(42);
        assertThatExceptionOfType(MethodCallException.class)
                .isThrownBy(() -> ReflectionUtils.<Long>invokeMethod(
                        testObject, "setLongValue", new Class<?>[] {long.class}, new Object[]{123456789L}))
                .withMessage("Unable to call TestPojo#setLongValue");
    }

    @Test
    void shouldGenerateNewCollectionInstance() {
        assertThat(ReflectionUtils.generateNewCollectionInstance(List.class)).isInstanceOf(ArrayList.class);
        assertThat(ReflectionUtils.generateNewCollectionInstance(Set.class)).isInstanceOf(HashSet.class);
        assertThat(ReflectionUtils.generateNewCollectionInstance(Queue.class)).isInstanceOf(LinkedList.class);
        assertThat(ReflectionUtils.generateNewCollectionInstance(Vector.class)).isInstanceOf(Vector.class);
        assertThat(ReflectionUtils.generateNewCollectionInstance(TreeSet.class)).isInstanceOf(TreeSet.class);
        assertThat(ReflectionUtils.generateNewCollectionInstance(PriorityQueue.class)).isInstanceOf(PriorityQueue.class);

        assertThatExceptionOfType(InstanceCreationException.class)
                .isThrownBy(() -> ReflectionUtils.generateNewCollectionInstance(Collection.class))
                .withMessage("Unable to generate new instance of 'Collection'");
    }

}

package ice.bricks.meta.tests;

import ice.bricks.meta.ClassUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassUtilsTest {

    @Test
    void shouldExtractClassName() {
        String className = ClassUtils.getClassName("ice.bricks.meta.tests.ClassUtilsTest");
        assertThat(className).isEqualTo("ClassUtilsTest");
    }

    @Test
    void shouldExtractPackageName() {
        String classPackage = ClassUtils.getClassPackage("ice.bricks.meta.tests.ClassUtilsTest");
        assertThat(classPackage).isEqualTo("ice.bricks.meta.tests");

        classPackage = ClassUtils.getClassPackage("ClassUtilsTest");
        assertThat(classPackage).isNull();
    }

    @Test
    void shouldLoadClassByName() {
        Class<?> clazz = ClassUtils.getClassByName("java.lang.Exception");
        assertThat(clazz).isEqualTo(java.lang.Exception.class);

        clazz = ClassUtils.getClassByName("java.lang.NonExistingClass");
        assertThat(clazz).isNull();
    }

}

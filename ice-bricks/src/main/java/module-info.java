module ice.bricks {
    requires jdk.compiler;
    requires lombok;
    requires org.jetbrains.annotations;
    requires org.apache.commons.lang3;

    exports ice.bricks.beans;
    exports ice.bricks.exceptions;
    exports ice.bricks.io;
    exports ice.bricks.lang.model;
    exports ice.bricks.meta;
    exports ice.bricks.objects;
    exports ice.bricks.reflection;
    exports ice.bricks.streams;
}

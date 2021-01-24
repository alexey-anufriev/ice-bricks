package ice.bricks.streams;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains streams related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamUtils {

    /**
     * Collects the stream of elements into a collection of a specified type.
     * Input/output types are checked.
     *
     * @param elements stream of elements
     * @param collectionType target collection type
     * @param <T> type of element
     * @return collection of a specified type with elements from the stream
     */
    public static <T> Collection<T> collectStream(Stream<T> elements, Class<?> collectionType) {
        return (Collection<T>) collectStreamUnsafe(elements, collectionType);
    }

    /**
     * Collects the stream of elements into a collection of a specified type.
     * Input/output types are not checked.
     *
     * @param elements stream of elements
     * @param collectionType target collection type
     * @return collection of a specified type with elements from the stream
     */
    public static Collection<?> collectStreamUnsafe(Stream<?> elements, Class<?> collectionType) {
        if (List.class.isAssignableFrom(collectionType)) {
            return elements.collect(Collectors.toList());
        }

        if (Set.class.isAssignableFrom(collectionType)) {
            return elements.collect(Collectors.toSet());
        }

        if (Queue.class.isAssignableFrom(collectionType)) {
            return elements.collect(Collectors.toCollection(PriorityQueue::new));
        }

        throw new IllegalArgumentException(collectionType.getCanonicalName() + " is not supported");
    }

}

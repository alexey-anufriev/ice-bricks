package ice.bricks.lang.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class TypeDetails {

    private final String typeName;
    private final String boxedTypeName;
    private final boolean isPrimitive;
    private final boolean isArray;
    private final boolean isAbstract;
    private final boolean isInterface;

    @Builder.Default
    private final List<TypeDetails> generics = Collections.emptyList();

    @Override
    public String toString() {
        if (!this.generics.isEmpty()) {
            String typeParameters = this.generics.stream()
                    .map(TypeDetails::toString)
                    .collect(Collectors.joining(", "));

            return this.typeName + "<" + typeParameters + ">";
        }
        else {
            return this.typeName;
        }
    }

    public String toBoxedString() {
        if (!this.generics.isEmpty()) {
            String typeParameters = this.generics.stream()
                    .map(TypeDetails::toBoxedString)
                    .collect(Collectors.joining(", "));

            return this.boxedTypeName + "<" + typeParameters + ">";
        }
        else {
            return this.boxedTypeName;
        }
    }

}

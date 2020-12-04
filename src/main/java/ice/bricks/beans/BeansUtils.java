package ice.bricks.beans;

import com.sun.tools.javac.code.Symbol;
import ice.bricks.lang.model.LanguageModelUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Types;

/**
 * Contains Java Beans related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BeansUtils {

    /**
     * Builds a method name that represents class field's getter according to the Java Beans naming convention.
     *
     * @param field class field
     * @return getter name
     */
    public static String getFieldGetterName(Element field) {
        String fieldName = StringUtils.capitalize(field.getSimpleName().toString());
        String methodName = "get" + fieldName;

        TypeKind typeKind = field.asType().getKind();
        if (typeKind.isPrimitive() && typeKind == TypeKind.BOOLEAN) {
            methodName = "is" + fieldName;
        }

        return methodName;
    }

    /**
     * Builds a method name that represents class field's setter according to the Java Beans naming convention.
     *
     * @param field class field
     * @return setter name
     */
    public static String getFieldSetterName(Element field) {
        String fieldName = StringUtils.capitalize(field.getSimpleName().toString());
        return "set" + fieldName;
    }

    /**
     * Checks that the method is named accordingly to the Java Beans naming convention
     * having as a basis related field's name.
     *
     * @param types instance of {@link Types}
     * @param fieldElement field to be checked
     * @param methodElement method to be checked
     * @return true if the method and the field are aligned in terms of naming, otherwise false
     */
    public static boolean checkPropertyNamingConvention(Types types, Element fieldElement, Element methodElement) {
        String fieldName = StringUtils.capitalize(fieldElement.getSimpleName().toString());

        Symbol.MethodSymbol method = (Symbol.MethodSymbol) methodElement;
        String methodName = method.getSimpleName().toString();

        boolean isBoolean = LanguageModelUtils.isBooleanType(types, method.getReturnType());
        boolean isBooleanGetter = methodName.equals("is" + fieldName);
        boolean isRegularGetter = methodName.equals("get" + fieldName);
        boolean hasNoParameters = method.getParameters().isEmpty();
        boolean hasSameReturnTypeAsField = fieldElement.asType().equals(method.getReturnType());

        if (((isBoolean && isBooleanGetter) || isRegularGetter) && hasNoParameters && hasSameReturnTypeAsField) {
            return true;
        }

        boolean isVoid = LanguageModelUtils.isVoidType(method.getReturnType());
        boolean isRegularSetter = methodName.equals("set" + fieldName);
        boolean hasSingleParameter = method.getParameters().size() == 1;
        boolean hasSameParameterTypeAsField = hasSingleParameter
                && fieldElement.asType().equals(method.getParameters().get(0).asType());

        if (isVoid && isRegularSetter && hasSameParameterTypeAsField) {
            return true;
        }

        return false;
    }

}

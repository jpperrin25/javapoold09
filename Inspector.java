
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Inspector<T> {
    private final Class<T> inspectedClass;

    public Inspector(Class<T> inspectedClass) {
        this.inspectedClass = inspectedClass;
    }

    public void displayInformations() {
        System.out.println("Information of the \""
            + inspectedClass.getName() + "\" class:");

        Class<?> superclass = inspectedClass.getSuperclass();
        System.out.println("Superclass: "
            + (superclass != null ? superclass.getName() : "None"));

        Method[] methods = inspectedClass.getDeclaredMethods();
        System.out.println(methods.length + " methods:");

        for (Method method : methods) {
            System.out.println("- " + method.getName());
        }

        Field[] fields = inspectedClass.getDeclaredFields();
        System.out.println(fields.length + " fields:");

        for (Field field : fields) {
            System.out.println("- " + field.getName());
        }
    }

    public T createInstance()
            throws InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException,
                   NoSuchMethodException {
        return inspectedClass.getDeclaredConstructor().newInstance();
    }
}

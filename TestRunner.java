import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class TestRunner {

    public void runTests(Class<?> testClass) {
        Object testInstance;

        try {
            testInstance = testClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            throw new RuntimeException(
                "Impossible de creer une instance de la classe de test", e);
        }

        for (Method method : testClass.getDeclaredMethods()) {
            Test annotation = method.getAnnotation(Test.class);

            if (annotation == null || !annotation.enabled()) {
                continue;
            }

            System.out.println("Running test: " + annotation.name());

            try {
                method.setAccessible(true);
                method.invoke(testInstance);
            } catch (IllegalAccessException
                    | InvocationTargetException e) {
                throw new RuntimeException(
                    "Echec du test : " + annotation.name(), e);
            }
        }
    }
}

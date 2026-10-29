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

        try {
            invokeLifecycle(testClass, testInstance, BeforeClass.class);

            for (Method method : testClass.getDeclaredMethods()) {
                Test annotation = method.getAnnotation(Test.class);

                if (annotation == null || !annotation.enabled()) {
                    continue;
                }

                System.out.println(annotation.name());

                try {
                    invokeLifecycle(testClass, testInstance, Before.class);

                    method.setAccessible(true);
                    method.invoke(testInstance);

                } catch (IllegalAccessException
                        | InvocationTargetException e) {
                    throw new RuntimeException(
                        "Echec du test : " + annotation.name(), e);
                } finally {
                    invokeLifecycle(testClass, testInstance, After.class);
                }
            }
        } finally {
            invokeLifecycle(testClass, testInstance, AfterClass.class);
        }
    }

    private void invokeLifecycle(
            Class<?> testClass,
            Object testInstance,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {

        for (Method method : testClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(annotationClass)) {
                continue;
            }

            try {
                method.setAccessible(true);
                method.invoke(testInstance);
            } catch (IllegalAccessException
                    | InvocationTargetException e) {
                throw new RuntimeException(
                    "Erreur pendant l'execution de "
                    + annotationClass.getSimpleName(), e);
            }

            // Le sujet prévoit une méthode de chaque type, si elle existe.
            return;
        }
    }
}

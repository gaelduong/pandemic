package shared;

public class Utils {

    /**
     * Gets the corresponding enum given the string name
     */
    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String name){
        try {
            return Enum.valueOf(enumClass, name);
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

}

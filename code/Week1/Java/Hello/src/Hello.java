/**
 * Created by Steve Dame on 2/20/2014.
 * This is a duck simple example of how to get started with IDE based Java programming.
 */
public class Hello {

    /**
     * The singular private variable to hold the name to be addressed
     */
    private String myName;

    /**
     * Instantiate the private variable for the Hello object
     * @param name
     */
    Hello( String name ) { myName = name; }

    /**
     * Speak is the only way for Hello to communicate.
     */
    public void speak()
    {
        System.out.println("Hi! My name is " + myName);
     }
}

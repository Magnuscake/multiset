import implementation.*;

/**
 * Class to create appropriate multiset data structure.
 * Follows (parameterised) Factory design pattern.
 *
 * @author Jeffrey Chan & Yongli Ren, RMIT 2020
 */
public class MultisetImplCreator
{
    /** Implementation type that this factory creates. */
    protected String mImplementationType;


    /**
     * Constructor for factory.
     *
     * @param implementationType String of implementation to create.
     */
    public MultisetImplCreator(String implementationType)
        throws IllegalArgumentException
    {
        if (implementationType.compareTo("array") != 0 &&
            implementationType.compareTo("orderedlinkedlist") != 0 &&
            implementationType.compareTo("bst") != 0 &&
            implementationType.compareTo("duallinkedlist") != 0
            ) {
                throw new IllegalArgumentException("Unknown implementation type.");
        }
        mImplementationType = implementationType;
    } // end of MultisetImplCreator()


    /**
     * Create an object/instance of the appropriate implementation.
     * If creation failed, will return null.
     *
     * @return Object/instance of implementation, upcasted to RmitMultiset.
     */
    public RmitMultiset createImplementation() {
        RmitMultiset multiset = null;
        switch (mImplementationType) {
            case "array":
                multiset = new ArrayMultiset();
                break;
 			case "orderedlinkedlist":
     		    multiset = new OrderedLinkedListMultiset();
     			break;
     		case "bst":
     		    multiset = new BstMultiset();
     		    break;
            case "duallinkedlist":
              	multiset = new DualLinkedListMultiset();
               	break;
         } // end of switch

         // if fall out of switch statement above, means unknown implementation type
         // and null will be returned.
         return multiset;
     } // end of createImplementation()

 } // end of class MultisetCreator

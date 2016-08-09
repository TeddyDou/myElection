package FrontEnd_CORBA;

/**
 * Holder class for : Front_End
 * 
 * @author OpenORB Compiler
 */
final public class Front_EndHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal Front_End value
     */
    public FrontEnd_CORBA.Front_End value;

    /**
     * Default constructor
     */
    public Front_EndHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public Front_EndHolder(FrontEnd_CORBA.Front_End initial)
    {
        value = initial;
    }

    /**
     * Read Front_End from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = Front_EndHelper.read(istream);
    }

    /**
     * Write Front_End into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        Front_EndHelper.write(ostream,value);
    }

    /**
     * Return the Front_End TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return Front_EndHelper.type();
    }

}

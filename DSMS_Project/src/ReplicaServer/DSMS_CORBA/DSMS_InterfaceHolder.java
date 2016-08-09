package ReplicaServer.DSMS_CORBA;

/**
 * Holder class for : DSMS_Interface
 * 
 * @author OpenORB Compiler
 */
final public class DSMS_InterfaceHolder
        implements org.omg.CORBA.portable.Streamable
{
    /**
     * Internal DSMS_Interface value
     */
    public ReplicaServer.DSMS_CORBA.DSMS_Interface value;

    /**
     * Default constructor
     */
    public DSMS_InterfaceHolder()
    { }

    /**
     * Constructor with value initialisation
     * @param initial the initial value
     */
    public DSMS_InterfaceHolder(ReplicaServer.DSMS_CORBA.DSMS_Interface initial)
    {
        value = initial;
    }

    /**
     * Read DSMS_Interface from a marshalled stream
     * @param istream the input stream
     */
    public void _read(org.omg.CORBA.portable.InputStream istream)
    {
        value = DSMS_InterfaceHelper.read(istream);
    }

    /**
     * Write DSMS_Interface into a marshalled stream
     * @param ostream the output stream
     */
    public void _write(org.omg.CORBA.portable.OutputStream ostream)
    {
        DSMS_InterfaceHelper.write(ostream,value);
    }

    /**
     * Return the DSMS_Interface TypeCode
     * @return a TypeCode
     */
    public org.omg.CORBA.TypeCode _type()
    {
        return DSMS_InterfaceHelper.type();
    }

}

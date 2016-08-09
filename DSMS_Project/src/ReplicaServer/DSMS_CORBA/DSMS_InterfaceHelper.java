package ReplicaServer.DSMS_CORBA;

/** 
 * Helper class for : DSMS_Interface
 *  
 * @author OpenORB Compiler
 */ 
public class DSMS_InterfaceHelper
{
    /**
     * Insert DSMS_Interface into an any
     * @param a an any
     * @param t DSMS_Interface value
     */
    public static void insert(org.omg.CORBA.Any a, ReplicaServer.DSMS_CORBA.DSMS_Interface t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract DSMS_Interface from an any
     *
     * @param a an any
     * @return the extracted DSMS_Interface value
     */
    public static ReplicaServer.DSMS_CORBA.DSMS_Interface extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return ReplicaServer.DSMS_CORBA.DSMS_InterfaceHelper.narrow( a.extract_Object() );
        }
        catch ( final org.omg.CORBA.BAD_PARAM e )
        {
            throw new org.omg.CORBA.MARSHAL(e.getMessage());
        }
    }

    //
    // Internal TypeCode value
    //
    private static org.omg.CORBA.TypeCode _tc = null;

    /**
     * Return the DSMS_Interface TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "DSMS_Interface" );
        }
        return _tc;
    }

    /**
     * Return the DSMS_Interface IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:DSMS_CORBA/DSMS_Interface:1.0";

    /**
     * Read DSMS_Interface from a marshalled stream
     * @param istream the input stream
     * @return the readed DSMS_Interface value
     */
    public static ReplicaServer.DSMS_CORBA.DSMS_Interface read(org.omg.CORBA.portable.InputStream istream)
    {
        return(ReplicaServer.DSMS_CORBA.DSMS_Interface)istream.read_Object(ReplicaServer.DSMS_CORBA._DSMS_InterfaceStub.class);
    }

    /**
     * Write DSMS_Interface into a marshalled stream
     * @param ostream the output stream
     * @param value DSMS_Interface value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, ReplicaServer.DSMS_CORBA.DSMS_Interface value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to DSMS_Interface
     * @param obj the CORBA Object
     * @return DSMS_Interface Object
     */
    public static DSMS_Interface narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof DSMS_Interface)
            return (DSMS_Interface)obj;

        if (obj._is_a(id()))
        {
            _DSMS_InterfaceStub stub = new _DSMS_InterfaceStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to DSMS_Interface
     * @param obj the CORBA Object
     * @return DSMS_Interface Object
     */
    public static DSMS_Interface unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof DSMS_Interface)
            return (DSMS_Interface)obj;

        _DSMS_InterfaceStub stub = new _DSMS_InterfaceStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}

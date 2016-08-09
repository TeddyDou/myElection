package FrontEnd_CORBA;

/** 
 * Helper class for : Front_End
 *  
 * @author OpenORB Compiler
 */ 
public class Front_EndHelper
{
    /**
     * Insert Front_End into an any
     * @param a an any
     * @param t Front_End value
     */
    public static void insert(org.omg.CORBA.Any a, FrontEnd_CORBA.Front_End t)
    {
        a.insert_Object(t , type());
    }

    /**
     * Extract Front_End from an any
     *
     * @param a an any
     * @return the extracted Front_End value
     */
    public static FrontEnd_CORBA.Front_End extract( org.omg.CORBA.Any a )
    {
        if ( !a.type().equivalent( type() ) )
        {
            throw new org.omg.CORBA.MARSHAL();
        }
        try
        {
            return FrontEnd_CORBA.Front_EndHelper.narrow( a.extract_Object() );
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
     * Return the Front_End TypeCode
     * @return a TypeCode
     */
    public static org.omg.CORBA.TypeCode type()
    {
        if (_tc == null) {
            org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init();
            _tc = orb.create_interface_tc( id(), "Front_End" );
        }
        return _tc;
    }

    /**
     * Return the Front_End IDL ID
     * @return an ID
     */
    public static String id()
    {
        return _id;
    }

    private final static String _id = "IDL:FrontEnd_CORBA/Front_End:1.0";

    /**
     * Read Front_End from a marshalled stream
     * @param istream the input stream
     * @return the readed Front_End value
     */
    public static FrontEnd_CORBA.Front_End read(org.omg.CORBA.portable.InputStream istream)
    {
        return(FrontEnd_CORBA.Front_End)istream.read_Object(FrontEnd_CORBA._Front_EndStub.class);
    }

    /**
     * Write Front_End into a marshalled stream
     * @param ostream the output stream
     * @param value Front_End value
     */
    public static void write(org.omg.CORBA.portable.OutputStream ostream, FrontEnd_CORBA.Front_End value)
    {
        ostream.write_Object((org.omg.CORBA.portable.ObjectImpl)value);
    }

    /**
     * Narrow CORBA::Object to Front_End
     * @param obj the CORBA Object
     * @return Front_End Object
     */
    public static Front_End narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Front_End)
            return (Front_End)obj;

        if (obj._is_a(id()))
        {
            _Front_EndStub stub = new _Front_EndStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
            return stub;
        }

        throw new org.omg.CORBA.BAD_PARAM();
    }

    /**
     * Unchecked Narrow CORBA::Object to Front_End
     * @param obj the CORBA Object
     * @return Front_End Object
     */
    public static Front_End unchecked_narrow(org.omg.CORBA.Object obj)
    {
        if (obj == null)
            return null;
        if (obj instanceof Front_End)
            return (Front_End)obj;

        _Front_EndStub stub = new _Front_EndStub();
        stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
        return stub;

    }

}

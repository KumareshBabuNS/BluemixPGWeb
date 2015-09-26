package com.ibm.pas.bluemix.pgweb.main;

public class BluemixPGWebException extends Exception
{
    public BluemixPGWebException()
    {
    }

    public BluemixPGWebException(final Throwable cause)
    {
        super(cause);
    }

    public BluemixPGWebException
            (final String msg,
             final Throwable cause)
    {
        super(msg, cause);
    }

    public BluemixPGWebException(final String msg)
    {
        super(msg);
    }
}

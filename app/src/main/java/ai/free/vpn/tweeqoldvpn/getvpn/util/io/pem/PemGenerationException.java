

package ai.free.vpn.tweeqoldvpn.getvpn.util.io.pem;

import java.io.IOException;

@SuppressWarnings("serial")
public class PemGenerationException
    extends IOException
{
    private Throwable cause;

    public PemGenerationException(String message, Throwable cause)
    {
        super(message);
        this.cause = cause;
    }

    public PemGenerationException(String message)
    {
        super(message);
    }

    public Throwable getCause()
    {
        return cause;
    }
}

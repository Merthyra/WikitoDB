package at.ac.tuwien.docspars.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 
 * @author Hannes
 * ByteArrayWrapper to compare byte arrays
 * intended to be used as key in a map data structure to save storage compared to conventional strings
 */

public final class ASCIIString2ByteArrayWrapper
{
    private final byte[] data;

    public ASCIIString2ByteArrayWrapper(String data)
    {
        if (data == null)
        {
            throw new NullPointerException();
        }
        this.data = data.getBytes(StandardCharsets.US_ASCII);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof ASCIIString2ByteArrayWrapper))
        {
            return false;
        }
        return Arrays.equals(data, ((ASCIIString2ByteArrayWrapper)other).data);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(data);
    }

    @Override
    public String toString() {
    	return new String(data, StandardCharsets.US_ASCII);
    }
}

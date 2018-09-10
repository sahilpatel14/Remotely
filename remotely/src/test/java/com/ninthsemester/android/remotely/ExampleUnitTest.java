package com.ninthsemester.android.remotely;

import org.junit.Test;

import static org.junit.Assert.*;
import com.ninthsemester.android.remotely.extras.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void validateBaseUrl_passValidUrl_returnsTrue() {
        Boolean response = ExtensionsKt.isValidUrl("http://www.awesomeness.com");
        assertEquals(response, true);
    }

    @Test
    public void validateBaseUrl_passValidSSLUrl_returnsTrue() {
        Boolean response = ExtensionsKt.isValidUrl("https://www.awesomeness.com");
        assertEquals(response, true);
    }

    @Test
    public void validateBaseUrl_passRouteWithBaseUrl_returnsTrue() {
        Boolean response = ExtensionsKt.isValidUrl("http://www.awesomeness.com/pink/panda/party");
        assertEquals(response, true);
    }

    @Test
    public void validateBaseUrl_invalidScheme_returnsFalse() {
        Boolean response = ExtensionsKt.isValidUrl("htt://www.awesomeness.com/");
        assertEquals(response, false);
    }


    @Test
    public void validateBaseUrl_invalidUrl_returnsFalse() {
        Boolean response = ExtensionsKt.isValidUrl("awesomeness.com/");
        assertEquals(response, false);
    }
}
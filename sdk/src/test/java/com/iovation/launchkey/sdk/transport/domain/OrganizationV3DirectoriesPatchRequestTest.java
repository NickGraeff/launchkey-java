package com.iovation.launchkey.sdk.transport.domain; /**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.net.URI;
import java.util.UUID;

import static org.junit.Assert.*;

public class OrganizationV3DirectoriesPatchRequestTest {
    @Test
    public void getDirectoryId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new OrganizationV3DirectoriesPatchRequest(id, false, null, null, null, null).getDirectoryId());
    }

    @Test
    public void isActive() throws Exception {
        assertTrue(new OrganizationV3DirectoriesPatchRequest(null, true, null, null, null, null).isActive());
    }

    @Test
    public void getAndroidKey() throws Exception {
        assertEquals("AK", new OrganizationV3DirectoriesPatchRequest(null, false, "AK", null, null, null).getAndroidKey());
    }

    @Test
    public void getIosP12() throws Exception {
        assertEquals("p12", new OrganizationV3DirectoriesPatchRequest(null, false, null, "p12", null, null).getIosP12());
    }

    @Test
    public void isDenialContextInquiryEnabled() throws Exception {
        assertTrue(new OrganizationV3DirectoriesPatchRequest(null, false, null, null, true, null).isDenialContextInquiryEnabled());
    }

    @Test
    public void getCallbackUrl() throws Exception {
        URI uri = URI.create("https://api.launchkey.com");
        assertEquals(uri, new OrganizationV3DirectoriesPatchRequest(null, false, null, null, null, uri).getWebhookUrl());
    }

    @Test
    public void toJSONWithNullActiveAndDenialContextInquiryEnabledDoesNotSend() throws Exception {
        String expected = "{\"directory_id\":\"67c87654-aed9-11e7-98e9-0469f8dc10a5\"," +
                "\"android_key\":\"ak\",\"ios_p12\":\"p12\"," +
                "\"webhook_url\":\"https://api.launchkey.com\"}";
        String actual = new ObjectMapper().writeValueAsString(new OrganizationV3DirectoriesPatchRequest(
                UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5"), null, "ak", "p12", null,
                URI.create("https://api.launchkey.com")));
        assertEquals(expected, actual);
    }

    @Test
    public void toJSONAllValues() throws Exception {
        String expected = "{\"directory_id\":\"67c87654-aed9-11e7-98e9-0469f8dc10a5\"," +
                "\"active\":true,\"android_key\":\"ak\",\"ios_p12\":\"p12\"," +
                "\"denial_context_inquiry_enabled\":true,\"webhook_url\":\"https://api.launchkey.com\"}";
        String actual = new ObjectMapper().writeValueAsString(new OrganizationV3DirectoriesPatchRequest(
                UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5"), true, "ak", "p12", true,
                URI.create("https://api.launchkey.com")));
        assertEquals(expected, actual);
    }
}
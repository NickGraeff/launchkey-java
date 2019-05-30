package com.iovation.launchkey.sdk.transport.apachehttp; /**
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

import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.KeysPostResponse;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysPostRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportOrganizationV3DirectoryKeysPostTest
        extends ApacheHttpTransportTestBase {

    @Mock
    private OrganizationV3DirectoryKeysPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;


    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3DirectoryKeysPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/organization/v3/directory/keys")));
    }

    @Test
    public void marshalsExpectedData() throws Exception {
        transport.organizationV3DirectoryKeysPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.organizationV3DirectoryKeysPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        KeysPostResponse expected = mock(KeysPostResponse.class);
        when(objectMapper.readValue(anyString(), eq(KeysPostResponse.class))).thenReturn(expected);
        KeysPostResponse actual = transport.organizationV3DirectoryKeysPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(KeysPostResponse.class));
        assertEquals(expected, actual);
    }
}
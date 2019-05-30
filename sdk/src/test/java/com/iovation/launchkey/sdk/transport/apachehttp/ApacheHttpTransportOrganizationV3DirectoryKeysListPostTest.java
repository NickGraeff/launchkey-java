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
import com.iovation.launchkey.sdk.transport.domain.KeysListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.KeysListPostResponsePublicKey;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysListPostRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportOrganizationV3DirectoryKeysListPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private OrganizationV3DirectoryKeysListPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;
    private KeysListPostResponsePublicKey[] result;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        result = new KeysListPostResponsePublicKey[]{mock(KeysListPostResponsePublicKey.class)};
        when(objectMapper.readValue(anyString(), eq(KeysListPostResponsePublicKey[].class))).thenReturn(result);
    }

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3DirectoryKeysListPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/organization/v3/directory/keys/list")));
    }

    @Test
    public void marshalsExpectedRequestData() throws Exception {
        transport.organizationV3DirectoryKeysListPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.organizationV3DirectoryKeysListPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.organizationV3DirectoryKeysListPost(request, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void responseIsParsedProperlyAndReturned() throws Exception {
        when(objectMapper.readValue(anyString(), eq(KeysListPostResponsePublicKey[].class))).thenReturn(result);
        KeysListPostResponse expected = new KeysListPostResponse(Arrays.asList(result));
        KeysListPostResponse actual =
                transport.organizationV3DirectoryKeysListPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(KeysListPostResponsePublicKey[].class));
        assertEquals(expected, actual);
    }

}
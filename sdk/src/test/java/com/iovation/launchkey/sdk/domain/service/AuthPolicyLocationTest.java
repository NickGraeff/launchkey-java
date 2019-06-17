package com.iovation.launchkey.sdk.domain.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AuthPolicyLocationTest {
    private AuthPolicy.Location location;

    @Before
    public void setUp() throws Exception {
        location = new AuthPolicy.Location(100.1, 2.2, 3.3);
    }

    @After
    public void tearDown() throws Exception {
        location = null;
    }

    @Test
    public void getLongitude() throws Exception {
        assertEquals(3.3, location.getLongitude(), 0.001);
    }

    @Test
    public void getLatitude() throws Exception {
        assertEquals(2.2, location.getLatitude(), 0.001);
    }

    @Test
    public void getRadius() throws Exception {
        assertEquals(100.1, location.getRadius(), 0.001);
    }

    @Test
    public void toStringHasClassName() throws Exception {
        assertThat(location.toString(), containsString(location.getClass().getSimpleName()));
    }
}

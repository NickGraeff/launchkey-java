/**
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

package com.iovation.launchkey.sdk.integration.steps;

import com.iovation.launchkey.sdk.domain.service.AuthPolicy;
import com.iovation.launchkey.sdk.error.InvalidPolicyInput;
import com.iovation.launchkey.sdk.integration.managers.DirectoryDeviceManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceAuthsManager;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ServiceAuthorizationPolicySteps {

    private final DirectoryDeviceManager directoryDeviceManager;
    private final DirectoryServiceAuthsManager directoryServiceAuthsManager;

    @Inject
    public ServiceAuthorizationPolicySteps(DirectoryDeviceManager directoryDeviceManager, DirectoryServiceAuthsManager directoryServiceAuthsManager) {
        this.directoryDeviceManager = directoryDeviceManager;
        this.directoryServiceAuthsManager = directoryServiceAuthsManager;
    }

    @Given("^the current Authorization Policy requires (\\d+) factors$")
    public void theCurrentAuthorizationPolicyRequiresFactors(int numberOfFactors) {
        directoryServiceAuthsManager.setFactors(numberOfFactors);
    }

    @Given("^the current Authorization Policy requires inherence$")
    public void theCurrentAuthorizationPolicyRequiresInherence() {
        directoryServiceAuthsManager.setInherence();
    }

    @And("^the current Authorization Policy requires knowledge$")
    public void theCurrentAuthorizationPolicyRequiresKnowledge() {
        directoryServiceAuthsManager.setKnowledge();
    }

    @And("^the current Authorization Policy requires possession$")
    public void theCurrentAuthorizationPolicyRequiresPossession() {
        directoryServiceAuthsManager.setPossession();
    }

    @When("^I make a Policy based Authorization request for the User$")
    public void iMakeAPolicyBasedAuthorizationRequestForTheUser() throws Throwable {
        directoryServiceAuthsManager.createAuthorizationRequest(directoryDeviceManager.getCurrentUserIdentifier(), null, directoryServiceAuthsManager.getCurrentAuthPolicy());
    }

    @Given("the current Authorization Policy requires a geofence with a radius of {double}, a latitude of {double}, and a longitude of {double}")
    public void theCurrentAuthorizationPolicyRequiresAGeofenceOf(double radius, double latitude, double longitude) throws InvalidPolicyInput {
        directoryServiceAuthsManager.addLocation(new AuthPolicy.Location(radius, latitude, longitude));
    }

    @Given("the current Authorization Policy requires a geofence with a radius of {double}, a latitude of {double}, a longitude of {double}, and named {string}")
    public void theCurrentAuthorizationPolicyRequiresAGeofenceOfName(double radius, double latitude, double longitude, String name) throws InvalidPolicyInput {
        directoryServiceAuthsManager.addLocation(new AuthPolicy.Location(name, radius, latitude, longitude));
    }
}

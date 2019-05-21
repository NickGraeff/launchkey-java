/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.domain.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Object that represents the current state of an service request within the Platform.
 */
public class AuthorizationResponse {
    /**
     * Unique identifier for the authorization request generated by the LaunchKey API.
     */
    private final String authorizationRequestId;

    /**
     * Has the request been authorized by the user.
     */
    private final boolean authorized;

    /**
     * Hashed user identifier to track a specific user for an application. This value will be used by the
     * Logout Callback to identify the user logging out. This value may
     * be used in place of a username or White Label User identifier for authorization requests.
     */
    private final String serviceUserHash;

    /**
     * A string that uniquely identifies the user across the entire Organization to which the Application whose Application Key
     * was included in the request belongs. This will be returned if, and only if, the Application belongs to an
     * Organization.
     */
    private final String organizationUserHash;

    /**
     * A value uniquely and permanently identifying the User associated with the authorization request within the Application whose
     * Application Key was included in the requestServerSentEventAuthorizationResponseCore belongs.
     */
    private final String userPushId;

    /**
     * Identifier that is unique for a single user identifying the device the user utilized to respond to the
     * authorization request.
     */
    private final String deviceId;

    private final List<String> servicePins;

    /**
     *
     */
    private final Type type;
    private final Reason reason;
    private final String denialReason;
    private final Boolean fraud;
    private final AuthPolicy policy;
    private final List<AuthMethod> authMethods;

    /**
     * @param authorizationRequestId Unique identifier for the authorization request generated by the LaunchKey API.
     * @param authorized The users response to the authorization request.  True and False are authorized and declined
     * respectively.
     * @param serviceUserHash Hashed identifier of the User that responded to the authentication request. This
     * value will be used by the De-Orbit Callback to identify the de-orbiting User. This value may
     * be used in place of a username or White Label User identifier for authorization requests.
     * @param organizationUserHash Unique identifier of the User that responded to the authentication request within the
     * organization in which the Application for this request resides.  This will be returned if,
     * and only if, the Application belongs to an Organization.
     * @param userPushId Unique identifier of the User that responded to the authorization request
     * within the Application to which this authorization request was made.
     * @param deviceId Identifier for the device the User utilized to respond to the authorization request.
     * @param servicePins List of random values returned
     * @param type Authorization response type
     * @param reason Authorization response reason
     * @param denialReason ID for {@link DenialReason} in list provided to
     *                      {@link com.iovation.launchkey.sdk.client.ServiceClient#createAuthorizationRequest(String, String, AuthPolicy, String, Integer, String, String, List)}
     *                      which was selected by the user when denying the request.
     * @param fraud Was the authorization request determined to be fraudulent?
     * @param policy Policy utilized by the device at the time of authorization
     * @param authMethods List of the auth methods and the state of each at the time of authorization
     */
    public AuthorizationResponse(
            String authorizationRequestId,
            boolean authorized,
            String serviceUserHash,
            String organizationUserHash,
            String userPushId,
            String deviceId,
            List<String> servicePins,
            Type type,
            Reason reason,
            String denialReason,
            Boolean fraud,
            AuthPolicy policy,
            List<AuthMethod> authMethods) {
        this.authorizationRequestId = authorizationRequestId;
        this.authorized = authorized;
        this.serviceUserHash = serviceUserHash;
        this.organizationUserHash = organizationUserHash;
        this.userPushId = userPushId;
        this.deviceId = deviceId;
        this.servicePins = servicePins;
        this.type = type;
        this.reason = reason;
        this.denialReason = denialReason;
        this.fraud = fraud;
        this.policy = policy;
        this.authMethods = authMethods;
    }

    /**
     * @param authorizationRequestId Unique identifier for the authorization request generated by the LaunchKey API.
     * @param authorized The users response to the authorization request.  True and False are authorized and declined
     * respectively.
     * @param serviceUserHash Hashed identifier of the User that responded to the authentication request. This
     * value will be used by the De-Orbit Callback to identify the de-orbiting User. This value may
     * be used in place of a username or White Label User identifier for authorization requests.
     * @param organizationUserHash Unique identifier of the User that responded to the authentication request within the
     * organization in which the Application for this request resides.  This will be returned if,
     * and only if, the Application belongs to an Organization.
     * @param userPushId Unique identifier of the User that responded to the authorization request
     * within the Application to which this authorization request was made.
     * @param deviceId Identifier for the device the User utilized to respond to the authorization request.
     * @param servicePins List of random values returned
     */
    public AuthorizationResponse(
            String authorizationRequestId,
            boolean authorized,
            String serviceUserHash,
            String organizationUserHash,
            String userPushId,
            String deviceId,
            List<String> servicePins) {
        this(authorizationRequestId, authorized, serviceUserHash, organizationUserHash, userPushId, deviceId,
                servicePins, null, null, null, null, null, null);
    }

    /**
     * Get the unique identifier for the authentication request for which this is the response.
     *
     * @return Authentication request ID
     */
    public String getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    /**
     * Get the users response to the service request.  True and False are authorized and declined respectively.
     *
     * @return Authorized response value
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * Hashed identifier of the User that responded to the authentication request. This value will be used by the
     * De-Orbit Callback to identify the de-orbiting User.
     *
     * @return User hash
     */
    public String getServiceUserHash() {
        return serviceUserHash;
    }

    /**
     * Get the unique identifier of the User that responded to the authentication request within the organization in
     * which the Application for this request resides.  This will be returned if, and only if, the Application belongs to an
     * Organization.
     *
     * @return Organization-User ID
     */
    public String getOrganizationUserHash() {
        return organizationUserHash;
    }

    /**
     * Get the unique identifier of the User that responded to the authentication request within the Application to
     * which this authorization request was made. This value may be used in place of a username or White Label
     * User identifier for authorization requests.
     * .
     *
     * @return User push ID
     */
    public String getUserPushId() {
        return userPushId;
    }

    /**
     * Get the identifier for the device the User utilized to respond to the authentication request. This
     * value is unique to the User that responded to the authentication request and may be duplicated in another User.
     *
     * @return Device ID
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * A list of up to 5 random value strings that are unique for a Device and Service combination. The list is
     * intended for Device validation at the Service. Devices will rotate out Service pins as a queue, first in -
     * first out (FIFO). As such, they are rotating shared secrets known only to the device and the Service. Service
     * pins can be used to protect against a myriad of potential attacks. However, they do run the risk of devices
     * getting "out of sync" and resulting in devices not being able to authenticate. If you implement service pins
     * in your solutions, you will need to build in a recovery mechanism to reset the known service pins and re-sync
     * the device.
     *
     * @return List of Device Pins
     */
    public List<String> getServicePins() {
        return servicePins;
    }

    /**
     * Get the type of response
     * @return Type of response
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the reason for the response
     * @return Reason for the response
     */
    public Reason getReason() {
        return reason;
    }

    /**
     * Get the ID for {@link DenialReason} in list provided to
     * {@link com.iovation.launchkey.sdk.client.ServiceClient#createAuthorizationRequest(String, String, AuthPolicy, String, Integer, String, String, List)}
     * which was selected by the user when denying the request.
     * @return Denial reason ID
     */
    public String getDenialReason() {
        return denialReason;
    }

    /**
     * Was the authorization request determined to be fraudulent?
     * @return Fraud flag
     */
    public Boolean isFraud() {
        return fraud;
    }

    /**
     * Get the policy used by the device that responded to the request
     * @return Auth policy
     */
    public AuthPolicy getPolicy() {
        return policy;
    }

    /**
     * Get the state of the devices auth methods at the time of the response
     * @return List of auth methods and their state
     */
    public List<AuthMethod> getAuthMethods() {
        return authMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizationResponse)) return false;
        AuthorizationResponse that = (AuthorizationResponse) o;
        return isAuthorized() == that.isAuthorized() &&
                Objects.equals(getAuthorizationRequestId(), that.getAuthorizationRequestId()) &&
                Objects.equals(getServiceUserHash(), that.getServiceUserHash()) &&
                Objects.equals(getOrganizationUserHash(), that.getOrganizationUserHash()) &&
                Objects.equals(getUserPushId(), that.getUserPushId()) &&
                Objects.equals(getDeviceId(), that.getDeviceId()) &&
                Objects.equals(getServicePins(), that.getServicePins()) &&
                getType() == that.getType() &&
                getReason() == that.getReason() &&
                Objects.equals(getDenialReason(), that.getDenialReason()) &&
                Objects.equals(isFraud(), that.isFraud()) &&
                Objects.equals(getPolicy(), that.getPolicy()) &&
                Objects.equals(getAuthMethods(), that.getAuthMethods());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthorizationRequestId(), isAuthorized(), getServiceUserHash(),
                getOrganizationUserHash(), getUserPushId(), getDeviceId(), getServicePins(), getType(), getReason(),
                getDenialReason(), isFraud(), getPolicy(), getAuthMethods());
    }

    @Override
    public String toString() {
        return "AuthorizationResponse{" +
                "authorizationRequestId='" + authorizationRequestId + '\'' +
                ", authorized=" + authorized +
                ", serviceUserHash='" + serviceUserHash + '\'' +
                ", organizationUserHash='" + organizationUserHash + '\'' +
                ", userPushId='" + userPushId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", servicePins=" + servicePins +
                ", type=" + type +
                ", reason=" + reason +
                ", denialReason='" + denialReason + '\'' +
                ", fraud=" + fraud +
                ", policy=" + policy +
                ", authMethods=" + authMethods +
                '}';
    }

    /**
     * The response type. This is a general categorization of the response.
     */
    public enum Type {
        /**
         * The user authorized the authorization request
         */
        AUTHORIZED,

        /**
         * The use denied the authorization request
         */
        DENIED,

        /**
         * The user failed to complete the authentication request
         */
        FAILED,

        /**
         * Other exists only to allow for forward compatibility to future response types
         */
        OTHER
    }

    /**
     * The response
     */
    public enum Reason {
        /**
         * User satisfies all request policy requirements; successfully authenticates with all applicable methods
         */
        APPROVED,

        /**
         * User satisfies all request policy requirements; chooses to deny request rather than proceed with
         * authentication
         */
        DISAPPROVED,

        /**
         * User satisfies all request policy requirements; chooses to deny request because they believe it to be
         * fraudulent in some manner
         */
        FRAUDULENT,

        /**
         * Authenticator fails to satisfy request policy; authentication not allowed
         */
        POLICY,

        /**
         * Authenticator satisfies all request policy requirements, but permission on device prevents auth method
         * verification
         */
        PERMISSION,

        /**
         * Authenticator satisfies all request policy requirements, but user fails to successfully authenticate with
         * all required authentication methods
         */
        AUTHENTICATION,

        /**
         * Authenticator fails to satisfy request policy because authenticator configuration is incompatible with the
         * request policy (i.e. requiring an auth method that is configured to be unavailable to end users)
         */
        CONFIGURATION,

        /**
         * User can't receive or respond to request because a Local Auth Request is pending authorization
         */
        BUSY_LOCAL,

        /**
         * Other exists only to allow for forward compatibility to future response reasons
         */
        OTHER
    }


}

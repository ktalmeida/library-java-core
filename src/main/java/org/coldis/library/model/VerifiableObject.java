package org.coldis.library.model;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.coldis.library.helper.ReflectionHelper;

/**
 * Object that can be verified.
 */
public interface VerifiableObject {

	/**
	 * Returns the verification.
	 *
	 * @return The verification.
	 */
	Verification getVerification();

	/**
	 * Returns the verification status.
	 *
	 * @return The verification status.
	 */
	VerificationStatus getVerificationStatus();

	/**
	 * Gets the attributes that should be verified for the object to be verified.
	 *
	 * @param  verifiableObject The verifiable object.
	 * @return                  The attributes that should be verified for the
	 *                          object to be verified.
	 */
	static Set<String> getVerifiableAttributes(final VerifiableObject verifiableObject) {
		// Set of verifiable attributes.
		final Set<String> verifiableAttributes = new HashSet<>();
		// If the verifiable object is given.
		if (verifiableObject != null) {
			// For each method.
			for (final Method currentMethod : verifiableObject.getClass().getMethods()) {
				// If it is a getter.
				if (ReflectionHelper.isGetter(currentMethod.getName(), currentMethod.getParameters())) {
					// Gets the attribute name.
					final String attributeName = ReflectionHelper.getAttributeName(currentMethod.getName());
					// Gets the verifiable attribute annotation.
					final VerifiableAttribute veriableAttributeMetadata = currentMethod
							.getAnnotation(VerifiableAttribute.class);
					// If the attribute should be verified.
					if (veriableAttributeMetadata != null) {
						// Adds the attribute to the verifiable set.
						verifiableAttributes.add(attributeName);
					}
				}
			}
		}
		// Returns the verifiable attributes.
		return verifiableAttributes;
	}

	/**
	 * Returns the verification status for an attribute.
	 *
	 * @param  verifiableObject The verifiable object.
	 * @param  attributeName    Attribute name.
	 * @return                  The verification status for an attribute.
	 */
	static VerificationStatus getVerificationStatus(final VerifiableObject verifiableObject,
			final String attributeName) {
		// Attribute status is not verified by default.
		VerificationStatus status = VerificationStatus.NOT_VERIFIED;
		// If the object and attribute are given.
		if ((verifiableObject != null) && (verifiableObject.getVerification().getItems() != null)
				&& (attributeName != null)) {
			// For each verification on the object.
			for (final VerificationItem verificationItem : verifiableObject.getVerification().getItems()) {
				// If the verification is for the attribute.
				if (verificationItem.getAttributes().contains(attributeName)) {
					// If the verification is not expired.
					if (!verificationItem.getExpired()) {
						// Depending on the current attribute status.
						switch (status) {
							// If the current attribute status is not verified or override.
							case NOT_VERIFIED:
							case OVERRIDE:
								// If the current verification status is not override.
								if (!VerificationStatus.OVERRIDE.equals(verificationItem.getStatus())) {
									// Replaces the current attribute status.
									status = verificationItem.getStatus();
								}
								break;
								// If the current attribute status is valid.
							case VALID:
								// If the current verification status is not verified or override.
								if (!Set.of(VerificationStatus.NOT_VERIFIED, VerificationStatus.OVERRIDE)
										.contains(verificationItem.getStatus())) {
									// If the current verification status is invalid.
									if (VerificationStatus.INVALID.equals(verificationItem.getStatus())) {
										// The status is set to dubious.
										status = VerificationStatus.DUBIOUS;
									}
									// For every other verification status.
									else {
										// Replaces the current attribute status.
										status = verificationItem.getStatus();
									}
								}
								break;
								// If the current attribute status is dubious.
							case DUBIOUS:
								// If the current verification status is invalid.
								if (VerificationStatus.INVALID.equals(verificationItem.getStatus())) {
									// Replaces the current attribute status.
									status = verificationItem.getStatus();
								}
								break;
								// If the current attribute status is invalid.
							case INVALID:
								// If the current verification status is valid.
								if (VerificationStatus.VALID.equals(verificationItem.getStatus())) {
									// The status is set to dubious.
									status = VerificationStatus.DUBIOUS;
								}
								break;
						}
					}
				}
			}
		}
		// Returns the verification status.
		return status;
	}

	/**
	 * Returns the verification status for an object.
	 *
	 * @param  verifiableObject The verifiable object.
	 * @return                  The verification status for an object.
	 */
	static VerificationStatus getVerificationStatus(final VerifiableObject verifiableObject) {
		// Object status is valid by default.
		VerificationStatus status = VerificationStatus.VALID;
		// If there are verifiable attributes.
		if (VerifiableObject.getVerifiableAttributes(verifiableObject) != null) {
			// For each verifiable.
			for (final String attributeName : VerifiableObject.getVerifiableAttributes(verifiableObject)) {
				// Gets the verification for the attribute.
				final VerificationStatus attributeStatus = VerifiableObject.getVerificationStatus(verifiableObject, attributeName);
				// Depending on the current object status.
				switch (status) {
					// If the current object status is valid.
					case VALID:
						// Replaces the current object status.
						status = attributeStatus;
						break;
						// If the current object status is dubious.
					case DUBIOUS:
						// If the current attribute status is not valid.
						if (!VerificationStatus.VALID.equals(attributeStatus)) {
							// Replaces the current object status.
							status = attributeStatus;
						}
						break;
						// If the current object status is not verified or override.
					case NOT_VERIFIED:
					case OVERRIDE:
						// If the current attribute status is invalid.
						if (VerificationStatus.INVALID.equals(attributeStatus)) {
							// Replaces the current object status.
							status = attributeStatus;
						}
						break;
						// If the current object status is invalid.
					case INVALID:
						// Nothing to do.
						break;
				}
			}
		}
		// Returns the object status.
		return status;
	}

}

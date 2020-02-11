package org.coldis.library.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract time-stamped object.
 */
public abstract class AbstractTimestampedObject implements TimestampedObject {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = -7189965218508960943L;

	/**
	 * Object creation date/time.
	 */
	private LocalDateTime createdAt;

	/**
	 * Object last update date/time.
	 */
	private LocalDateTime updatedAt;

	/**
	 * @see org.coldis.library.model.TimestampedObject#getCreatedAt()
	 */
	@Override
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	/**
	 * @see org.coldis.library.model.TimestampedObject#setCreatedAt(java.time.LocalDateTime)
	 */
	@Override
	public void setCreatedAt(final LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @see org.coldis.library.model.TimestampedObject#getUpdatedAt()
	 */
	@Override
	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	/**
	 * @see org.coldis.library.model.TimestampedObject#setUpdatedAt(java.time.LocalDateTime)
	 */
	@Override
	public void setUpdatedAt(final LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.createdAt, this.updatedAt);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractTimestampedObject)) {
			return false;
		}
		final AbstractTimestampedObject other = (AbstractTimestampedObject) obj;
		return Objects.equals(this.createdAt, other.createdAt) && Objects.equals(this.updatedAt, other.updatedAt);
	}

}

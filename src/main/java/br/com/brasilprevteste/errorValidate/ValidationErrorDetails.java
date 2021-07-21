package br.com.brasilprevteste.errorValidate;

import lombok.Getter;

@Getter
public class ValidationErrorDetails extends ErrorDetails {

	private String field;
	private String fieldMessage;

	public static final class Builder {
		private String title;
		private int status;
		private String detail;
		private String timestamp;
		private String developerMessage;
		private String field;
		private String fieldMessage;

		private Builder() {

		}

		public static Builder newBuilder() {
			return new Builder();
		}

		public Builder title(String title) {
			this.title = title;
			return this;
		}

		public Builder status(int status) {
			this.status = status;
			return this;
		}

		public Builder detail(String detail) {
			this.detail = detail;
			return this;
		}

		public Builder timestamp(String timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public Builder developerMessage(String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}

		public Builder field(String field) {
			this.field = field;
			return this;
		}

		public Builder fieldMessage(String fieldMessage) {
			this.fieldMessage = fieldMessage;
			return this;
		}

		public ValidationErrorDetails build() {
			ValidationErrorDetails validationErrorDetails = new ValidationErrorDetails();
			validationErrorDetails.setDeveloperMessage(developerMessage);
			validationErrorDetails.setTitle(title);
			validationErrorDetails.setStatus(status);
			validationErrorDetails.setDetail(detail);
			validationErrorDetails.setTimestamp(timestamp);
			validationErrorDetails.field = this.field;
			validationErrorDetails.fieldMessage = this.fieldMessage;
			return validationErrorDetails;
		}
	}
}

package de.flavormate.bb_thymeleaf;

public enum Fragments {
	ERROR,
	BRING,
	RECOVERY_PASSWORD_FORM,
	RECOVERY_PASSWORD_OK,
	RECOVERY_PASSWORD_FAILED,
	RECOVERY_PASSWORD_MAIL;


	public String getFile() {
		return switch (this) {
			case ERROR -> "error.html";
			case BRING -> "bring.html";
			case RECOVERY_PASSWORD_FORM -> "recovery/password-recovery.html";
			case RECOVERY_PASSWORD_OK -> "recovery/password-recovery-success.html";
			case RECOVERY_PASSWORD_FAILED -> "recovery/password-recovery-failed.html";
			case RECOVERY_PASSWORD_MAIL -> "password-recover.html";
		};
	}
}

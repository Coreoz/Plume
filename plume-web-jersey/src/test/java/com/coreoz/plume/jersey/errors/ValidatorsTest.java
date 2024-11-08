package com.coreoz.plume.jersey.errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class ValidatorsTest {
    @Test
    public void checkRequired_whenParameterIsNotNull_shouldReturnParameter() {
        String input = "test";
        String result = Validators.checkRequired(input);
        assertThat(result).isEqualTo(input);
    }

    @Test
    public void checkRequired_whenParameterIsNull_shouldThrowWsException() {
        assertThatThrownBy(() -> Validators.checkRequired(null))
            .isInstanceOf(WsException.class)
            .hasMessage(WsError.FIELD_REQUIRED.name());
    }

    @Test
    public void checkRequired_withFieldName_whenFieldValueIsNotNull_shouldReturnFieldValue() {
        String fieldName = "field";
        String fieldValue = "value";
        String result = Validators.checkRequired(fieldName, fieldValue);
        assertThat(result).isEqualTo(fieldValue);
    }

    @Test
    public void checkRequired_withFieldName_whenFieldValueIsNull_shouldThrowWsException() {
        String fieldName = "field";
        assertThatThrownBy(() -> Validators.checkRequired(fieldName, (String) null))
            .isInstanceOf(WsException.class)
            .hasMessage(WsError.FIELD_REQUIRED.name());
    }

    @Test
    public void checkEmail_whenEmailIsValid_shouldReturnEmail() {
        String fieldName = "email";
        String fieldValue = "test@example.com";
        String result = Validators.checkEmail(fieldName, fieldValue);
        assertThat(result).isEqualTo(fieldValue);
    }

    @Test
    public void checkEmail_whenEmailIsInvalid_shouldThrowWsException() {
        String fieldName = "email";
        String invalidEmail = "invalid-email";
        assertThatThrownBy(() -> Validators.checkEmail(fieldName, invalidEmail))
            .isInstanceOf(WsException.class)
            .hasMessage(WsError.EMAIL_INVALID.name());
    }

    @Test
    public void checkHexaColor_whenValidHexColor_shouldReturnFieldValue() {
        String fieldName = "color";
        String validHexColor = "a1b2c3";
        String result = Validators.checkHexaColor(fieldName, validHexColor);
        assertThat(result).isEqualTo(validHexColor);
    }

    @Test
    public void checkHexaColor_whenInvalidHexColor_shouldThrowWsException() {
        String fieldName = "color";
        String invalidHexColor = "zzzzzz";
        assertThatThrownBy(() -> Validators.checkHexaColor(fieldName, invalidHexColor))
            .isInstanceOf(WsException.class)
            .hasMessage(WsError.COLOR_INVALID.name());
    }

    @Test
    public void checkHexaColorWithStartingHash_whenValidHexColorWithHash_shouldReturnFieldValueWithoutHash() {
        String fieldName = "color";
        String hexColorWithHash = "#a1b2c3";
        String result = Validators.checkHexaColorWithStartingHash(fieldName, hexColorWithHash);
        assertThat(result).isEqualTo("#a1b2c3");
    }

    @Test
    public void checkHexaColorWithStartingHash_whenNullProvider_shouldReturnNull() {
        String fieldName = "color";
        String hexColorWithHash = null;
        String result = Validators.checkHexaColorWithStartingHash(fieldName, hexColorWithHash);
        assertThat(result).isNull();
    }

    @Test
    public void checkHexaColorWithStartingHash_whenNoHash_shouldThrowWsException() {
        String fieldName = "color";
        String hexColorWithoutHash = "a1b2c3";
        assertThatThrownBy(() -> Validators.checkHexaColorWithStartingHash(fieldName, hexColorWithoutHash))
            .isInstanceOf(WsException.class)
            .hasMessage(WsError.COLOR_INVALID.name());
    }

    @Test
    public void checkHexaColorWithStartingHash_whenInvalidHexColorAfterHash_shouldThrowWsException() {
        String fieldName = "color";
        String invalidHexColor = "#zzzzzz";
        assertThatThrownBy(() -> Validators.checkHexaColorWithStartingHash(fieldName, invalidHexColor))
            .isInstanceOf(WsException.class)
            .hasMessage(WsError.COLOR_INVALID.name());
    }
}

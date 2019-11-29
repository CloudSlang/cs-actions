package io.cloudslang.content.utilities.util.randomPasswordGenerator;

import org.passay.CharacterData;

public enum Characters implements CharacterData {
    LowerCase("INSUFFICIENT_LOWERCASE", "abcdefghijklmnopqrstuvwxyz"),
    UpperCase("INSUFFICIENT_UPPERCASE", "ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    Digit("INSUFFICIENT_DIGIT", "0123456789"),
    Special("INSUFFICIENT_SPECIAL", "-=[];,.~!@#$%&*()_+{}|:<>?");

    private final String errorCode;
    private final String characters;

    Characters(String code, String charString) {
        this.errorCode = code;
        this.characters = charString;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getCharacters() {
        return this.characters;
    }

}

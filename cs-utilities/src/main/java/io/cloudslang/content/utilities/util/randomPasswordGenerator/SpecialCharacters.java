package io.cloudslang.content.utilities.util.randomPasswordGenerator;

import org.passay.CharacterData;

public class SpecialCharacters implements CharacterData {

    private String characters = "-=[];,.~!@#$%&*()_+{}|:<>?";

    public SpecialCharacters(String forbiddenCharacters) {
        for (char forbiddenCharacter : forbiddenCharacters.toCharArray()) {
            StringBuilder sb = new StringBuilder(characters);
            int index = sb.indexOf(Character.toString(forbiddenCharacter));
            if(index != -1){
                sb.deleteCharAt(index);
                characters = sb.toString();
            }
        }
    }

    public String getErrorCode() {
        return "INSUFFICIENT_SPECIAL";
    }

    public String getCharacters() {
        return this.characters;
    }

}
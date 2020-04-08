package io.cloudslang.content.abby.utils;

import io.cloudslang.content.abby.exceptions.AbbyySdkException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.validation.Validator;
import java.io.IOException;

import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
public abstract class AbbyyResultValidatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    protected AbbyyResultValidator sut;

    @Before
    public void setUp() throws Exception {
        this.sut = this.newSutInstance();
    }


    protected abstract AbbyyResultValidator newSutInstance() throws Exception;


    @Test
    public void validate_resultIsNull_IllegalArgumentException() throws IOException {
        //Arrange
        final String result = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.validate(result);
    }
}

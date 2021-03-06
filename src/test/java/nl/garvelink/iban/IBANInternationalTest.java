/*
   Copyright 2019 Barend Garvelink

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package nl.garvelink.iban;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Ensures that the {@link IBAN} class accepts IBAN numbers from every participating country (...known at the time the test was last updated).
 */
@RunWith(Parameterized.class)
public class IBANInternationalTest {

    private final TestData td;

    @SuppressWarnings("unused")
    public IBANInternationalTest(TestData td) {
        this.td = td;
    }

    @Parameterized.Parameters(name = " {0} ")
    public static List<TestData> parameters() {
        return CountryCodesParameterizedTest.PARAMETERS;
    }

    @Test
    public void parseShouldAcceptPlainForm() {
        IBAN iban = IBAN.parse(td.plain);
        assertNotNull(iban);
        assertThat(iban.toPlainString(), is(equalTo(td.plain)));
        assertThat(iban.toString(), is(equalTo(td.pretty)));
    }

    @Test
    public void parseShouldAcceptPrettyPrintedForm() {
        IBAN iban = IBAN.parse(td.pretty);
        assertNotNull(iban);
        assertThat(iban.toPlainString(), is(equalTo(td.plain)));
        assertThat(iban.toString(), is(equalTo(td.pretty)));
    }

    @Test
    public void parseShouldRejectInvalidIBANLength() {
        try {
            IBAN.parse(td.plain + '9');
            fail("Invalid input should have been rejected for incorrect length.");
        } catch (WrongLengthException e) {
            assertThat(e.getFailedInput(), is(td.plain + '9'));
            assertThat(e.getExpectedLength(), is(td.plain.length()));
            assertThat(e.getActualLength(), is(1 + td.plain.length()));
        }
    }

    @Test
    public void isRegisteredIBAN() {
        assertEquals(td.swift, IBAN.parse(td.plain).isInSwiftRegistry());
    }

    @Test
    public void isSEPACountry() {
        assertEquals(td.sepa, IBAN.parse(td.plain).isSEPA());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void getLengthForCountryCodeShouldReturnCorrectValue() {
        assertEquals(td.plain.length(), IBAN.getLengthForCountryCode(td.plain.substring(0, 2)));
    }
}

package com.lisak.util;

import com.lisak.exception.MotorPolicyServiceFailureException;
import com.lisak.swagger.model.PolicyCover;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static com.lisak.util.JsonMapper.getResource;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonMapperTest {

    private String policyCoverSuccess;

    private String policyCoverInvalid;

    private ClassThatJacksonCannotSerialize self;

    @Before
    public void setup() throws Exception {
        URL url = Resources.getResource("policy-cover-normal.json");
        policyCoverSuccess = Resources.toString(url, Charsets.UTF_8);

        url = Resources.getResource("policy-cover-invalid.json");
        policyCoverInvalid = Resources.toString(url, Charsets.UTF_8);

        self = new ClassThatJacksonCannotSerialize();

    }

    @Test
    public void test_fromJson_success() {
        final PolicyCover policyCover = JsonMapper.fromJson(policyCoverSuccess, PolicyCover.class);
        assertThat(policyCover)
                .isNotNull()
                .hasFieldOrPropertyWithValue("policyNo", 45381820L)
                .hasFieldOrPropertyWithValue("policyCoverNo", 421L);
        //todo test other policyCover object properties
    }

    @Test
    public void test_fromJson_null() {
        final PolicyCover policyCover = JsonMapper.fromJson(null, PolicyCover.class);
        assertThat(policyCover).isNull();

        //todo test other policyCover object properties
    }

    @Test(expected = MotorPolicyServiceFailureException.class)
    public void test_fromJson_malformed() {
        JsonMapper.fromJson(policyCoverInvalid, PolicyCover.class);

        //todo test other policyCover object properties
    }

    @Test
    public void test_toJson_success() {
        final String policyCoverJson = JsonMapper.toJson(policyCoverSuccess);
        assertThat(policyCoverJson).isNotNull();
    }

    @Test
    public void test_toJson_null() {
        final String policyCoverString = JsonMapper.toJson(null);
        assertThat(policyCoverString).isNull();
    }

    @Test(expected = MotorPolicyServiceFailureException.class)
    public void test_toJson_malformed() {
        JsonMapper.toJson(self);
}

    @Test(expected = RuntimeException.class)
    public void test_getResource_malformed() {
       JsonMapper.getResource("foo.json");
    }

    public class ClassThatJacksonCannotSerialize {
        private final ClassThatJacksonCannotSerialize self = this;

        @Override
        public String toString() {
            return self.getClass().getName();
        }
    }
}
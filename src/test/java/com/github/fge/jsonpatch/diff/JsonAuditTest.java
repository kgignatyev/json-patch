package com.github.fge.jsonpatch.diff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

/**
 * User: kgignatyev
 * Date: 8/27/14
 */
public class JsonAuditTest {

    @Test
    public void testAuditRecords() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode res = new JsonAudit().asJson(om.valueToTree(new Person("John")), om.valueToTree(new Person("John Dow")));
        System.out.println("simple value change " + om.writeValueAsString(res));
    }


    @Test
    public void testAuditRecordsForArrays() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode res = new JsonAudit().asJson(om.valueToTree(new Person[]{new Person("John")}), om.valueToTree(new Person[]{new Person("John Dow")}));
        System.out.println("value change on array element " + om.writeValueAsString(res));
    }


    @Test
       public void testAuditRecordsForArrayRemovals() throws JsonProcessingException {
           ObjectMapper om = new ObjectMapper();
           JsonNode res = new JsonAudit().asJson(om.valueToTree(new Person[]{new Person("John")}), om.valueToTree(new Person[]{}));
           System.out.println("removal of array element = " + om.writeValueAsString(res));
       }


    static class Person {
        String name;

        Person() {
        }

        Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

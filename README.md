## Read me first

This project, as of version 1.4, is licensed under both LGPLv3 and ASL 2.0. See
file LICENSE for more details. Versions 1.3 and lower are licensed under LGPLv3
only.

## What this is

This is an implementation of [RFC 6902 (JSON Patch)](http://tools.ietf.org/html/rfc6902) and [JSON
Merge Patch](http://tools.ietf.org/html/draft-ietf-appsawg-json-merge-patch-02) written in Java,
which uses [Jackson](https://github.com/FasterXML/jackson-databind) (2.2.x) at its core.

this fork included JsonAudit implementation

Its features are:

* {de,}serialization of JSON Patch and JSON merge patch instances with Jackson;
* full support for RFC 6902 operations, including `test`;
* JSON "diff" (RFC 6902 only) with operation factorization.

The JSON diff implementation is courtesy of [Randy Watler](https://github.com/rwatler).

JsonAudit class allows writing audit log that includes 'old values' for removed and updated elements

## Versions

The current version is **1.7**. See file `RELEASE-NOTES.md` for details.

## Using it in your project

With Gradle:

```groovy
dependencies {
    compile(group: "com.kgionline", name: "json-patch", version: "yourVersionHere");
}
```

With Maven:

```xml
<dependency>
    <groupId>com.kgionline</groupId>
    <artifactId>json-patch</artifactId>
    <version>yourVersionHere</version>
</dependency>
```

## JSON "diff" factorization

When computing the difference between two JSON texts (in the form of `JsonNode` instances), the diff
will factorize value removals and additions as moves and copies.

For instance, given this node to patch:

```json
{ "a": "b" }
```

in order to obtain:

```json
{ "c": "b" }
```

the implementation will return the following patch:

```json
[ { "op": "move", "from": "/a", "path": "/c" } ]
```

It is able to do even more than that. See the test files in the project.

## Note about the `test` operation and numeric value equivalence

RFC 6902 mandates that when testing for numeric values, however deeply nested in the tested value,
a test is successful if the numeric values are _mathematically equal_. That is, JSON texts:

```json
1
```

and:

```json
1.00
```

must be considered equal.

This implementation obeys the RFC; for this, it uses the numeric equivalence of
[jackson-coreutils](https://github.com/fge/jackson-coreutils).

## Sample usage

### JSON Patch

You have to choices to build a `JsonPatch` instance: use Jackson deserialization, or initialize one
directly from a `JsonNode`. Examples:

```
// Using Jackson
final ObjectMapper mapper = new ObjectMapper();
final InputStream in = ...;
final JsonPatch patch = mapper.readValue(in, JsonPatch.class);
// From a JsonNode
final JsonPatch patch = JsonPatch.fromJson(node);
```

You can then apply the patch to your data:

```java
// orig is also a JsonNode
final JsonNode patched = patch.apply(orig);
```

### JSON diff

The main class is `JsonDiff`. It returns the patch as a `JsonNode`. Sample usage:

```java
final JsonNode patchNode = JsonDiff.asJson(firstNode, secondNode);
```

You can then use the generated `JsonNode` to build a patch using the code sample above.

### JSON Merge Patch

As for `JsonPatch`, you may use either Jackson or "direct" initialization:

```java
// With Jackson
final JsonMergePatch patch = mapper.readValue(in, JsonMergePatch.class);
// With a JsonNode
final JsonMergePatch patch = JsonMergePatch.fromJson(node);
```

Applying a patch also uses an `.apply()` method:

```java
// orig is also a JsonNode
final JsonNode patched = patch.apply(orig);
```


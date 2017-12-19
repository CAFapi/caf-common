# decoder-js

---

Implementation of Decoder which supports constructing objects using JavaScript.

## Format

The input stream provided to the Decoder is expected to contain a JavaScript object surrounded by '(' and ')'.

```
({
    name: "My Example",
    description: "This is an example of the format that should be provided to the Decoder"
});
```

And the class to decode this object to would be;

```
public class Example {
  private String name;
  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
```

A function is available for use that will retrieve the value of a specified environment variable during decoding.
It is used in the JavaScript by calling the `getenv` function, passing the name of the variable to retrieve.

```
({
    name: getenv("MY_STRING"),
    description: getenv("MY_DESCRIPTION")
});
```

If an environment variable specified is present at runtime, then its value will be used on the decoded object property.
If the variable is not present the property will be set to the default value of the property type.

Within the object definition JavaScript operators may be used to control the values set for properties.
This can be used to define fallback environment variables and default values.

```
({
    name: getenv("MY_STRING") || (getenv("GLOBAL_STRING") || "Default") + " Example",
    description: getenv("MY_DESCRIPTION") || "Default description"
});
```

In the above example;
* If the environment variable `MY_STRING` is set, the value of the decoded `name` property on the object
will use that value.
e.g. MY_STRING=My Name, `name` is set to "My Name".
* If `MY_STRING` is not set then the environment variable `GLOBAL_STRING` will be checked. If it has a value
then the `name` property is set to that value concatenated with " Example".
e.g. GLOBAL_STRING=Global, `name` is set to "Global Example".
* If Neither environment variable is set then the `name` property is set to "Default Example".

### Notes
If you have characters in your key or value names that might confuse javascript, then know your javascript. For example if I have a key with name like `my-another-key` then docoder shall fail to parse the configuration, as it shall consider the dash as substract operator. So in that case you can use single quotes to your salvation. 

```
// Shall produce parse error
({
    my-another-key: "Another key"
});

// This shall go fine though; NOTE single quotes around my-another-key
({
    'my-another-key': "Another key"
});

```

Important point to remember here is, that this library ultimately calling a javascript enging from java.

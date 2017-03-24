({
    myInt: getenv("TEST_MYINT") || 100,
    myString: getenv("TEST_MYSTRING")
    || (getenv("TEST_BASE") || "default") + "-appended",
    myBoolean: getenv("TEST_MYBOOLEAN") || false,
    myNestedProp: {
        myNestedString: getenv("TEST_MYNESTEDSTRING") || "default nested string",
        myNestedBoolean: getenv("TEST_MYNESTEDBOOLEAN") || false,
        myNestedInt: getenv("TEST_MYNESTEDINT") || 500
    }
});
package com.hpe.caf.decoder;

/**
 * Class for decoder to decode input data to.
 */
public class DecodeResult {
    private String myString;
    private int myInt;
    private boolean myBoolean;
    private NestedProp myNestedProp;

    public String getMyString() {
        return myString;
    }

    public void setMyString(String myString) {
        this.myString = myString;
    }

    public int getMyInt() {
        return myInt;
    }

    public void setMyInt(int myInt) {
        this.myInt = myInt;
    }

    public boolean isMyBoolean() {
        return myBoolean;
    }

    public void setMyBoolean(boolean myBoolean) {
        this.myBoolean = myBoolean;
    }

    public NestedProp getMyNestedProp() {
        return myNestedProp;
    }

    public void setMyNestedProp(NestedProp myNestedProp) {
        this.myNestedProp = myNestedProp;
    }

    public class NestedProp {
        private String myNestedString;
        private int myNestedInt;
        private boolean myNestedBoolean;

        public String getMyNestedString() {
            return myNestedString;
        }

        public void setMyNestedString(String myNestedString) {
            this.myNestedString = myNestedString;
        }

        public int getMyNestedInt() {
            return myNestedInt;
        }

        public void setMyNestedInt(int myNestedInt) {
            this.myNestedInt = myNestedInt;
        }

        public boolean isMyNestedBoolean() {
            return myNestedBoolean;
        }

        public void setMyNestedBoolean(boolean myNestedBoolean) {
            this.myNestedBoolean = myNestedBoolean;
        }
    }
}

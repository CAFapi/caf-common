/*
 * Copyright 2015-2024 Open Text.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opentext.caf.decoder;

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

package com.exenum;

public enum MyCalc implements MyCalcInterface {
    PLUS {
        @Override
        public int calc(int x1, int x2) {
            return x1 + x2;
        }

        @Override
        public boolean validate(int x1, int x2) {
            if (x1 == 0 && x2 == 0)
                return false;

            return true;
        }
    },
    MINUS {
        @Override
        public int calc(int x1, int x2) {
            return x1 - x2;
        }

        @Override
        public boolean validate(int x1, int x2) {
            if (x1 == 0 && x2 == 0)
                return false;

            if (x1 > x2)
                return true;

            return false;
        }
    },
    DIVIDE {
        @Override
        public int calc(int x1, int x2) {
            return x1 / x2;
        }

        @Override
        public boolean validate(int x1, int x2) {
            if (x1 == 0 || x2 == 0)
                return false;

            return true;
        }
    }
}

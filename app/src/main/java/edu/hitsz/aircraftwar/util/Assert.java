package edu.hitsz.aircraftwar.util;

public class Assert {
    public static void assertNotNull(Object obj) throws NullPointerException{
        if(obj == null){
            throw new NullPointerException();
        }
    }
}

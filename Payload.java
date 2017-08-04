package com.remotes.utils;


/** This class is a stand-in for actual data to be transferred over the net.
Construct test payloads of any size and give them an identifying name.
The payload will be initialized with random bytes, and is able to calculate
a checksum to test for data integrity.
*/
public class Payload implements java.io.Serializable {

    public static final boolean REDUNDANCY = true;

    private String name;
    private byte[] data;

    /** Create a test payload of given size (in Bytes) with the given name and random content.
    Note that the actual object is slightly larger than the given size due to its own struture
    and the name. */
    public Payload(String name, int size) {
        this.name = name;
        data = new byte[size];
        for (int i=0; i<size; i++) {
            if (REDUNDANCY) {
                data[i] = (byte)(Math.random()*128); // 1 bit in 8 is zero, others are random
            } else {
                data[i] = (byte)(Math.random()*256-128); // all 8 bits are random
            }

        }
    }

    /** Calculates and returns a checksum (simple XOR of all the data bytes). */
    public byte checksum() {
        byte checksum = 0;
        for (int i=0; i<data.length; i++) {
            checksum ^= data[i];
        }
        return checksum;
    }

    /** Returns the name property of this Payload. */
    public String getName() { return name; }
}

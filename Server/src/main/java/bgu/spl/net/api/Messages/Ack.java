package bgu.spl.net.api.Messages;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Ack extends Message {

    //fields
    private short msgOpcode;
    private List<byte[]> optional;

    public Ack(short msgOpcode, List<byte[]> optional) {
        super(new Integer(10).shortValue());
        this.msgOpcode = msgOpcode;
        this.optional = optional;
    }

    public byte[] messageToEncode() {
        byte[] byteArr = new byte[2];
        byte[] byteArrOpcode =shortToBytes(msgOpcode);
        //
        if (optional != null){
            // first get total size
            int size = 2;
            for (byte[] bytes: optional) {
                size += bytes.length;
            }
            byteArr = new byte[size];
            // loop all bytes and add them to the arr
            int counter = 2;
            for (byte[] bytes: optional) {
                for (int i = 0; i < bytes.length; i++) {
                    byteArr[i + counter] = bytes[i];
                }
                counter += bytes.length;
            }
        }
        // also add the opcode
        byteArr[0] = byteArrOpcode[0];
        byteArr[1] = byteArrOpcode[1];
        //
        return byteArr;
    }

}

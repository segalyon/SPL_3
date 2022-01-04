package bgu.spl.net.api.Messages;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Ack extends Message {

    //fields
    private short msgOpcode;
    private String optional;

    public Ack(short msgOpcode, String optional) {
        super(new Integer(10).shortValue());
        this.msgOpcode = msgOpcode;
        this.optional=optional;
    }

    /**
     * transforms each short in the shortQueue to byte array and then the string Optional to byte array
     * joins all the array to create a joined array
     * @return byte array that represents the message
     */
    public byte[] messageToEncode() {
        LinkedList<byte[]> encodedArrayList=new LinkedList<>();
//        int counter=shortQueue.size()*2+2;//the number of byte cells in the returned array-> 2 bytes for each short in the short queue plus 2 bytes for the opcode we add before the while
//        encodedArrayList.add(shortToBytes(getOpcode()));//add the opcode to the list
//        while(!shortQueue.isEmpty()){
//            encodedArrayList.add(shortToBytes(shortQueue.remove()));
//        }
//        if(optional!=null) {
//            encodedArrayList.add(optional.getBytes());
//            counter += encodedArrayList.get(encodedArrayList.size() - 1).length;// gets the length of the array made from the string
//        }
//        byte[]encodedArray=new byte[counter];
//        int index=0;
//        for (byte[] byteArray:encodedArrayList) {
//            for (byte b: byteArray) {
//                encodedArray[index]=b;
//                index++;
//            }
//        }
//        return  encodedArray;
    }

}

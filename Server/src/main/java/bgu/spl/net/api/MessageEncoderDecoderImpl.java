package bgu.spl.net.api;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    //fields
    List<byte[]> words = new LinkedList<>();
    Queue<Byte> msg = new ConcurrentLinkedQueue<>();
    private Short Opcode=null;

    @Override
    public Message decodeNextByte(byte nextByte) {

        Message message = null;
        // add new word if seen seperator and no opcode as benn detected
//        if(nextByte == ';'){
//        }
//        else if(nextByte=='\0' && Opcode != null){
//            msg.
//        }
//        else {
//            currentWord.add(nextByte);
//        }
        msg.add(nextByte);

        // if opcode is null, expect opcode in word
        if (Opcode == null) {
            if (msg.size() == 2) {
                Opcode = bytesToShort(extractBytes(msg, 2));
            }
        }
        if (Opcode != null && nextByte == ';') {
            //
            switch (Opcode.intValue()){
                case 1:
                        words.add(extractString(msg, '\0'));
                        words.add(extractString(msg, '\0'));
                        words.add(extractString(msg, '\0'));
                        message = new Register(words);
                        cleanMessege();
                    break;
                case 2:
                        words.add(extractString(msg, '\0'));
                        words.add(extractString(msg, '\0'));
                        words.add(extractBytes(msg, 1));
                        message=new Login(words);
                        cleanMessege();
                    break;
                case 3:
                        message=new Logout();
                        cleanMessege();
                    break;
                case 4:
                        words.add(extractBytes(msg, 1));
                        words.add(extractString(msg, '\0'));
                        message = new Follow(words);
                        cleanMessege();
                    break;
                case 5:
                        words.add(extractString(msg, '\0'));
                        message= new POST(words);
                        cleanMessege();
                    break;
                case 6:
                        words.add(extractString(msg, '\0'));
                        words.add(extractString(msg, '\0'));
                        words.add(extractString(msg, '\0'));
                        message= new PM(words);
                        cleanMessege();
                    break;
                case 7:
                        message=new LogStat();
                        cleanMessege();
                    break;
                case 8:
                        words.add(extractString(msg, '\0'));
                        message=new STAT(words);
                        cleanMessege();
                    break;
                case 12:
                        words.add(extractString(msg, '\0'));
                        message = new Block(words);
                        cleanMessege();
                    break;


            }
        }
        return message;


    }

    @Override
    public byte[] encode(Message message) {
        return message.messageToEncode();
    }

    private byte[] extractString(Queue<Byte> msg, char delimiter) {
        List<Byte> word = new LinkedList<>();
        while (msg.peek() != delimiter){
            word.add(msg.poll());
        }
        msg.remove(); // remove delimiter
        return listToArray(word);
    }
    private byte[] extractBytes(Queue<Byte> msg, int c) {
        List<Byte> word = new LinkedList<>();
        for(int i = 0; i < c; i++){
            word.add(msg.poll());
        }
        return listToArray(word);
    }



    private short bytesToShort(byte[] byteArr){
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }
//
    private byte[] listToArray(List<Byte> list){
       byte[] arr = new byte[list.size()];
       for (int i = 0; i < list.size(); i++){
           arr[i] = list.get(i);
       }
       return arr;
    }
//
//    //for the decoder functions, after finishing readisng a message restore the fields to default
    private void cleanWord(){
       msg.clear();
    }
    private void cleanMessege(){
      //  bytes.clear();
        words.clear();
        cleanWord();
        Opcode = null;
    }
}

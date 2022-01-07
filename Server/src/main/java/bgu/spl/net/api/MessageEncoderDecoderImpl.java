package bgu.spl.net.api;
import bgu.spl.net.api.Messages.Message;
import bgu.spl.net.api.Messages.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    //fields
    List<byte[]> words = new LinkedList<>();
    List<Byte> currentWord = new ArrayList<Byte>();
    private Short Opcode=null;

    @Override
    public Message decodeNextByte(byte nextByte) {

        Message message = null;
        // add new word if seen seperator and no opcode as benn detected
        if(nextByte == ';'){
        }
        else if(nextByte=='\0' && Opcode != null){
            if (currentWord.size() == 0){ // if no word before, then the 0 is a flag
                currentWord.add(nextByte);
            }
            words.add(listToArray(currentWord));
            cleanWord();
        }
        else {
            currentWord.add(nextByte);
        }

        // if opcode is null, expect opcode in word
        if (Opcode == null) {
            if (currentWord.size() == 2) {
                Opcode = bytesToShort(listToArray(currentWord));
                cleanWord();
            }
        }
        if (Opcode != null && nextByte == ';') {
            //
            switch (Opcode.intValue()){
                case 1:
                    //if (words.size() == 3) {
                        message = new Register(words);
                        cleanMessege();
                    //}
                    break;
                case 2:
                        if(words.size()==2)
                            words.add(listToArray(currentWord));
                        message=new Login(words);
                        cleanMessege();
                    break;
                case 3:
                    message=new Logout();
                    cleanMessege();
                    break;
                case 4:
                    //if(words.size()== 2)
                    //{
                        message = new Follow(words);
                        cleanMessege();
                   // }
                    break;
                case 5:
                    //if(words.size()==1){
                        message= new POST(words);
                        cleanMessege();
                  //  }
                    break;
                case 6:
                   // if(words.size()==3){
                        message= new PM(words);
                        cleanMessege();
                   // }
                    break;
                case 7:
                   // if(words.size()==1){
                        message=new LogStat(words);
                        cleanMessege();
                    //}
                    break;
                case 8:
                   // if(words.size()==1){
                        message=new STAT(words);
                        cleanMessege();
                  //  }
                    break;
                case 12:
                   // if(words.size()==1) {
                        message = new Block(words);
                        cleanMessege();
                   // }
                    break;


            }
        }
        return message;


    }

    @Override
    public byte[] encode(Message message) {
        return message.messageToEncode();
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
//    //for the decoder functions, after finishing reading a message restore the fields to default
    private void cleanWord(){
       currentWord = new ArrayList<Byte>();
    }
    private void cleanMessege(){
      //  bytes.clear();
        words.clear();
        cleanWord();
        Opcode = null;
    }
}

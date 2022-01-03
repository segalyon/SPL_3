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
        if(nextByte=='\0' && Opcode != null){
            words.add(listToArray(currentWord));
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
        if (Opcode != null) {
            //
            switch (Opcode.intValue()){
                case 1:
                    if (words.size() == 3) {
                        message = new Register(words);
                        cleanMessege();
                    }
                    break;
                case 2:
                    if(words.size()==2){
                        byte[] captcha= new byte[1];
                        captcha[0]=nextByte;
                        message=new Login(words, captcha);
                        cleanMessege();
                    }
                    break;
                case 3:
                    message=new Logout();
                    cleanMessege();
                    break;
                case 5:
                    if(words.size()==1){
                        message= new POST(words);
                        cleanMessege();
                    }
                    break;
                case 6:
                    if(words.size()==3){
                        message= new PM(words);
                        cleanMessege();
                    }
                    break;
                case 7:
                    if(words.size()==0){
                        message=new LogStat();
                        cleanMessege();
                    }
                    break;
                case 8:
                    if(words.size()==1){
                        message=new STAT(words);
                        cleanMessege();
                    }
                    break;
                case 9:
                    if(words.size()==2) {
                        message = new Notification(words);
                        cleanMessege();
                    }
                    break;


            }
        }
        return message;



//
//
//        ///
//        if(Opcode==null && counter>0 && bytes.size()==1){ //returns true when the second byte after '\0' is received
//            bytes.add(nextByte);
//            byte[] arr=listToArray(bytes);
//            Opcode=bytesToShort(arr);
//            bytes.remove(1);
//        }
//        if(nextByte=='\0') //counter for the '\0' bytes
//            counter++;
//
//        if(Opcode!=null) {
//
//            switch (Opcode.intValue()) {
//                // register message
//                case 1:
//                    if(counter==2 && sepreatingIndex==-1) //true after the second '\0' byte is received which is the end of username
//                        sepreatingIndex=bytes.size();
//                    if (counter == 4) {         //true after the third '\0' byte is received - if true create the register message
//                        byte[] arr = listToArray(bytes);
//                        closeFields();
//                        int tmpSepreatingIndex=sepreatingIndex;
//                        this.sepreatingIndex=-1;
//                        return new Register(arr,tmpSepreatingIndex);
//                    }
//                    break;
//                //login message
//                case 2:
//                    if(counter==2 && sepreatingIndex==-1) //true after the second '\0' byte is received which is the end of username
//                        sepreatingIndex=bytes.size();
//                    if (counter == 3) {          //true after the third '\0' byte is received - if true create the login message
//                        byte[] arr = listToArray(bytes);
//                        closeFields();
//                        int tmpSepreatingIndex=sepreatingIndex;
//                        this.sepreatingIndex=-1;
//                        return new LoginMessage(arr,tmpSepreatingIndex);
//                    }
//                    break;
//                //logout message
//                case 3:
//                    closeFields();
//                    return new LogoutMessage();
//
//                //follow and unfollow messgae
//                case 4:
//                    if (bytes.size() == 4) { //checks the number of users
//                        byte[] tmpArr={bytes.get(3),nextByte};
//                        numOfUsers=bytesToShort(tmpArr);
//                        counter=0; //changes the counter to 0 so now it will count the number of 0 between each user
//                    }
//                    if (numOfUsers!=-1 && counter==numOfUsers.intValue()){ //if we read the numOfUsers /0 we are finished
//                        bytes.add(nextByte);
//                        byte[] arr = listToArray(bytes);
//                        closeFields();
//                        followMessage=false;
//                        int tempNumOfUsers=numOfUsers.intValue();
//                        numOfUsers=-1;
//                        return new FollowUnfollowMessage(arr,tempNumOfUsers);
//                    }
//                    break;
//                //post message
//                case 5:
//                    if (counter == 2) {    //true after the second '\0' byte is received - if true create the post message
//                        byte[] arr = listToArray(bytes);
//                        closeFields();
//                        return new PostMessage(arr);
//                    }
//                    break;
//                //pm message
//                case 6:
//                    if(counter==2 && sepreatingIndex==-1)  //true after the second '\0' byte is received which is the end of username
//                        sepreatingIndex=bytes.size();
//                    if (counter == 3) {  //true after the third '\0' byte is received - if true create the pm message
//                        byte[] arr = listToArray(bytes);
//                        closeFields();
//                        int tmpSepreatingIndex=sepreatingIndex;
//                        this.sepreatingIndex=-1;
//                        return new PrivateMessage(arr, tmpSepreatingIndex );
//                    }
//                    break;
//                //user list message
//                case 7:
//                    closeFields();
//                    return new UserlistMessage();
//                //stat message
//                case 8:
//                    if (counter == 2) {
//                        byte[] arr = listToArray(bytes);
//                        closeFields();
//                        return new StatMessage(arr);
//                    }
//                    break;
//            }
//        }
//        bytes.add(nextByte);
//        return null;


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

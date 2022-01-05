package bgu.spl.net.api.Messages;

import bgu.spl.net.srv.DataBase;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Notification extends Message {
    //fields
    private short pmOrPublic;
    private String postingUser;
    private String content;
    public Notification(short pmOrPublic, String postingUser, String content){
        super((short) 9);
        this.pmOrPublic=pmOrPublic;
        this.postingUser=postingUser;
        this.content=content;
    }
    public int getPmOrPublic(){return pmOrPublic;}

    public String getContent() {
        return content;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public byte[] messageToEncode(){
        String filteredContent = content;
        byte[] opcodeArr = shortToBytes(getOpcode());
        byte[] typeArr = shortToBytes(pmOrPublic);
        byte[] seperatorArr = shortToBytes((short)'\0');
        byte[] postingUserArr = postingUser.getBytes();
        // filter content
        if (pmOrPublic == 1){
            DataBase db= DataBase.getInstance();
            List<String> filter= db.getFilterWords();
            for (String word: filter) {
                filteredContent.replaceAll("\\b" + word + "\\b", "<filtered>");
            }
//            String result="";
//            String current="";
//            for(char c:content.toCharArray())
//            {
//                if(c !=' ')
//                    current+=c;
//                else{
//                    if(!filter.contains(current))
//                        result+=current+" ";
//                    else result+="<filtered> ";
//                    current="";
//                }
//            }
//            return result.getBytes();
        }
        byte[] contentArr = filteredContent.getBytes();
        byte[] res = new byte[5 + contentArr.length + postingUserArr.length];
        // write
        res[0] = opcodeArr[0];
        res[1] = opcodeArr[1];
        res[2] = typeArr[0];
        //
        for(int i = 0; i < postingUserArr.length; i++){
            res[i + 3] = postingUserArr[i];
        }
        //
        res[postingUserArr.length + 3] = seperatorArr[0];
        //
        for(int i = 0; i < contentArr.length; i++){
            res[i + postingUserArr.length + 4] = contentArr[i];
        }
        //
        res[postingUserArr.length + contentArr.length + 4] = seperatorArr[0];
        return res;
    }
/*
    /*
    public byte[] messageToEncode(){
        byte[]tmpOpcode=shortToBytes(getOpcode());
        byte[] tmpPmOrPublic;
        if(pmOrPublic=='1')
           tmpPmOrPublic = shortToBytes((short) 1);
        else
            tmpPmOrPublic = shortToBytes((short) 0);
        String stringOfPostInfo=""+postingUser+"\0"+content+"\0";
        byte[]postInfo=stringOfPostInfo.getBytes();
        int sizeOfEncodedArray=4+postInfo.length;
        byte[]encodedArray=new byte[sizeOfEncodedArray];
        encodedArray[0]=tmpOpcode[0];
        encodedArray[1]=tmpOpcode[1];
        encodedArray[2]=tmpPmOrPublic[0];
        encodedArray[3]=tmpPmOrPublic[1];
        for(int i=4; i<encodedArray.length; i++)
            encodedArray[i]=postInfo[i-4];

        return encodedArray;
    }
*/
}
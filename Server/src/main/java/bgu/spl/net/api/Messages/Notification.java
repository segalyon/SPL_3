package bgu.spl.net.api.Messages;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Notification extends Message {
    //fields
    private int pmOrPublic;
    private String postingUser;
    private String content;
    public Notification(int pmOrPublic, String postingUser, String content){
        super((short) 9);
        this.pmOrPublic=pmOrPublic;
        this.postingUser=postingUser;
        this.content=content;
        /*
        byte[] pmpublic=new byte[1];
        byte[] postuser= new byte[words.get(0).length-1];
        pmpublic[1]= ((byte[])words.get(0))[0];
        this.pmOrPublic=Integer.parseInt(new String(pmpublic, StandardCharsets.UTF_8));
        for(int i=0; i<pmpublic.length;i++)
        {
            postuser[i]=((byte[])words.get(0))[i+1];
        }
        this.postingUser=new String(postuser, StandardCharsets.UTF_8);
        this.content=new String(words.get(1), StandardCharsets.UTF_8);
        */
    }
    public int getPmOrPublic(){return pmOrPublic;}

    public String getContent() {
        return content;
    }

    public String getPostingUser() {
        return postingUser;
    }
    /**
     * turn the short of the opcode to a byte array, turn the char of pmOrPublic to a byte array
     * turn the content and posting user strings to a byte array
     * joins all the array to create a joined array
     * @return byte array that represents the message
     */
    /*
    public byte[] messageToEncode(){
        byte[]tmpOpcode=shortToBytes(getOpcode());
        byte[] tmpPmOrPublic=new byte[1];
        if(pmOrPublic=='1')
            tmpPmOrPublic[0]=(byte)1;
        else
            tmpPmOrPublic[0]=(byte)0;
        String stringOfPostInfo=""+postingUser+"\0"+content+"\0";
        byte[]postInfo=stringOfPostInfo.getBytes();
        int sizeOfEncodedArray=3+postInfo.length;
        byte[]encodedArray=new byte[sizeOfEncodedArray];
        encodedArray[0]=tmpOpcode[0];
        encodedArray[1]=tmpOpcode[1];
        encodedArray[2]=tmpPmOrPublic[0];
        for(int i=3; i<encodedArray.length; i++)
            encodedArray[i]=postInfo[i-3];
        return encodedArray;

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
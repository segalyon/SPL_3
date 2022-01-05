package bgu.spl.net.api.Messages;
public class ErrorMessage extends Message {
    //fields
    private short msgOpcode;

    public ErrorMessage(short msgOpcode) {
        super(new Integer(11).shortValue());
        this.msgOpcode=msgOpcode;
    }

    /**
     * turn the opcode to byte array, the message opcode and then join them to one byte array
     * @return byte array which represents the message
     */
    public byte[] messageToEncode() {
        byte[] ArrOpcode=shortToBytes(getOpcode());
        byte[] ArrMsgOpcode=shortToBytes(msgOpcode);
        byte[] res = {ArrOpcode[0], ArrOpcode[1], ArrMsgOpcode[0], ArrMsgOpcode[1]};
        return res;
    }

    // public String toString(){return  getOpcode()+""+messageOpcode;}

}

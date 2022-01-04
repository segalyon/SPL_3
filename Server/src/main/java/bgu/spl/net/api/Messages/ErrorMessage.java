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
        byte[]tmpOpcode=shortToBytes(getOpcode());
        byte[]tmpMessageOpcode=shortToBytes(msgOpcode);
        byte[] sendresult={tmpOpcode[0], tmpOpcode[1], tmpMessageOpcode[0], tmpMessageOpcode[1]};
        return sendresult;
    }

    // public String toString(){return  getOpcode()+""+messageOpcode;}

}

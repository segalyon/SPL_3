#include "../include/receiverChannel.h"

void ReceiverChannel::run() {
    bool isRunning = true;
    while (isRunning){
        char opcodeArr[2];
        connection.getBytes(opcodeArr, 2);
        short opcode = bytesToShort(opcodeArr);
        ///
        string msg;
        // NOTIFICATION
        if (opcode == 9) {
            msg = "NOTIFICATION";
            // public/PM
            char PMorPublic[1];
            connection.getBytes(PMorPublic, 1);
            short pmOrPublic = bytesToShort(PMorPublic);
            if(pmOrPublic==1)
                msg += " Public";
            else
                msg += " PM";
            // username
            string username;
            connection.getFrameAscii(username, '\0');
            msg += username;
            // content
            string content;
            connection.getFrameAscii(content, '\0');
            msg += content;
        }
        // ACK
        if (opcode == 10) {
            char msgOpcodeArr[2];
            connection.getBytes(msgOpcodeArr, 2);
            short msgOpcode = bytesToShort(msgOpcodeArr);
            msg = "ACK " + msgOpcode;
            //
            string optionalArg;
//            bool isArg = connection.getFrameAscii(optionalArg, ' ');
//            while(isArg) {
//                msg += " " + optionalArg;
//                isArg = connection.getFrameAscii(optionalArg, ' ');
//            }
        }
        // ERROR
        if (opcode == 11) {
            char errorArr[2];
            connection.getBytes(errorArr, 2);
            short error = bytesToShort(errorArr);
            msg = "ERROR " + std::to_string(error);
        }

        // print
        cout<<msg<<endl;

        // login/logout
        if(msg == "ACK 2") {
            loginState = true;
        }
        if(msg == "ACK 3") {
            loginState = false;
            isRunning = false;
            connection.close();
        }
    }


}